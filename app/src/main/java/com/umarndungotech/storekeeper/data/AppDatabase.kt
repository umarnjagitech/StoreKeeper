package com.umarndungotech.storekeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umarndungotech.storekeeper.data.dao.ProductDao
import com.umarndungotech.storekeeper.data.dao.UserDao
import com.umarndungotech.storekeeper.data.model.Product
import com.umarndungotech.storekeeper.data.model.User

@Database(entities = [User::class, Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "storekeeper_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
