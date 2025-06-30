package com.ubaya.projectanmp.model

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithExpenses(
    @Embedded val budget: Budget,
    @Relation(
        parentColumn = "id",
        entityColumn = "budgetId"
    )
    val expenses: List<Expense>
) {
    val spent: Int get() = expenses.sumOf { it.amount }
    val remaining: Int get() = budget.maxAmount - spent
}