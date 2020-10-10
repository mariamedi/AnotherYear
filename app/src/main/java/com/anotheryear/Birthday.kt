package com.anotheryear

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * Data class for the Birthday object that holds the first and last name of the person, the date of their birthday and a UUID
 */
@Entity
data class Birthday (@PrimaryKey val id: UUID = UUID.randomUUID(),
                     var firstName: String = "", var lastName: String = "", var birthdate: Date = Date()) : Serializable
