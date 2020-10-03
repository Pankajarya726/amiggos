package com.tekzee.amiggos.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tekzee.amiggos.room.dao.ItemDao
import com.tekzee.amiggos.room.entity.Menu

@Database(entities = [Menu::class], version = 1, exportSchema = false)
public abstract class AmiggoRoomDatabase : RoomDatabase() {

   abstract fun itemDao(): ItemDao

   companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time. 
        @Volatile
        private var INSTANCE: AmiggoRoomDatabase? = null

        fun getDatabase(context: Context): AmiggoRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AmiggoRoomDatabase::class.java,
                        "amiggos_database"
                    ).build()
                INSTANCE = instance
                return instance
            }
        }
   }
}