package com.mhss.app.mynews.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mhss.app.mynews.databinding.DiscoverCategoriesItemBinding

class DiscoverCategoriesAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<DiscoverFragmentCategoryItem, DiscoverCategoriesAdapter.CategoryItemViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(
            DiscoverCategoriesItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner

    class CategoryItemViewHolder(private var binding: DiscoverCategoriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiscoverFragmentCategoryItem) {
            binding.apply {
                root.transitionName = item.name
                root.setOnClickListener { onItemClicked(item.name) }
                categoryName.text = item.name
                icon.setImageResource(item.icon)
                holderCard.transitionName = item.name
                holderCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        item.backgroundColor
                    )
                )

            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DiscoverFragmentCategoryItem>() {
            override fun areItemsTheSame(
                oldItem: DiscoverFragmentCategoryItem,
                newItem: DiscoverFragmentCategoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: DiscoverFragmentCategoryItem,
                newItem: DiscoverFragmentCategoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}