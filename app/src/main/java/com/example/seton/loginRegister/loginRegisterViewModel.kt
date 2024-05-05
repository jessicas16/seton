package com.example.seton.loginRegister

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seton.config.ApiConfiguration
import com.example.seton.entity.BasicDRO
import com.example.seton.entity.userDTO
import com.example.seton.entity.userLoginDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class loginRegisterViewModel:ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var _response = MutableLiveData<BasicDRO>()

    val response: LiveData<BasicDRO>
        get() = _response

    suspend fun loginUser(user : userLoginDTO){
        try {
            ioScope.async(Dispatchers.IO) {
                val res = repo.loginUser(user)
                _response.postValue(res)
            }.await()
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            val res =  BasicDRO(
                status = "500",
                message = "An error occurred! Please try again later.",
                data = ""
            )
            _response.postValue(res)
        }
    }

    suspend fun registerUser(user : userDTO){
        try {
            ioScope.async(Dispatchers.IO) {
                val res = repo.registerUser(user)
                _response.postValue(res)
            }.await()
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            val res =  BasicDRO(
                status = "500",
                message = "An error occurred! Please try again later.",
                data = ""
            )
            _response.postValue(res)
        }
    }
}