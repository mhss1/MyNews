package com.mhss.app.mynews.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mhss.app.mynews.R
import com.mhss.app.mynews.databinding.FragmentArticleDetailsBinding


class ArticleDetailsFragment : Fragment() {

    private lateinit var binding: FragmentArticleDetailsBinding
    private val args: ArticleDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article

        Glide
            .with(requireContext())
            .load(article.imageUrl)
            .placeholder(R.drawable.img_placeholder_ic)
            .error(R.drawable.error_ic)
            .into(binding.articleImg)

        binding.articleTitleTv.text = article.title
        binding.dateTv.text = article.publishedAt
        binding.sourceTv.text = article.source
        binding.articleContentTv.text = article.content

        binding.shareFab.setOnClickListener {
            shareArticle(article.url)
        }
        binding.goArticleBtn.setOnClickListener {
            goToArticle(article.url)
        }

    }

    private fun goToArticle(url: String){
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url))
        startActivity(intent)
    }

    private fun shareArticle(url: String){
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }
        startActivity(Intent.createChooser(shareIntent, "Share article with:"))
    }
}