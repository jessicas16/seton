package com.example.seton.projectPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seton.config.ApiConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListProjectViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var _response = MutableLiveData<List<DataProject>>()

    val response: LiveData<List<DataProject>>
        get() = _response

    suspend fun getUserProjects() {
        try {
            val res = repo.getUserProjects()
            _response.postValue(res.data as List<DataProject>)
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            _response.postValue(emptyList())
        }
    }
}