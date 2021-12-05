package com.mhss.app.mynews.data.wokers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mhss.app.mynews.data.repository.ArticlesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshArticlesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val articlesRepository: ArticlesRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        articlesRepository.refreshArticles()
        return Result.success()
    }

}
