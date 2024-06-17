package id.ac.istts.seton.reportPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.projectPage.DataProject
import kotlinx.coroutines.launch

class ReportViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _projects = MutableLiveData<List<DataProject>>()
    private val _members = MutableLiveData<List<Users>>()

    val projects: LiveData<List<DataProject>>
        get() = _projects

    val members: LiveData<List<Users>>
        get() = _members

    fun getUserProjects(email : String) {
        viewModelScope.launch {
            try {
                val res = repo.getUserProjects(true, email)
                Log.i("MESSAGE", res.message)
                Log.i("DATA_PROJECTS", res.data.toString())
                _projects.value = res.data
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _projects.value = emptyList()
            }
        }
    }

    fun getUserPIC(emailList: List<String>){
        viewModelScope.launch {
            try {
                _members.value = repo.getUserPIC(emailList)
            }catch (e: Exception){
                _members.value = emptyList()
            }
        }
    }


}