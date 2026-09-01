package com.hamradio.logger.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hamradio.logger.data.db.dao.ContactDao
import com.hamradio.logger.data.db.entity.RadioContact

@Database(
    entities = [RadioContact::class],
    version = 1,
    exportSchema = false
)
abstract class HamRadioDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var instance: HamRadioDatabase? = null

        fun getInstance(context: Context): HamRadioDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    HamRadioDatabase::class.java,
                    "ham_radio_db"
                ).build().also { instance = it }
            }
        }
    }
}
