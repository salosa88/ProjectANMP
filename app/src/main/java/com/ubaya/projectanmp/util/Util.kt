package com.ubaya.projectanmp.util

import android.content.Context
import com.ubaya.projectanmp.model.AppDatabase

const val DB_NAME = "expense_db"

fun buildDb(context: Context): AppDatabase =
    AppDatabase.invoke(context)