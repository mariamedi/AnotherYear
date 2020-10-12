package com.anotheryear.etsyApi

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
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

    /**
     * Fetches the most recent active listings on Etsy.
     */
    fun fetchRecentActiveListings(): LiveData<List<Listing>> {
        val responseLiveData: MutableLiveData<List<Listing>> = MutableLiveData()
        val etsyRequest: Call<EtsyResponse> = etsyApi.fetchRecentActiveListings()

        etsyRequest.enqueue(object : Callback<EtsyResponse> {
            override fun onFailure(call: Call<EtsyResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch Recent Active Listings Etsy data", t)
            }
            override fun onResponse(call: Call<EtsyResponse>, response: Response<EtsyResponse>) {
                Log.d(TAG, "Response received")
                val etsyResponse: EtsyResponse? = response.body()
                var listingItems: List<Listing> = etsyResponse?.results
                    ?: mutableListOf()
                listingItems = listingItems.filterNot {
                    it.url?.isBlank()
                }
                responseLiveData.value = listingItems
            }
        })
        return responseLiveData
    }

    /**
     * Fetches the information of a particular listing with the Etsy assigned
     * listingId provided.
     */
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

    /**
     * Fetches the first image listing associated with a particular listing.
     */
    fun fetchImageListing(listingId: Int, callback: OnEtsyImageResponse) {
        val etsyRequest: Call<EtsyImageResponse> = etsyApi.fetchImageListings(listingId)

        etsyRequest.enqueue(object : Callback<EtsyImageResponse> {
            override fun onFailure(call: Call<EtsyImageResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch Image Listing Etsy data: " + listingId.toString(), t)
            }
            override fun onResponse(call: Call<EtsyImageResponse>, response: Response<EtsyImageResponse>) {
                callback.images(response.body())
            }
        })
    }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = etsyApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }
}