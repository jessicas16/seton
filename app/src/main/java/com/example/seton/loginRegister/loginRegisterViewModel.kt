package com.example.seton.loginRegister

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seton.config.ApiConfiguration
import com.example.seton.entity.BasicDTO
import com.example.seton.entity.userDRO
import com.example.seton.entity.userLoginDRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class loginRegisterViewModel:ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var _response = MutableLiveData<BasicDTO>()

    val response: LiveData<BasicDTO>
        get() = _response

    fun loginUser(user : userLoginDRO){
        try {
            ioScope.launch {
                val res = repo.loginUser(user)
                _response.postValue(res)
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            val res =  BasicDTO(
                status = "500",
                message = "An error occurred! Please try again later.",
                data = ""
            )
            _response.postValue(res)
        }
    }

    fun registerUser(user : userDRO){
        try {
            ioScope.launch {
                val res = repo.registerUser(user)
                _response.postValue(res)
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            val res =  BasicDTO(
                status = "500",
                message = "An error occurred! Please try again later.",
                data = ""
            )
            _response.postValue(res)
        }
    }
}