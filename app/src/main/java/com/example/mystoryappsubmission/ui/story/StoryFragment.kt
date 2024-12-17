package com.example.mystoryappsubmission.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryappsubmission.R
import com.example.mystoryappsubmission.adapter.ListStoryAdapter
import com.example.mystoryappsubmission.databinding.FragmentMystoryBinding
import com.example.mystoryappsubmission.ui.ViewModelFactory
import com.example.mystoryappsubmission.ui.add.AddStoryFragment

class StoryFragment : Fragment() {

    private var _binding: FragmentMystoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val storyAdapter by lazy { ListStoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMystoryBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        bindViewModel()

        binding.AddContent.setOnClickListener {
            // Membuka AddStoryFragment ketika AddContent di klik
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val addStoryFragment = AddStoryFragment()
            fragmentTransaction.replace(R.id.fragment_activity, addStoryFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(requireContext()).also { manager ->
                addItemDecoration(DividerItemDecoration(context, manager.orientation))
            }
            adapter = storyAdapter
        }
    }

    private fun bindViewModel() {
        viewModel.story.observe(viewLifecycleOwner) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

}





