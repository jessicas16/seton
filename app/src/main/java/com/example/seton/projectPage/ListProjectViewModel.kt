package com.example.seton.projectPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.config.ApiConfiguration
import kotlinx.coroutines.launch

class ListProjectViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _projects = MutableLiveData<List<DataProject>>()

    val projects: LiveData<List<DataProject>>
        get() = _projects

    fun getUserProjects() {
        viewModelScope.launch {
            try {
                val res = repo.getUserProjects()
                Log.i("DATA_PROJECTS", res.data.toString())
                _projects.value = res.data
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _projects.value = emptyList()
            }
        }
    }
}