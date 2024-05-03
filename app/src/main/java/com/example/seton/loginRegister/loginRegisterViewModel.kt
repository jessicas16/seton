package com.example.seton.loginRegister

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seton.config.ApiConfiguration
import com.example.seton.entity.BasicDTO
import com.example.seton.entity.Users
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

    fun loginUser(user : userLoginDRO): Unit{
        ioScope.launch {
            try {
                val response = repo.loginUser(user)
                Log.e("INIIIIII", response.toString())
                _response.value = response

            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _response.value = BasicDTO(
                    status = "500",
                    message = "An error occurred! Please try again later.",
                    data = ""
                )
            }
        }
    }
}