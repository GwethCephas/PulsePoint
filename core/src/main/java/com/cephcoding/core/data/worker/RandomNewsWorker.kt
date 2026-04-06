package com.cephcoding.core.data.worker

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.ceph.core.R
import com.cephcoding.core.domain.model.Article
import com.cephcoding.core.domain.repository.PulseRepository
import com.cephcoding.core.utils.Constants.PULSE_CHANNEL_ID
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import kotlin.random.Random

class RandomNewsWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: PulseRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                val yesterday = LocalDate.now().minusDays(1).toString()
                val today = LocalDate.now().toString()
                val randomNews =
                    repository.getTodayNews(from = yesterday, to = today).firstOrNull() ?: emptyList()
                if (randomNews.isNotEmpty()) {
                    val randomArticle = randomNews.random()
                    showNotification(article = randomArticle)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun showNotification(article: Article) {

        val imageBitmap: Bitmap? = try {
            val loader = ImageLoader(applicationContext)
            val imageRequest = ImageRequest.Builder(applicationContext)
                .data(article.urlToImage)
                .allowHardware(true)
                .build()
            val result = loader.execute(imageRequest)

            if (result is SuccessResult) {
                result.drawable.toBitmap()
            } else null

        } catch (e: Exception) {
            throw e

        }
        val notification = NotificationCompat.Builder(applicationContext, PULSE_CHANNEL_ID)
            .setContentTitle(article.title)
            .setContentText(article.description)
            .setStyle(NotificationCompat.BigTextStyle().bigText(article.description))
            .setSmallIcon(R.drawable.ic_data)
            .setAutoCancel(true)


        if (imageBitmap != null) {
            notification.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(imageBitmap)
                    .setBigContentTitle(article.description)

            )
        } else {
            notification.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(article.description)
            )
        }
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random.nextInt(), notification.build())
    }
}