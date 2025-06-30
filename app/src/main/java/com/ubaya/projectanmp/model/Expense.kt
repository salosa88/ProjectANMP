package com.ubaya.projectanmp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Budget::class,
        parentColumns = ["id"],
        childColumns = ["budgetId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("budgetId")]
)
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val budgetId: Int,                // FK → Budget.id
    val amount: Int,
    val description: String,
    val createdAt: Long = System.currentTimeMillis() / 1000
)