package com.example.workoutapp.sessionmanagers

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor (
    private val authorizationHeader : String,
    private val tokenManager: TokenManager,
) : Interceptor {

    init {
        Log.d("TokenInterceptor", "I was initialised")
    }

    /*
    The token generation code can return null to indicate that this function should not try to add
    a token to the request, such as in the case where a token couldn't be found or generated. This will
    then result in a 401 response from the API, where the authenticator will try to resolve the issue.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // Code currency attempts to add the token to ALL requests
        // if (request.headers("Authorization").isEmpty()) {

        // Since this is a suspend function, tell kotlin to run this on the current thread instead
        // of having to require us spawning a coroutine for the code to run in.
        val token = runBlocking { tokenManager.getCurrentAccessToken() }

        // Log.d("TokenInterceptor", "Token: $token")

        if (token != null) {
            Log.d("TokenInterceptor", "Adding a found token to a request")
            request = request.newBuilder()
                .header(authorizationHeader, "Bearer $token")
                .build()
        }

        return chain.proceed(request)
    }

}