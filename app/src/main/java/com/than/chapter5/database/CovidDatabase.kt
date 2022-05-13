package com.than.chapter5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.than.chapter5.dao.FavoriteDao
import com.than.chapter5.dao.UserDao
import com.than.chapter5.model.Favorite
import com.than.chapter5.model.User

@Database(entities = [User::class, Favorite::class], version = 4)
abstract class CovidDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    companion object{
        @Volatile
        private var INSTANCE: CovidDatabase? = null
        fun getInstance(context: Context): CovidDatabase?{
            if (INSTANCE == null){
                synchronized(CovidDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, CovidDatabase::class.java, "covidDatabase.db").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}