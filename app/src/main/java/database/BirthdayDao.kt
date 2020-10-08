package database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anotheryear.Birthday
import java.util.*

/**
 * Dao interface for the birthday database
 */
@Dao
interface BirthdayDao {

    /**
     * Gets all the data from the db and sorts it by the latest birthday
     */
    @Query("SELECT * FROM birthday ORDER BY birthdate DESC")
    fun getBirthdays(): LiveData<List<Birthday>>

    /**
     * Gets the birthday from the db that is associated with the UUID
     */
    @Query("SELECT * FROM birthday WHERE id=(:id)")
    fun getBirthday(id: UUID): LiveData<Birthday?>

    /**
     * Updates a birthday in the db based on its UUID
     */
    @Update
    fun updateBirthday(birthday: Birthday)

    /**
     * Adds a new birthday to the db
     */
    @Insert
    fun addBirthday(birthday: Birthday)

    /**
     * Delete a birthday in the db based on its UUID
     */
    @Query("DELETE FROM birthday WHERE id=(:id)")
    fun deleteBirthday(id: UUID)
}