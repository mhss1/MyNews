package com.mhss.app.mynews.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mhss.app.mynews.R
import com.mhss.app.mynews.databinding.ArticleSimpleListItemBinding
import com.mhss.app.mynews.domain.Article

class SimpleArticleItemRecAdapter(
    private val onItemClicked: (Article) -> Unit
) : ListAdapter<Article, SimpleArticleItemRecAdapter.ArticleViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ArticleSimpleListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class ArticleViewHolder(private var binding: ArticleSimpleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {

                articleContainer.transitionName = article.url
                articleContainer.setOnClickListener { onItemClicked(article) }
                Glide
                    .with(binding.root.context)
                    .load(article.imageUrl)
                    .placeholder(R.drawable.img_placeholder_ic)
                    .error(R.drawable.error_ic)
                    .centerCrop()
                    .into(articleImg)
                articleTitleTv.text = article.title
                dateTv.text = article.publishedAt
                sourceTv.text = article.source

            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}