package com.ubaya.projectanmp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM Budget WHERE ownerId = :userId")
    fun getBudgets(userId: Int): List<Budget>

    @Transaction
    @Query("SELECT * FROM Budget WHERE ownerId = :userId")
    fun getBudgetWithExpenses(userId: Int): List<BudgetWithExpenses>

    @Insert
    fun insert(b: Budget)

    @Update
    fun update(b: Budget)

    @Query("SELECT IFNULL(SUM(amount),0) FROM Expense WHERE budgetId = :budgetId")
    fun sumExpense(budgetId: Int): Int

    @Query("DELETE FROM Budget WHERE id = :budgetId")
    fun deleteById(budgetId: Int)
}