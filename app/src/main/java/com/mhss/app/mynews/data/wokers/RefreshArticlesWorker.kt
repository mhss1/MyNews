package com.mhss.app.mynews.data.wokers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.mhss.app.mynews.data.repository.ArticlesRepository
import com.mhss.app.mynews.ui.veiwmodels.ArticlesViewModel
import com.mhss.app.mynews.util.DataState
import com.mhss.app.mynews.util.countryToCode
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException

@HiltWorker
class RefreshArticlesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val articlesRepository: ArticlesRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val country = inputData.getString("country") ?: "United States"
        try {
            articlesRepository.refreshArticles(country.countryToCode())
        }catch (e: Exception){
            return Result.retry()
        }
        return Result.success()
    }

}
