package com.anotheryear.gift

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.anotheryear.etsyApi.EtsyGetter
import com.anotheryear.etsyApi.Listing

private const val TAG =  "GiftDetailVM"

class GiftDetailViewModel: ViewModel() {

    private val giftIdLiveData = MutableLiveData<Int>()
    lateinit var bitmap:Bitmap

    val giftLiveData: LiveData<Listing> =
        Transformations.switchMap(giftIdLiveData) { giftId ->
            EtsyGetter().fetchListing(giftId)
    }

    /**
     * Load gift of interest
     */
    fun loadGift(giftId: Int) {
        Log.d(TAG, "loadGift() called")
        giftIdLiveData.value = giftId
    }

    /**
     * Load image passed in with gift
     */
    fun loadImage(image: Bitmap) {
        Log.d(TAG, "loadImage() called")
        bitmap = image
    }
}