package com.ubaya.projectanmp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ubaya.projectanmp.model.User
import com.ubaya.projectanmp.util.SessionManager
import com.ubaya.projectanmp.util.buildDb
import com.ubaya.projectanmp.util.sha256
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class AuthViewModel(app: Application)
    : AndroidViewModel(app), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val messageLD = MutableLiveData<String>()

//  Sign Up
    fun register(username: String, first: String, last: String,
                 pass: String, repeat: String) {
        launch {
            if (pass.length < 8) {
                messageLD.postValue("Password minimal 8 karakter")
                return@launch
            }
            if (pass != repeat) {
                messageLD.postValue("Password dan ulangi password tidak sama")
                return@launch
            }
            val db = buildDb(getApplication())
            try {
                db.userDao().register(
                    User(username = username,
                        firstName = first, lastName = last,
                        password = pass.sha256())
                )
                messageLD.postValue("Registrasi berhasil, silakan login")
            } catch (e: Exception) {
                messageLD.postValue("Username sudah digunakan")
            }
        }
    }

//   Sign In
    fun login(username: String, pass: String,
              onSuccess: () -> Unit) {
        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().login(username, pass.sha256())
            if (user != null) {
                SessionManager.login(getApplication(), user.id, user.username)
                withContext(Dispatchers.Main) { onSuccess() }
            } else {
                messageLD.postValue("Username / Password salah")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}