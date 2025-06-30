package com.ubaya.projectanmp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ubaya.projectanmp.model.Expense
import com.ubaya.projectanmp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ExpenseViewModel(app: Application)
    : AndroidViewModel(app), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val expensesLD = MutableLiveData<List<Expense>>()
    val messageLD  = MutableLiveData<String>()

    fun refresh(userId: Int) {
        launch {
            val list = buildDb(getApplication())
                .expenseDao().getExpensesByUser(userId)
            expensesLD.postValue(list)
        }
    }

    fun addExpense(budgetId: Int, amount: Int, note: String, userId: Int) {
        launch {
            if (amount < 0) { messageLD.postValue("Nominal negatif"); return@launch }
            buildDb(getApplication())
                .expenseDao().insert(
                    Expense(budgetId = budgetId, amount = amount, description = note)
                )
            refresh(userId)
        }
    }

    fun deleteExpense(id: Int, userId: Int) {
        launch {
            buildDb(getApplication()).expenseDao().deleteById(id)
            refresh(userId)
        }
    }

    override fun onCleared() { super.onCleared(); job.cancel() }
}