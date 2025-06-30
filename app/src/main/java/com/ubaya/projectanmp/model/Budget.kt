package com.ubaya.projectanmp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("ownerId")]
)
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerId: Int,                 // FK → User.id
    val name: String,
    val maxAmount: Int
)