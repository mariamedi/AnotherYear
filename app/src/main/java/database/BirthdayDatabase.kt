package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anotheryear.Birthday

/**
 * Database class for Birthdays that calls the Dao
 */
@Database(entities = [ Birthday::class ], version=1, exportSchema = false)
@TypeConverters(BirthdayTypeConverters::class)
abstract class BirthdayDatabase : RoomDatabase() {

    abstract fun birthdayDao(): BirthdayDao
}