package com.anotheryear

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import database.BirthdayDatabase
import java.util.*
import java.util.concurrent.Executors

private const val TAG = "BirthdayRepository"
private const val DATABASE_NAME = "birthday-database"

/**
 * Repository class for Birthday that builds the database and holds
 * Writing and Reading functionality. This class is a Singleton
 */
class BirthdayRepository private constructor(context: Context) {

    // build the database to store birthday data
    private val database: BirthdayDatabase = Room.databaseBuilder(
        context.applicationContext,
        BirthdayDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val birthdayDao = database.birthdayDao()
    private val executor = Executors.newSingleThreadExecutor()

    /**
     * Gets a LiveData list of birthdays from the DB
     */
    fun getBirthdays(): LiveData<List<Birthday>> = birthdayDao.getBirthdays()

    /**
     * Gets a LiveData birthday from the DB based on the UUID
     */
    fun getBirthday(id: UUID): LiveData<Birthday?> = birthdayDao.getBirthday(id)

    /**
     * Takes a birthday and updates it in the database wherever the UUID exists
     */
    fun updateBirthday(birthday: Birthday) {
        Log.d(TAG, "updateBirthday() called")
        executor.execute {
            birthdayDao.updateBirthday(birthday)
        }
    }

    /**
     * Takes a birthday and adds it to the database
     */
    fun addBirthday(birthday: Birthday) {
        Log.d(TAG, "addBirthday() called")
        executor.execute {
            birthdayDao.addBirthday(birthday)
        }
    }

    /**
     * Deletes a birthday from the database based on the given UUID
     */
    fun deleteBirthday(id: UUID) {
        Log.d(TAG, "deleteBirthday() called")
        executor.execute {
            birthdayDao.deleteBirthday(id)
        }
    }


    /**
     * Companion object that creates the Singleton Instance and holds its getter function
     */
    companion object {
        private var INSTANCE: BirthdayRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = BirthdayRepository(context)
            }
        }

        fun get(): BirthdayRepository {
            return INSTANCE
                ?: throw IllegalStateException("BirthdayRepository must be initialized")
        }
    }
}
