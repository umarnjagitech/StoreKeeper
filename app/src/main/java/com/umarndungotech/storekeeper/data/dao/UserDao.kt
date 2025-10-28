package com.umarndungotech.storekeeper.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.umarndungotech.storekeeper.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}
