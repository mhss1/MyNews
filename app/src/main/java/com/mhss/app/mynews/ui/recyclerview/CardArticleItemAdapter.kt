package com.mhss.app.mynews.ui.recyclerview

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mhss.app.mynews.R
import com.mhss.app.mynews.databinding.TopHeadlinesCardListItemBinding
import com.mhss.app.mynews.domain.Article

class CardArticleItemAdapter(
    private val onItemClicked: (Article) -> Unit
) : ListAdapter<Article, CardArticleItemAdapter.ArticleViewHolder>(DiffCallback) {

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

                articleImg.visibility = View.VISIBLE
                cardContainer.setOnClickListener { onItemClicked(article) }
                Glide
                    .with(binding.root.context)
                    .load(article.imageUrl)
                    .placeholder(R.drawable.img_placeholder_ic)
                    .listener(object : RequestListener<Drawable?>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            articleImg.visibility = View.GONE
                            return true
                        }
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ) = false

                    })
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