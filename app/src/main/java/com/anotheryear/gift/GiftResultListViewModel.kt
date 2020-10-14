package com.anotheryear.gift

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anotheryear.etsyApi.*
import org.w3c.dom.ls.LSException

private const val TAG = "GiftResultListViewModel"

class GiftResultListViewModel : ViewModel() {

    private val giftKeywordsLiveData = MutableLiveData<List<String>>()

    var giftListLiveData: LiveData<List<Listing>> = MutableLiveData<List<Listing>>()

    fun loadKeywords(keywords: List<String>) {
        Log.d(TAG, "loadKeywords() called")
        giftKeywordsLiveData.value = keywords
        Log.d(TAG, keywords.toString())
    }

    /**
     * Returns list of keywords
     */
    fun getKeywords(): List<String> {
        Log.d(TAG, "getKeywords() called")
        return giftKeywordsLiveData.value!!
    }

    fun getListings(budget: Array<Float>) {
        if (budget[0] != Float.MIN_VALUE && budget[1] != Float.MAX_VALUE) {

            giftListLiveData = EtsyGetter().fetchBudgetActiveListings(
                giftKeywordsLiveData.value!!,
                budget[1],
                budget[0],
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()
                        listingItems = listingItems.filterNot {
                            it.url.isBlank()
                        }
                        Log.d(TAG, "Response received - to getListings")
                        return listingItems
                    }
                })
        } else {
            giftListLiveData = EtsyGetter().fetchNonBudgetedActiveListings(giftKeywordsLiveData.value!!,
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()
                        listingItems = listingItems.filterNot {
                            it.url.isBlank()
                        }
                        Log.d(TAG, "Response received - to getListings")
                        return listingItems
                    }
                })
        }
    }
}
