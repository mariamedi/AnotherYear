package com.anotheryear.etsyApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EtsyApi {

    @GET("v2/listings/active?api_key=uaymewhj7nol96z4eny0b3id")
    fun fetchRecentActiveListings(): Call<EtsyResponse>

    @GET("v2//listings/{listing_id}?api_key=uaymewhj7nol96z4eny0b3id")
    fun fetchListing(@Path("listing_id") listing_id: Int): Call<EtsyResponse>
}
