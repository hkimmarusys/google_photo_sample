package com.example.google_photo_sample.auth

import android.util.Log
import com.google.gson.JsonParser
import okhttp3.*
import com.example.google_photo_sample.BuildConfig


object GoogleAuthClient {
    private const val TOKEN_URI = "https://oauth2.googleapis.com/token"
    private const val PHOTOS_API = "https://photoslibrary.googleapis.com/v1/mediaItems"

    suspend fun exchangeCodeForAccessToken(authCode: String): String {
        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("code", authCode)
            .add("client_id", BuildConfig.CLIENT_ID)
            .add("client_secret", BuildConfig.CLIENT_SECRET)
            .add("grant_type", "authorization_code")
            .add("redirect_uri", "https://googlephotosample.com/callback")
            .build()

        val request = Request.Builder()
            .url(TOKEN_URI)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()


        val json = JsonParser.parseString(responseBody).asJsonObject
        return json["access_token"].asString
    }

    suspend fun fetchPhotos(accessToken: String): List<String> {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(PHOTOS_API)
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        val photoUrls = mutableListOf<String>()

        responseBody?.let {
            val json = JsonParser.parseString(it).asJsonObject
            val items = json["mediaItems"]?.asJsonArray

            items?.forEach { item ->
                val obj = item.asJsonObject
                val baseUrl = obj["baseUrl"]?.asString
                if (baseUrl != null) {
                    photoUrls.add(baseUrl)
                }
            }
        }

        return photoUrls
    }
}
