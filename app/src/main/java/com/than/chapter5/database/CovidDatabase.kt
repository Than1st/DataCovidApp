package com.than.chapter5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.than.chapter5.dao.UserDao
import com.than.chapter5.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class CovidDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        private var INSTANCE: CovidDatabase? = null
        fun getInstance(context: Context): CovidDatabase?{
            if (INSTANCE == null){
                synchronized(CovidDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, CovidDatabase::class.java, "covidDatabase.db").build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}