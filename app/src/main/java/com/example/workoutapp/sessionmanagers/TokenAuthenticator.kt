package com.example.workoutapp.sessionmanagers

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor (
    private val authorizationHeader : String,
    private val tokenManager: TokenManager,
    ) : Authenticator {

    init {
        Log.d("TokenAuthenticator", "I was initialised")
    }

    /*
    Called on 401 or 407 error codes. Returns null to indicate that the request should be given up on,
    and there should be no further retries.
     */
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.code() == 401) {
            // https://github.com/yschimke/okhttp/blob/f9171936cac4cfc607b622951e7e65a085781a6a/okhttp/src/main/kotlin/okhttp3/Authenticator.kt#L97-L111
            // Allows us to check if we've tried to fix this response before, we only want to try once.
            // There is a chain of responses linked to this response, any new requests will have a different chain, so checking
            // if the response had a prior response, because this new response also had 401, will indicate that
            // we shouldn't try again.
            if ((response.priorResponse()) != null) {
                Log.d("TokenAuthenticator", "Giving up trying to add authentication to this request")
                return null // Returning null will not retry the response, so will return the 401 of the retried request
            }
            Log.d("TokenAuthenticator", "Attempting to regenerate a token")
            // Perform token refresh and obtain a new token
            // The endpoints called here SHOULD NOT return 401, else the code will run infinitely.
            val newAccessToken = runBlocking { tokenManager.getNewAccessToken() }

            Log.d("TokenAuthenticator", "New Access Token: $newAccessToken")

            // Retry the request with the new token
            return response.request().newBuilder()
                .header(authorizationHeader, "Bearer $newAccessToken")
                .build()
        }
        return null // If not a 401 error, return null to indicate no retry
    }

}