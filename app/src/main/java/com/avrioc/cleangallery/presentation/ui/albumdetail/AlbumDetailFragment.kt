package com.avrioc.cleangallery.presentation.ui.albumdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.avrioc.cleangallery.R
import com.avrioc.cleangallery.databinding.FragmentAlbumDetailBinding
import com.avrioc.cleangallery.domain.model.Media
import com.avrioc.cleangallery.presentation.adapter.PhotoAdapter
import com.avrioc.cleangallery.presentation.ui.SharedViewModel
import com.example.testgallaryapplication.presentation.listeners.PhotoClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(), PhotoClickListener {

    private lateinit var binding: FragmentAlbumDetailBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val albumDetailViewModel: AlbumDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    override fun onPhotoClick(media: Media) {
        sharedViewModel.singleImage = media
        findNavController().navigate(
            R.id.action_albumDetailFragment_to_singlePhotoFragment
        )
    }

    private fun initViews() {

        binding.apply {
            viewModel = albumDetailViewModel
            lifecycleOwner = viewLifecycleOwner
            rvPhotos.layoutManager =
                StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            rvPhotos.setHasFixedSize(true)
        }
        binding.rvPhotos.adapter = PhotoAdapter(getDynamicSize(), this)
        albumDetailViewModel.apply {
            albumName.value = sharedViewModel.albumName
            sharedViewModel.mediaList.value?.let { setMediaList(it) }
        }
    }

    private fun initObservers() {

        albumDetailViewModel.mediaList.observe(viewLifecycleOwner) { mediaList ->
            (binding.rvPhotos.adapter as PhotoAdapter).submitList(mediaList)
        }

        albumDetailViewModel.navigateBack.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            }
        }
        binding.rvPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val lastVisiblePositions = layoutManager.findLastVisibleItemPositions(null)
                val lastVisiblePosition = lastVisiblePositions.maxOrNull() ?: -1

                // Check if we have scrolled to the end of the list
                if (!albumDetailViewModel.loading.value!! && lastVisiblePosition >= (binding.rvPhotos.adapter?.itemCount
                        ?: 0) - 1
                ) {
                    albumDetailViewModel.loadNextPage()
                }
            }
        })

    }

    private fun getDynamicSize() = context?.resources?.displayMetrics?.widthPixels!!.div(4)
}