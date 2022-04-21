package com.than.chapter5.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.than.chapter5.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun loginUser(username: String, password: String): Boolean
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun getUser(username: String, password: String): User
    @Insert(onConflict = REPLACE)
    fun insertUser(user: User):Long
    @Update
    fun updateUser(user: User): Int
}