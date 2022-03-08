package com.example.dissertationandroid.data

import android.util.Log
import com.example.dissertationandroid.data.model.LoggedInUser
import com.mongodb.*
import java.io.IOException
import com.example.dissertationandroid.APIService
import com.google.gson.JsonObject
import org.json.JSONObject
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.BsonField
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        // TODO: Fix all of this pls
        var url = "https://us-east1-dissertation-339211.cloudfunctions.net/login-user"

        //Initialise retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .build()

        //Create service
        val service = retrofit.create(APIService::class.java)

        //Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", username)
        jsonObject.put("password", password)
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        var responseUser: String?= null
        //var response: Response<ResponseBody>?= null

        CoroutineScope(Dispatchers.IO).launch {
            var response1 = service.loginUser(requestBody)

            withContext(Dispatchers.Main) {
                if (response1.isSuccessful) {
                    println(response1.body())

                } else {
                    Log.e("RETROFIT_ERROR", response1!!.code().toString())
                }
                responseUser = response1.body()!!.string()
                //{"_id":{"$oid":"62275fe6f62f7bee2ccdfc6b"},"username":"test@test.com","password":"password","displayname":"TestUser","reminderSettings":{"notifications":true,"reminderTime":"20:00","frequency":"daily"},"avatar":"0"}
            }

            //responseUser = response1.body()!!
        }

        val returnObject = JSONObject(responseUser)

        //returnObject.put("username",responseUser.)
        val user = LoggedInUser(
            returnObject.getJSONObject("_id").getString("\$oid"),
            returnObject.getString("displayname"))
        //Result(success)
        return Result.Success(user)

    }

    fun logout() {
        // TODO: revoke authentication
    }
}