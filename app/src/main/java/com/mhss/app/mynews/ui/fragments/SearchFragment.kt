package com.mhss.app.mynews.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.mhss.app.mynews.databinding.FragmentSearchBinding
import com.mhss.app.mynews.ui.recyclerview.SimpleArticleItemAdapter
import com.mhss.app.mynews.ui.veiwmodels.ArticlesViewModel
import com.mhss.app.mynews.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: ArticlesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val adapter = SimpleArticleItemAdapter {
            findNavController().navigate(SearchFragmentDirections.searchFragmentToDetailsFragment(it))
        }
        binding.articlesRec.adapter = adapter

        binding.articleSearchEdt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setLastResult(null)
                viewModel.searchArticles(
                    binding.articleSearchEdt.text.toString(),
                    preferenceManager.getString("language", "") ?: "")
                hideKeyboard()
                binding.articleSearchEdt.clearFocus()
            }
            true
        }

        viewModel.lastResult.observe(viewLifecycleOwner){
            // to avoid requesting the same query again when coming back from details fragment
            if (it != null) {
                adapter.submitList(it)
                binding.noArticlesTv.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            } else {
                viewModel.articlesByQuery().observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is DataState.Loading -> showProgressBar(true)
                        is DataState.Success -> {
                            binding.errorTv.visibility = View.GONE
                            showProgressBar(false)
                            binding.noArticlesTv.visibility =
                                if (state.data.isEmpty()) View.VISIBLE
                                else View.GONE

                            adapter.submitList(state.data)
                            viewModel.setLastResult(state.data)
                        }
                        is DataState.Error -> {
                            showProgressBar(false)
                            binding.errorTv.visibility = View.VISIBLE
                            binding.errorTv.text = state.message
                        }
                    }
                }
                binding.articleSearchEdt.requestFocus()
                showKeyboard()
            }
        }


    }

    private fun hideKeyboard() {
        activity?.currentFocus?.let { view ->
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(binding.articleSearchEdt, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}