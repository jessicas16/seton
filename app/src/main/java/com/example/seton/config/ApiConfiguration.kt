package com.example.seton.config

import com.example.seton.env
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiConfiguration{
    companion object{
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(env.URL)
            .build()

//        val retrofitService:ServiceStudent by lazy {
//            retrofit.create(ServiceStudent::class.java)
//        }
    }
}