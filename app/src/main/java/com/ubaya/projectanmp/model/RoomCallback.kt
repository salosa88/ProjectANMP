package com.ubaya.projectanmp.model

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class RoomCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        db.execSQL("""
            INSERT INTO User (id, username, firstName, lastName, password)
            VALUES (1, 'demo', 'Demo', 'User',
                    '039f741daa6eeaad47a29bf4575d6b7e4363984b6fedce96a319215c1de7e0d1')
        """.trimIndent())   // hash SHA-256 dari "demo123"

        db.execSQL("""
            INSERT INTO Budget (id, ownerId, name, maxAmount)
            VALUES
              (1, 1, 'Makanan', 1000000),
              (2, 1, 'Transport', 500000)
        """.trimIndent())

        db.execSQL("""
            INSERT INTO Expense (id, budgetId, amount, description, createdAt)
            VALUES
              (1, 1, 25000, 'Sarapan', strftime('%s','now')),
              (2, 2, 15000, 'Naik bus', strftime('%s','now'))
        """.trimIndent())
    }
}
