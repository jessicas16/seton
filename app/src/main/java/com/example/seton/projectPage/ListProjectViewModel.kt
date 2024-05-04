package com.example.seton.projectPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seton.config.ApiConfiguration
import com.example.seton.entity.BasicDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ListProjectViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var _response = MutableLiveData<BasicDTO>()
}