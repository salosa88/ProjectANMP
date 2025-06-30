package com.ubaya.projectanmp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ubaya.projectanmp.model.Budget
import com.ubaya.projectanmp.model.BudgetWithExpenses
import com.ubaya.projectanmp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BudgetViewModel(app: Application)
    : AndroidViewModel(app), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val budgetsLD = MutableLiveData<List<BudgetWithExpenses>>()
    val messageLD = MutableLiveData<String>()

    fun refresh(userId: Int) {
        launch {
            val list = buildDb(getApplication())
                .budgetDao().getBudgetWithExpenses(userId)
            budgetsLD.postValue(list)
        }
    }

    fun addBudget(userId: Int, name: String, max: Int) {
        launch {
            if (name.isBlank()) { messageLD.postValue("Nama harus diisi"); return@launch }
            if (max < 0)        { messageLD.postValue("Nominal negatif");  return@launch }

            buildDb(getApplication())
                .budgetDao()
                .insert(Budget(ownerId = userId, name = name, maxAmount = max))

            refresh(userId)
        }
    }

    fun updateBudget(budget: Budget, newMax: Int, userId: Int) {
        launch {
            val dao = buildDb(getApplication()).budgetDao()
            val spent = dao.sumExpense(budget.id)

            if (newMax < spent) {
                messageLD.postValue("Nominal < total expense ($spent)")
            } else {
                dao.update(budget.copy(maxAmount = newMax))
                refresh(userId)
            }
        }
    }

    fun deleteBudget(id: Int, userId: Int) {
        launch {
            buildDb(getApplication()).budgetDao().deleteById(id)
            refresh(userId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
