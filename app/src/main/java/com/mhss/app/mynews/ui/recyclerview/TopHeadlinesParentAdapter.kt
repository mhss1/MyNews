package com.mhss.app.mynews.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mhss.app.mynews.databinding.TopHeadlinesParentItemBinding
import com.mhss.app.mynews.ui.fragments.MainFragmentDirections

class TopHeadlinesParentAdapter(
    private val owner: LifecycleOwner,
) : ListAdapter<TopHeadlinesItem, TopHeadlinesParentAdapter.TopHeadlineViewHolder>(DiffCallback) {

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
                icon.setImageResource(item.icon)
                val adapter = CardArticleItemAdapter { article ->
                        root.findNavController().navigate(
                            MainFragmentDirections.mainFragmentToDetailsFragment(article)
                        )
                }
                topHeadlinesRec.adapter = adapter
                item.list.observe(owner) { list ->
                    adapter.submitList(list)
                    if (list.isEmpty())
                        noArticlesTv.visibility = View.VISIBLE
                    else
                        noArticlesTv.visibility = View.GONE
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