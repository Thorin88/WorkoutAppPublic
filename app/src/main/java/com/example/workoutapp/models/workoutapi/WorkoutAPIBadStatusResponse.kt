package com.example.workoutapp.models.workoutapi

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

/**
 * Error responses from the API always come in the same form, so this class helps access messages
 * sent alongside bad error codes.
 */
data class WorkoutAPIBadStatusResponse(
    val message: String,
) {

    companion object {
        inline fun <reified E> getErrorBody(response: Response<E>) : WorkoutAPIBadStatusResponse? {

            // https://stackoverflow.com/questions/32519618/retrofit-2-0-how-to-get-deserialised-error-response-body
            val gson = Gson()
            val type = object : TypeToken<WorkoutAPIBadStatusResponse>() {}.type
            val errorResponse: WorkoutAPIBadStatusResponse? = gson.fromJson(response.errorBody()?.charStream(), type)

            return errorResponse

        }
    }

}