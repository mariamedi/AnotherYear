package com.anotheryear.gift

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anotheryear.etsyApi.*

private const val TAG = "GiftResultListViewModel"

class GiftResultListViewModel : ViewModel() {

    val giftKeywordsLiveData = MutableLiveData<List<String>>()
    var giftListLiveData: LiveData<List<Listing>> = MutableLiveData<List<Listing>>()

    /**
     * Loads a list of keywords
     */
    fun loadKeywords(keywords: List<String>) {
        giftKeywordsLiveData.value = keywords
    }

    /**
     * Loads an array of parcelable keyword
     */
    fun loadKeywords(keywords: ArrayList<Keyword>) {

        val listKeywords = mutableListOf<String>()

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

    /**
     * Depending on the budget and tags provided, the fetch method is selected
     */
    fun getListings(budget: Array<Float>) {
        // Check contains at least one valid keyword
        Log.d(TAG, getKeywords().toString())
        Log.d(TAG, getKeywords().isNotEmpty().toString())

        val containsKeyword = getKeywords().isNotEmpty()

        // tags + budget
        if (containsKeyword && budget[0] != Float.MIN_VALUE && budget[1] != Float.MAX_VALUE) {
            giftListLiveData = EtsyGetter().fetchBudgetActiveListings(
                giftKeywordsLiveData.value!!,
                budget[1],
                budget[0],
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()

                        try {
                            listingItems = listingItems.filterNot {
                                it.url.isBlank()
                            }
                        }catch (e: Exception){
                            return listingItems
                        }
                        Log.d(TAG, "Response received - to getListings 1 ")
                        return listingItems
                    }
                })
        } // tags
        else if(containsKeyword){
            giftListLiveData = EtsyGetter().fetchNonBudgetedActiveListings(giftKeywordsLiveData.value!!,
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()
                        try {
                            listingItems = listingItems.filterNot {
                                it.url.isBlank()
                            }
                        }catch (e: Exception){
                            return listingItems
                        }
                        Log.d(TAG, "Response received - to getListings 2 ")
                        return listingItems
                    }
                })
        } // budget
        else if (budget[0] != Float.MIN_VALUE && budget[1] != Float.MAX_VALUE){
            giftListLiveData = EtsyGetter().fetchBudgetActiveListings(
                budget[1],
                budget[0],
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()
                        try {
                            listingItems = listingItems.filterNot {
                                it.url.isBlank()
                            }
                        }catch (e: Exception){
                            return listingItems
                        }
                        Log.d(TAG, "Response received - to getListings 3 ")
                        return listingItems
                    }
                })
        } // no tags | budget
        else {
            giftListLiveData = EtsyGetter().fetchRecentActiveListings(
                object : OnEtsyResponse {
                    override fun results(results: EtsyResponse?): List<Listing> {
                        var listingItems: List<Listing> = results?.results
                            ?: mutableListOf()
                        try {
                            listingItems = listingItems.filterNot {
                                it.url.isBlank()
                            }
                        }catch (e: Exception){
                            return listingItems
                        }
                        Log.d(TAG, "Response received - to getListings 4 ")
                        return listingItems
                    }
                })
        }
    }
}
