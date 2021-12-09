package com.mhss.app.mynews.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mhss.app.mynews.databinding.TopHeadlinesParentItemBinding
import com.mhss.app.mynews.ui.fragments.MainFragmentDirections

class TopHeadlinesParentRecAdapter(
    private val owner: LifecycleOwner,
) : ListAdapter<TopHeadlinesItem, TopHeadlinesParentRecAdapter.TopHeadlineViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopHeadlineViewHolder {
        return TopHeadlineViewHolder(
            TopHeadlinesParentItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TopHeadlineViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class TopHeadlineViewHolder(private var binding: TopHeadlinesParentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TopHeadlinesItem) {
            binding.apply {
                categoryName.text = item.category
                val adapter = CardArticleItemRecAdapter {article ->
                        root.findNavController().navigate(
                            MainFragmentDirections.mainFragmentToDetailsFragment(article)
                        )
                }
                topHeadlinesRec.adapter = adapter
                item.list.observe(owner) { list ->
                    adapter.submitList(list)
                }

            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TopHeadlinesItem>() {
            override fun areItemsTheSame(
                oldItem: TopHeadlinesItem,
                newItem: TopHeadlinesItem
            ): Boolean {
                return oldItem.category == newItem.category
            }

            override fun areContentsTheSame(
                oldItem: TopHeadlinesItem,
                newItem: TopHeadlinesItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}