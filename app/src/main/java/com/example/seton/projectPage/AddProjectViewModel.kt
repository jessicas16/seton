package com.example.seton.projectPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.config.ApiConfiguration
import com.example.seton.entity.BasicDRO
import com.example.seton.entity.UserDRO
import com.example.seton.entity.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddProjectViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _invitedUsers = MutableLiveData<List<Users>>()
    private val _checkEmail = MutableLiveData<UserDRO>()

    val invitedUsers: LiveData<List<Users>>
        get() = _invitedUsers

    val checkEmail: LiveData<UserDRO>
        get() = _checkEmail

    suspend fun checkEmailUser(email : String = "") {
        viewModelScope.launch {
        try {
            val res = repo.checkEmail(email = email)
            if(res.status == "200") {
                val data = res.data as Users
                val list = _invitedUsers.value?.toMutableList() ?: mutableListOf()
                list.add(data)
                _invitedUsers.postValue(list)
            }
            _checkEmail.postValue(res)
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            val res =  UserDRO(
                status = "500",
                message = "An error occurred! Please try again later.",
                data = Users("", "", null, "", null, -1)
            )
            _checkEmail.postValue(res)
        }
        }
    }
}