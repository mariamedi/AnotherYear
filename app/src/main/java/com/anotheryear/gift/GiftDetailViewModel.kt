package com.anotheryear.gift

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.anotheryear.Birthday
import com.anotheryear.BirthdayRepository
import com.anotheryear.etsyApi.EtsyGetter
import com.anotheryear.etsyApi.Listing
import java.util.*

private const val TAG =  "GiftDetailVM"

class GiftDetailViewModel: ViewModel() {

    private val giftIdLiveData = MutableLiveData<Int>()

    val giftLiveData: LiveData<Listing> =
        Transformations.switchMap(giftIdLiveData) { giftId ->
            EtsyGetter().fetchListing(giftId)
    }

    fun loadGift(giftId: Int) {
        Log.d(TAG, "loadGift() called")
        giftIdLiveData.value = giftId
    }
}