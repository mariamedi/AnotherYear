package com.anotheryear.etsyApi

interface OnEtsyResponse {
    fun results(results: EtsyResponse?):List<Listing>
}