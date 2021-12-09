package com.mhss.app.mynews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhss.app.mynews.R
import com.mhss.app.mynews.databinding.FragmentMainBinding
import com.mhss.app.mynews.ui.recyclerview.CardArticleItemRecAdapter
import com.mhss.app.mynews.ui.recyclerview.TopHeadlinesItem
import com.mhss.app.mynews.ui.recyclerview.TopHeadlinesParentRecAdapter
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

        setTodayDate()
        val adapter = TopHeadlinesParentRecAdapter(viewLifecycleOwner)
        adapter.submitList(getTopHeadlinesList())
        binding.articlesRec.adapter = adapter

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refreshArticles()
        }

        viewModel.refreshState.observe(viewLifecycleOwner) { state ->
            when(state){
                is DataState.Loading ->
                {
                    binding.refreshLayout.isRefreshing = true
                }
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
            TopHeadlinesItem("Top Headlines", viewModel.generalArticles),
            TopHeadlinesItem("Top in tech", viewModel.techArticles),
            TopHeadlinesItem("Top in health", viewModel.healthArticles),
            TopHeadlinesItem("Top in entertainment", viewModel.entertainmentArticles),
            TopHeadlinesItem("Top in sports", viewModel.sportsArticles),
            TopHeadlinesItem("Top in science", viewModel.scienceArticles),
            TopHeadlinesItem("Top in business", viewModel.businessArticles),
        )
}