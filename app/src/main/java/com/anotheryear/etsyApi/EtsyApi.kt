package com.anotheryear.etsyApi

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface EtsyApi {

    @GET("v2/listings/active?api_key=uaymewhj7nol96z4eny0b3id")
    fun fetchRecentActiveListings(): Call<EtsyResponse>

    @GET("v2//listings/{listing_id}?api_key=uaymewhj7nol96z4eny0b3id")
    fun fetchListing(@Path("listing_id") listing_id: Int): Call<EtsyResponse>

    @GET("v2//listings/{listing_id}/images?api_key=uaymewhj7nol96z4eny0b3id")
    fun fetchImageListings(@Path("listing_id") listing_id: Int): Call<EtsyImageResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}
