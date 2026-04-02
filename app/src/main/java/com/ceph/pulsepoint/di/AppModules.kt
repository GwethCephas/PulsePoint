package com.ceph.pulsepoint.di


import android.content.Context
import androidx.room.Room
import androidx.work.WorkerParameters
import com.ceph.pulsepoint.data.local.ArticleDatabase
import com.ceph.pulsepoint.data.remote.ApiService
import com.ceph.pulsepoint.data.repository.PulseRepositoryImpl
import com.ceph.pulsepoint.data.worker.RandomNewsWorker
import com.ceph.pulsepoint.domain.repository.PulseRepository
import com.ceph.pulsepoint.presentation.favorite.FavoriteViewModel
import com.ceph.pulsepoint.presentation.home.HomeViewModel
import com.ceph.pulsepoint.presentation.search.SearchViewModel
import com.ceph.pulsepoint.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()

    }

    single { get<Retrofit>().create(ApiService::class.java) }

}

val viewModelModule = module {

    viewModel { HomeViewModel(get()) }

    viewModel { SearchViewModel(get()) }

    viewModel { FavoriteViewModel(get()) }


}

val repositoryModule = module {

    single { PulseRepositoryImpl(get(), get()) } bind PulseRepository::class

}

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            ArticleDatabase::class.java,
            "Article_db"
        ).build()
    }

    single { get<ArticleDatabase>().dao }

}

val workerModule = module {


    worker { (context: Context, params: WorkerParameters) ->
        RandomNewsWorker(context, params, get())
    }

}