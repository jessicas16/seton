package id.ac.istts.seton.settingPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.entity.UserDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.projectPage.DataProject
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SettingViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _user = MutableLiveData<Users>()

    val user: LiveData<Users>
        get() = _user

    suspend fun updatePassword(email: String, oldPassword: String, newPassword: String): String {
        return try {
            val res = repo.updatePassword(email, oldPassword, newPassword)
            res.message
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            "Server error!"
        }
    }

    suspend fun updateProfile(email: String, name: String, profilePicture: MultipartBody.Part?): String {
        return try {
            val res = repo.updateProfile(email, profilePicture, name)
            res.message
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            "Server error!"
        }
    }

    suspend fun getUser(email: String) {
        try {
            val res = repo.checkEmail(email)
            if (res.status == "200") {
                _user.postValue(res.data)
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
    }
}