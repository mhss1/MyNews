package com.mhss.app.mynews.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mhss.app.mynews.R
import com.mhss.app.mynews.databinding.TopHeadlinesCardListItemBinding
import com.mhss.app.mynews.domain.Article

class CardArticleItemRecAdapter(
    private val onItemClicked: (Article) -> Unit
) : ListAdapter<Article, CardArticleItemRecAdapter.ArticleViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            TopHeadlinesCardListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class ArticleViewHolder(private var binding: TopHeadlinesCardListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {

                cardContainer.transitionName = article.url
                articleTitleTv.transitionName = article.title
                cardContainer.setOnClickListener { onItemClicked(article) }
                Glide
                    .with(binding.root.context)
                    .load(article.imageUrl)
                    .placeholder(R.drawable.img_placeholder_ic)
                    .error(R.drawable.error_ic)
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