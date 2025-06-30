package com.ubaya.projectanmp.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SessionManager {
    private const val PREF_NAME   = "session_pref"
    private const val KEY_ID      = "user_id"
    private const val KEY_USER    = "username"

    fun login(ctx: Context, id: Int, username: String) =
        ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().apply {
            putInt(KEY_ID, id)
            putString(KEY_USER, username)
        }.apply()

    fun logout(ctx: Context) =
        ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
            .edit().clear().apply()

    fun userId(ctx: Context): Int =
        ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
            .getInt(KEY_ID, -1)

    fun username(ctx: Context): String? =
        ctx.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
            .getString(KEY_USER, null)

    fun isLoggedIn(ctx: Context): Boolean = userId(ctx) != -1
}