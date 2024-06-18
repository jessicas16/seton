package id.ac.istts.seton.taskPage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.entity.AddCommentDTO
import id.ac.istts.seton.entity.Attachments
import id.ac.istts.seton.entity.BasicDRO
import id.ac.istts.seton.entity.ChecklistDRO
import id.ac.istts.seton.entity.Checklists
import id.ac.istts.seton.entity.CommentDRO
import id.ac.istts.seton.entity.Comments
import id.ac.istts.seton.entity.DataComment
import id.ac.istts.seton.entity.FileUploadRequest
import id.ac.istts.seton.entity.LabelDRO
import id.ac.istts.seton.entity.Labels
import id.ac.istts.seton.entity.PostAttachmentDRO
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.TaskDRO
import id.ac.istts.seton.entity.Users
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class TaskDetailViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _task = MutableLiveData<TaskDRO>()
    private val _status = MutableLiveData<BasicDRO>()
    private val _label = MutableLiveData<LabelDRO>()
    private val _checklist = MutableLiveData<ChecklistDRO>()
    private val _postAttachment = MutableLiveData<PostAttachmentDRO>()
    private val _getAttachment = MutableLiveData<List<Attachments>>()
    private val _getChecklist = MutableLiveData<List<Checklists>>()
    private val _getComment = MutableLiveData<List<DataComment>>()
    private val _addComment = MutableLiveData<CommentDRO>()

    val task: MutableLiveData<TaskDRO>
        get() = _task

    val status: MutableLiveData<BasicDRO>
        get() = _status

    val label: MutableLiveData<LabelDRO>
        get() = _label

    val checklist: MutableLiveData<ChecklistDRO>
        get() = _checklist

    val postAttachment: MutableLiveData<PostAttachmentDRO>
        get() = _postAttachment

    val getAttachment: MutableLiveData<List<Attachments>>
        get() = _getAttachment

    val getChecklist: MutableLiveData<List<Checklists>>
        get() = _getChecklist

    val getComment: MutableLiveData<List<DataComment>>
        get() = _getComment

    val addComment: MutableLiveData<CommentDRO>
        get() = _addComment

    fun getTaskById (taskId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getTaskDetail(taskId)
                Log.i("Task response", res.data.toString())
                _task.value = res
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _task.value =TaskDRO(
                    status = "500",
                    message = "Internal Server Error",
                    data =  DataTask(
                        id = -1,
                        title = "",
                        deadline = "",
                        description = "",
                        priority = -1,
                        status = -1,
                        pic = Users(
                            email = "",
                            name = "",
                            profile_picture = "",
                            password = "",
                            auth_token = "",
                            status = -1
                        ),
                        project = Projects(
                            id = -1,
                            name = "",
                            description = "",
                            start = "",
                            deadline = "",
                            pm_email = "",
                            status = -1
                        ),
                        teams = listOf(),
                        comments = listOf(),
                        attachments = listOf(),
                        checklists = listOf(),
                        labels = listOf()
                    )
                )
            }
        }
    }

    fun updateTaskStatus(
        taskId: String,
        status: String
    ){
        viewModelScope.launch {
            try {
                val res = repo.updateTaskStatus(taskId, status)
                _status.value = res
                getTaskById(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _status.value = BasicDRO(
                    status = "500",
                    message = "Internal Server Error",
                    data = null
                )
            }
        }
    }

    fun addNewLabel(
        taskId: String,
        title: String,
    ){
        viewModelScope.launch {
            try {
                val res = repo.addLabelToTask(taskId, title)
                _label.value = res
                getTaskById(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _label.value = LabelDRO(
                    status = "500",
                    message = "Internal Server Error",
                    data = Labels(
                        id = -1,
                        title = "",
                        color = "",
                        task_id = ""
                    )
                )
            }
        }
    }

    fun addNewChecklist(
        taskId: String,
        title: String,
    ){
        viewModelScope.launch {
            try {
                val res = repo.addChecklist(taskId, title)
                Log.i("Checklist response", res.toString())
                _checklist.value = res
                getTaskById(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _checklist.value = ChecklistDRO(
                    status = "500",
                    message = "Internal Server Error",
                    data = Checklists(
                        id = -1,
                        title = "",
                        is_checked = -1,
                        task_id = ""
                    )
                )
            }
        }
    }

    fun addAttachment(
        taskId: String,
        file: MultipartBody.Part
    ){
        viewModelScope.launch {
            try {
                val files = FileUploadRequest(taskId, file)
                val res = repo.postAttachment(files)
                Log.i("Attachment response", res.toString())
                _postAttachment.value = res
                getTaskById(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
            }
        }
    }

    fun getAttachments(
        taskId: String
    ){
        Log.i("IH MASOK", "UTUUTUUTUT")
        viewModelScope.launch {
            try {
                val res = repo.getAttachment(taskId)
                Log.i("Attachment response", res.toString())
                _getAttachment.value = res.data
                getTaskById(taskId)
            } catch (e: Exception) {
                Log.e("ERROR bang", e.message.toString())
            }
        }
    }

    fun getChecklist(
        taskId: String
    ){
        viewModelScope.launch {
            try {
                val res = repo.getAllChecklist(taskId)
                Log.i("Checklist response", res.toString())
                _getChecklist.value = res.data
                getTaskById(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
            }
        }
    }

    fun updateChecklistStatus(
        taskId: String,
        checklistId: String
    ){
        viewModelScope.launch {
            try {
                val res = repo.updateChecklistStatus(checklistId)
                Log.i("Checklist response", res.toString())
                getChecklist(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
            }
        }
    }

    fun deleteChecklist(
        taskId: String,
        checklistId: String
    ){
        viewModelScope.launch {
            try {
                val res = repo.deleteChecklist(checklistId)
                Log.i("Checklist response", res.toString())
                getChecklist(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
            }
        }
    }

    fun getAllComment(
        taskId: String
    ){
        viewModelScope.launch {
            try {
                val res = repo.getAllComment(taskId)
                Log.i("Comment response", res.toString())
                _getComment.value = res.data
                getTaskById(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
            }
        }
    }

    fun addCommentTask(
        taskId: String,
        com : AddCommentDTO
    ){
        viewModelScope.launch {
            try {
                val res = repo.addCommentTask(com)
                Log.i("Comment response", res.toString())
                _addComment.value = res
                getAllComment(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _addComment.value = CommentDRO(
                    status = "500",
                    message = "Internal Server Error",
                    data = Comments(
                        id = -1,
                        value = "",
                        time = "",
                        user_email = "",
                        task_id = ""
                    )
                )
            }
        }
    }
}