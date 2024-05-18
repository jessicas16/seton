package id.ac.istts.seton.config

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import id.ac.istts.seton.config.local.AppDatabase
import id.ac.istts.seton.env
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
            ).fallbackToDestructiveMigration().build()

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