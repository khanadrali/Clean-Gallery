package com.avrioc.cleangallery.presentation.ui.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.avrioc.cleangallery.domain.model.Album
import com.avrioc.cleangallery.presentation.adapter.album.AlbumAdapter
import com.avrioc.cleangallery.presentation.ui.SharedViewModel
import com.example.testgallaryapplication.R
import com.example.testgallaryapplication.databinding.FragmentAlbumListBinding
import com.example.testgallaryapplication.presentation.listeners.AlbumClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumListFragment : Fragment(), AlbumClickListener {


    private val albumListViewModel: AlbumListViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentAlbumListBinding
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private var rvAlbumAdapter: AlbumAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    override fun onAlbumClick(album: Album) {
        sharedViewModel.apply {
            albumName = album.albumName
            mediaList.value = albumListViewModel.mediaList.value
        }
        findNavController().navigate(R.id.action_albumListFragment_to_albumDetailFragment)
    }

    fun onChangeLayoutClick() {

        val viewType = albumListViewModel.viewType.value
        val list = albumListViewModel.albumList.value
        if (viewType == AlbumAdapter.VIEW_TYPE_GRID) changeToLinearLayout() else changeToGridLayout()
        albumListViewModel.albumList.value = getSortedList(list)
    }

    private fun changeToGridLayout() {
        binding.rvAlbum.layoutManager = staggeredGridLayoutManager
        staggeredGridLayoutManager.spanCount = AlbumAdapter.VIEW_TYPE_GRID
        rvAlbumAdapter?.setViewType(AlbumAdapter.VIEW_TYPE_GRID)
        albumListViewModel.viewType.value = AlbumAdapter.VIEW_TYPE_GRID
    }

    private fun changeToLinearLayout() {
        binding.rvAlbum.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvAlbumAdapter?.setViewType(AlbumAdapter.VIEW_TYPE_LINEAR)
        albumListViewModel.viewType.value = AlbumAdapter.VIEW_TYPE_LINEAR
    }

    private fun getSortedList(list: MutableList<Album>?) = list?.let {
        if (albumListViewModel.viewType.value == AlbumAdapter.VIEW_TYPE_LINEAR) {
            it.sortedBy { album -> album.albumName }
        } else {
            it.sortByDescending { date -> date.dateAdded }
            it
        }.toMutableList()
    }

    private fun initObservers() {

        sharedViewModel.loadMedia.observe(viewLifecycleOwner) { isLoadMedia ->
            if (isLoadMedia) albumListViewModel.loadMedia()
        }

        albumListViewModel.albumList.observe(viewLifecycleOwner) { albumList ->
            if (rvAlbumAdapter == null) {
                rvAlbumAdapter = AlbumAdapter(albumList, this@AlbumListFragment, getDynamicSize())
                binding.rvAlbum.adapter = rvAlbumAdapter
            } else {
                rvAlbumAdapter?.updateList(albumList)
                binding.rvAlbum.adapter = rvAlbumAdapter
            }
        }
    }

    private fun initViews() {

        staggeredGridLayoutManager = StaggeredGridLayoutManager(
            albumListViewModel.viewType.value ?: 0, GridLayoutManager.VERTICAL
        )
        binding.apply {
            fragment = this@AlbumListFragment
            viewModel = albumListViewModel
            lifecycleOwner = viewLifecycleOwner
            rvAlbum.layoutManager = staggeredGridLayoutManager
            rvAlbum.setHasFixedSize(true)
        }

    }

    private fun getDynamicSize(): Int {
        val spanCount = staggeredGridLayoutManager.spanCount
        return if (spanCount > 0) {
            context?.resources?.displayMetrics?.widthPixels?.div(spanCount) ?: 0
        } else {
            0
        }
    }


}