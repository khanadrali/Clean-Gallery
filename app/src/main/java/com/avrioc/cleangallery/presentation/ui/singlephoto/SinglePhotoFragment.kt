package com.avrioc.cleangallery.presentation.ui.singlephoto

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.avrioc.cleangallery.presentation.ui.SharedViewModel
import com.example.testgallaryapplication.databinding.FragmentSinglePhotoBinding

class SinglePhotoFragment : Fragment() {

    private val singlePhotoViewModel: SinglePhotoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentSinglePhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSinglePhotoBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    private fun initViews() {
        binding.apply {
            viewModel = singlePhotoViewModel
            lifecycleOwner = viewLifecycleOwner
            lottieView = binding.lottieAnimation
        }
        singlePhotoViewModel.singleImage.value = sharedViewModel.singleImage
    }

    private fun initObservers() {

        singlePhotoViewModel.navigateBack.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            }
        }

    }
}