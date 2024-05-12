package com.example.seton.config

import android.content.Context
import androidx.room.Room
import com.example.seton.config.local.AppDatabase
import com.example.seton.env
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
class ApiConfiguration{
    companion object{
        lateinit var defaultRepo: DefaultRepo
        fun getApiService(context: Context) {
            //LOCAL
            val roomDb = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "db_seton"
            )

            // API
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(env.URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .build()

            defaultRepo = DefaultRepo(roomDb, retrofit.create(ApiService::class.java))
        }
    }
}