package com.ubaya.projectanmp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("""
        SELECT e.* FROM Expense e
        JOIN Budget b ON b.id = e.budgetId
        WHERE b.ownerId = :userId
        ORDER BY e.createdAt DESC
    """)
    fun getExpensesByUser(userId: Int): List<Expense>

    @Insert fun insert(e: Expense)

    @Delete
    fun delete(expense: Expense)

    @Query("DELETE FROM Expense WHERE id = :expenseId")
    fun deleteById(expenseId: Int)
}