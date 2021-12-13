package com.mhss.app.mynews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayoutMediator
import com.mhss.app.mynews.R
import com.mhss.app.mynews.databinding.FragmentMainBinding
import com.mhss.app.mynews.ui.recyclerview.TopHeadlinesItem
import com.mhss.app.mynews.ui.recyclerview.TopHeadlinesParentAdapter
import com.mhss.app.mynews.ui.veiwmodels.ArticlesViewModel
import com.mhss.app.mynews.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: ArticlesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setTodayDate()
        val adapter = TopHeadlinesParentAdapter(viewLifecycleOwner)
        adapter.submitList(getTopHeadlinesList())
        binding.topHeadlinesParentViewPager.adapter = adapter

        TabLayoutMediator(
            binding.topHeadlinesParentTabLayout,
            binding.topHeadlinesParentViewPager)
        {tab, position ->
            val item = getTopHeadlinesList()[position]
            tab.text = item.category
            tab.setIcon(item.icon)
        }.attach()

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refreshArticles(preferenceManager.getString("country", "") ?: "")
        }

        viewModel.refreshState.observe(viewLifecycleOwner) { state ->
            when(state){
                is DataState.Loading -> binding.refreshLayout.isRefreshing = true
                is DataState.Success -> {
                    binding.refreshLayout.isRefreshing = false
                    binding.errorTv.visibility = View.GONE
                }
                is DataState.Error -> {
                    binding.refreshLayout.isRefreshing = false
                    binding.errorTv.visibility = View.VISIBLE
                    binding.errorTv.text = state.message
                }
            }

        }

    }

    private fun setTodayDate(){
        val today = Calendar.getInstance().time
        val sdf = SimpleDateFormat("EEEE, MMM dd,yyyy", Locale.getDefault())
        binding.dayTv.text = sdf.format(today)

    }

    private fun getTopHeadlinesList() =
        listOf(
            TopHeadlinesItem("Top Headlines", viewModel.generalArticles, R.drawable.top_headlines_ic),
            TopHeadlinesItem("Top in tech", viewModel.techArticles, R.drawable.tech_ic),
            TopHeadlinesItem("Top in health", viewModel.healthArticles, R.drawable.health_ic),
            TopHeadlinesItem("Top in entertainment", viewModel.entertainmentArticles, R.drawable.entertainment_ic),
            TopHeadlinesItem("Top in sports", viewModel.sportsArticles, R.drawable.sports_ic),
            TopHeadlinesItem("Top in science", viewModel.scienceArticles, R.drawable.science_ic),
            TopHeadlinesItem("Top in business", viewModel.businessArticles, R.drawable.business_ic),
        )
}