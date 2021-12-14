package com.mhss.app.mynews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mhss.app.mynews.R
import com.mhss.app.mynews.databinding.FragmentArticlesListBinding
import com.mhss.app.mynews.ui.recyclerview.SimpleArticleItemAdapter
import com.mhss.app.mynews.ui.veiwmodels.ArticlesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesListFragment : Fragment(R.layout.fragment_articles_list) {

    private lateinit var binding: FragmentArticlesListBinding
    private val viewModel: ArticlesViewModel by viewModels()
    private val args: ArticlesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticlesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SimpleArticleItemAdapter{
            findNavController().navigate(
                ArticlesListFragmentDirections.articlesListFragmentToDetailsFragment(it))
        }
        categoryToList(args.category).observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        binding.articlesList.adapter = adapter
    }

    private fun categoryToList(category: String) =
        when(category){
            "Tech" -> viewModel.techArticles
            "Entertainment" -> viewModel.entertainmentArticles
            "Health" -> viewModel.healthArticles
            "Sports" -> viewModel.sportsArticles
            "Science" -> viewModel.scienceArticles
            else -> viewModel.businessArticles
        }


}