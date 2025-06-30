package com.ubaya.projectanmp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ubaya.projectanmp.util.SessionManager
import com.ubaya.projectanmp.util.buildDb
import com.ubaya.projectanmp.util.sha256
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application)
    : AndroidViewModel(app), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + Dispatchers.IO

    val oldPass    = MutableLiveData("")
    val newPass    = MutableLiveData("")
    val repeatPass = MutableLiveData("")

    val messageLD = MutableLiveData<String>()

    private val userId = SessionManager.userId(app)

    fun onChangeClick() = launch {
        val old = oldPass.value.orEmpty()
        val new = newPass.value.orEmpty()
        val rep = repeatPass.value.orEmpty()

        if (new != rep) {
            messageLD.postValue("New & Repeat tidak sama"); return@launch
        }
        val row = buildDb(getApplication())
            .userDao().changePassword(userId, old.sha256(), new.sha256())

        messageLD.postValue(
            if (row == 1) "Password berhasil diubah"
            else "Old password salah"
        )
        if (row == 1) {
            oldPass.postValue(""); newPass.postValue(""); repeatPass.postValue("")
        }
    }

    fun signOut(app: Application) = SessionManager.logout(app)

    override fun onCleared() { job.cancel() }
}