package com.ubaya.projectanmp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun register(user: User)

    @Query("SELECT * FROM User WHERE username = :u AND password = :pHash")
    fun login(u: String, pHash: String): User?

    @Query("""
        UPDATE User SET password = :newHash
        WHERE id = :userId AND password = :oldHash
    """)

   fun changePassword(userId: Int, oldHash: String, newHash: String): Int
}