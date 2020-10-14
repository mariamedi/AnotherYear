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
        giftKeywordsLiveData.value = keywords
    }

    fun loadKeywords(keywords: ArrayList<Keyword>) {

        var listKeywords = mutableListOf<String>()

        for(k in keywords){
            listKeywords.add(k.keyword!!)
        }

        giftKeywordsLiveData.value = listKeywords
    }

    /**
     * Returns list of keywords
     */
    fun getKeywords(): List<String> {
        return giftKeywordsLiveData.value!!.filter { k: String? -> k != "" }
    }

    fun getListings(budget: Array<Float>) {
        // Check is contains at least one valid keyword
        Log.d(TAG, getKeywords().toString())
        Log.d(TAG, getKeywords().isNotEmpty().toString())

        var containsKeyword = getKeywords().isNotEmpty()

        if (containsKeyword && budget[0] != Float.MIN_VALUE && budget[1] != Float.MAX_VALUE) {
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
                        Log.d(TAG, "Response received - to getListings 1 ")
                        return listingItems
                    }
                })
        } else if(containsKeyword){
            giftListLiveData = EtsyGetter().fetchNonBudgetedActiveListings(giftKeywordsLiveData.value!!,
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()
                        listingItems = listingItems.filterNot {
                            it.url.isBlank()
                        }
                        Log.d(TAG, "Response received - to getListings 2 ")
                        return listingItems
                    }
                })
        }
        else if (budget[0] != Float.MIN_VALUE && budget[1] != Float.MAX_VALUE){
            giftListLiveData = EtsyGetter().fetchBudgetActiveListings(
                budget[1],
                budget[0],
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()
                        listingItems = listingItems.filterNot {
                            it.url.isBlank()
                        }
                        Log.d(TAG, "Response received - to getListings 3 ")
                        return listingItems
                    }
                })
        }
        else {
            giftListLiveData = EtsyGetter().fetchRecentActiveListings(
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()
                        listingItems = listingItems.filterNot {
                            it.url.isBlank()
                        }
                        Log.d(TAG, "Response received - to getListings 4 ")
                        return listingItems
                    }
                })
        }
    }
}
