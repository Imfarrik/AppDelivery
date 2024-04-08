package com.ccinc.api

import com.ccinc.api.model.CategoriesResponse
import com.ccinc.api.model.ProductsResponse
import com.ccinc.api.model.TagsResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET


interface FoodApi {

    @GET("Categories.json")
    suspend fun getCategories(): Result<List<CategoriesResponse>>

    @GET("Tags.json")
    suspend fun getTags(): Result<List<TagsResponse>>

    @GET("Products.json")
    suspend fun getProducts(): Result<List<ProductsResponse>>

}

fun foodApi(
    baseUrl: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json,
): FoodApi {
    return retrofit(
        baseUrl = baseUrl,
        okHttpClient = okHttpClient,
        json = json
    ).create()
}

private fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json,
): Retrofit {

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .run { if (okHttpClient != null) client(okHttpClient) else this }
        .build()
}