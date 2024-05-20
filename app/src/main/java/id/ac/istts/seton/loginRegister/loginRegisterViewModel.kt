package id.ac.istts.seton.loginRegister

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.entity.BasicDRO
import id.ac.istts.seton.entity.userDTO
import id.ac.istts.seton.entity.userLoginDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class loginRegisterViewModel:ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var _response = MutableLiveData<BasicDRO>()

    val response: LiveData<BasicDRO>
        get() = _response

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loginUser(user : userLoginDTO){
        try {
            ioScope.async(Dispatchers.IO) {
                val res = repo.loginUser(user)
                _response.postValue(res)
            }.await()
        } catch (e: Exception) {
            Log.e("Login ERROR", e.message.toString())
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loginUserWithGoogle(user : authUser){
        try {
            ioScope.async(Dispatchers.IO) {
                val res = repo.loginUserWithGoogle(user)
                _response.postValue(res)
            }.await()
        } catch (e: Exception) {
            Log.e("login google ERROR", e.message.toString())
            val res =  BasicDRO(
                status = "500",
                message = "An error occurred! Please try again later.",
                data = ""
            )
            _response.postValue(res)
        }
    }

    suspend fun registerUserWithGoogle(user : authUser){
        try {
            ioScope.async(Dispatchers.IO) {
                val res = repo.registerUserWithGoogle(user)
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