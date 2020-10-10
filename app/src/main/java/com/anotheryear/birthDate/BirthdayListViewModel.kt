package com.anotheryear.birthDate

import androidx.lifecycle.ViewModel
import com.anotheryear.Birthday
import com.anotheryear.BirthdayRepository

import java.util.*

/**
 * BirthdayListViewModel
 */
class BirthdayListViewModel : ViewModel() {

    private val birthdayRepository = BirthdayRepository.get()

/*
    // un-comment this to add 150 random entries to the db
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') // list fo chars fro name generator

    //Create 150 birthdays on initialization
    init {
        for (i in 1 until 151) {

            // Generate a seven character name for firstName
            val firstName = {
                (1..7)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
            }

            // Generate a seven character name for lastName
            val lastName = {
                (1..7)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
            }

            // Create a birthday object with the above variables and pass in the current calendar object
            val birthday = Birthday(
                UUID.randomUUID(), run(firstName), run(lastName),
                 Calendar.getInstance().time
            )
            birthdayRepository.addBirthday(birthday)
        }
    }
*/

    private val birthdayListLiveData = birthdayRepository.getBirthdays()

    /**
     * Getter for the list of birthdays
     */
    val birthdayList
        get() = birthdayListLiveData
}