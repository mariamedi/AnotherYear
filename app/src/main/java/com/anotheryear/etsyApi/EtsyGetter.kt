package com.anotheryear.etsyApi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "EtsyGetter"

class EtsyGetter {
    private val etsyApi: EtsyApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.etsy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        etsyApi = retrofit.create(EtsyApi::class.java)
    }

    fun fetchRecentActiveListings(): LiveData<List<Listing>> {
        val responseLiveData: MutableLiveData<List<Listing>> = MutableLiveData()
        val etsyRequest: Call<EtsyResponse> = etsyApi.fetchRecentActiveListings()

        etsyRequest.enqueue(object : Callback<EtsyResponse> {
            override fun onFailure(call: Call<EtsyResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch Etsy data", t)
            }
            override fun onResponse(
                call: Call<EtsyResponse>,
                response: Response<EtsyResponse>
            ) {
                Log.d(TAG, "Response received")
                val etsyResponse: EtsyResponse? = response.body()
                var listingItems: List<Listing> = etsyResponse?.results
                    ?: mutableListOf()
                listingItems = listingItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = listingItems
            }
        })
        return responseLiveData
    }

    fun fetchListing(listingId: Int): LiveData<Listing> {
        val responseLiveData: MutableLiveData<Listing> = MutableLiveData()
        val etsyRequest: Call<EtsyResponse> = etsyApi.fetchListing(listingId)

        etsyRequest.enqueue(object : Callback<EtsyResponse> {
            override fun onFailure(call: Call<EtsyResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch Etsy data", t)
            }
            override fun onResponse(
                call: Call<EtsyResponse>,
                response: Response<EtsyResponse>
            ) {
                Log.d(TAG, "Response received")
                val etsyResponse: EtsyResponse? = response.body()
                var listingItems: List<Listing> = etsyResponse?.results
                    ?: mutableListOf()
                listingItems = listingItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = listingItems[0]
            }
        })
        return responseLiveData
    }
}