package com.anotheryear.birthDate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.anotheryear.Birthday
import com.anotheryear.BirthdayRepository
import java.util.*

private const val TAG =  "BirthdayDetailVM"

/**
 * ViewModel class that grabs LiveData from the birthday database and also saves new data to it
 */
class BirthdayDetailViewModel: ViewModel() {

    private val birthdayRepository = BirthdayRepository.get()
    private val birthdayIdLiveData = MutableLiveData<UUID>()
    var birthdayLiveData: LiveData<Birthday?> = Transformations.switchMap(birthdayIdLiveData) {
            birthdayId -> birthdayRepository.getBirthday(birthdayId)
    }

    /**
     * Function to load a birthday from the database based on the inputted UUID
     */
    fun loadBirthday(birthdayID: UUID) {
        Log.d(TAG, "loadBirthday() called")
        birthdayIdLiveData.value = birthdayID
    }

    /**
     * Function that saves the inputted birthday to the database
     */
    fun saveBirthday(birthday: Birthday) {
        Log.d(TAG, "saveBirthday() called")
        if(birthdayLiveData.value == null){
            birthdayRepository.addBirthday(birthday)
        } else {
            birthdayRepository.updateBirthday(birthday)
        }
    }

    /**
     * Function that deletes a birthday based on the inputted UUID
     */
    fun deleteBirthday(birthdayID: UUID) {
        Log.d(TAG, "deleteBirthday() called")
        birthdayRepository.deleteBirthday(birthdayID)
    }
}