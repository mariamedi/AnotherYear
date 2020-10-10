package com.anotheryear.gift

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anotheryear.etsyApi.EtsyGetter
import com.anotheryear.etsyApi.Listing

private const val TAG =  "GiftResultListViewModel"
class GiftResultListViewModel : ViewModel() {

    private val giftKeywordsLiveData = MutableLiveData<Array<String>>()
    val giftListLiveData: LiveData<List<Listing>> = EtsyGetter().fetchRecentActiveListings()

    fun loadKeywords(keywords: Array<String>) {
        Log.d(TAG, "loadKeywords() called")
        giftKeywordsLiveData.value = keywords
    }
}
