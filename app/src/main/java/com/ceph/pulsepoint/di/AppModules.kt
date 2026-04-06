package com.ceph.pulsepoint.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkerParameters
import com.cephcoding.core.authentication.GoogleAuthClient
import com.cephcoding.core.data.local.ArticleDatabase
import com.cephcoding.core.data.remote.ApiService
import com.cephcoding.core.data.repository.PulseRepositoryImpl
import com.cephcoding.core.data.worker.RandomNewsWorker
import com.cephcoding.core.domain.repository.PulseRepository
import com.cephcoding.core.utils.Constants
import com.cephcoding.features.feat_favorite.FavoriteViewModel
import com.cephcoding.features.feat_home.HomeViewModel
import com.cephcoding.features.feat_search.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    //Firebase
    single { FirebaseAuth.getInstance() }

    //Auth
    single { GoogleAuthClient(androidContext(), get()) }

    //Worker
    worker { (context: Context, params: WorkerParameters) ->
        RandomNewsWorker(context, params, get())
    }
}

val coreModule = module {

    // Network call
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()

    }
    single { get<Retrofit>().create(ApiService::class.java) }

    //Repository
    single { PulseRepositoryImpl(get(), get()) } bind PulseRepository::class

    //Database
    single {
        Room.databaseBuilder(
            androidContext(),
            ArticleDatabase::class.java,
            "Article_db"
        ).build()
    }

    single { get<ArticleDatabase>().dao }
}

val featureModule = module {
    viewModel { HomeViewModel(get()) }

    viewModel { SearchViewModel(get()) }

    viewModel { FavoriteViewModel(get()) }
}
