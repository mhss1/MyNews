package com.mhss.app.mynews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mhss.app.mynews.R
import com.mhss.app.mynews.databinding.FragmentDiscoverBinding
import com.mhss.app.mynews.ui.recyclerview.DiscoverCategoriesAdapter
import com.mhss.app.mynews.ui.recyclerview.DiscoverFragmentCategoryItem
import com.mhss.app.mynews.ui.recyclerview.SimpleArticleItemAdapter
import com.mhss.app.mynews.ui.veiwmodels.ArticlesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private lateinit var binding: FragmentDiscoverBinding
    private val viewModel: ArticlesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnClickListener {
            findNavController()
                .navigate(DiscoverFragmentDirections.discoverFragmentToSearchFragment())
        }

        val categoriesAdapter = DiscoverCategoriesAdapter{
            findNavController().navigate(
                DiscoverFragmentDirections.discoverFragmentToArticlesListFragment(it))
        }
        categoriesAdapter.submitList(getCategories())
        binding.categoriesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesList.adapter = categoriesAdapter

        val discoverArticlesAdapter = SimpleArticleItemAdapter{
            findNavController().navigate(
                DiscoverFragmentDirections.discoverFragmentToDetailsFragment(it))
        }
        binding.discoverList.adapter = discoverArticlesAdapter
        viewModel.generalArticles.observe(viewLifecycleOwner){
            discoverArticlesAdapter.submitList(it)
        }

    }

    private fun getCategories() =
             listOf(
                DiscoverFragmentCategoryItem("Tech", R.drawable.cpu, R.color.red),
                DiscoverFragmentCategoryItem("Entertainment", R.drawable.popcorn, R.color.purple_500),
                DiscoverFragmentCategoryItem("Health", R.drawable.heart, R.color.green),
                DiscoverFragmentCategoryItem("Sports", R.drawable.soccer_ball, R.color.yellow),
                DiscoverFragmentCategoryItem("Science", R.drawable.atom, R.color.gray),
                DiscoverFragmentCategoryItem("Business", R.drawable.money, R.color.blue)
             )
}