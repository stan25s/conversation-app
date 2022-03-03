package com.example.dissertationandroid.data

import com.example.dissertationandroid.data.model.LoggedInUser
import com.mongodb.*
import java.io.IOException
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.BsonField
import com.mongodb.client.model.Filters.eq

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        var connectionString: ConnectionString? = null
        //Connect to MongoDB Client
//        connectionString = ConnectionString("mongodb+srv://admin:admin@cluster0.exxc7.mongodb.net/Main?retryWrites=true&w=majority")
//        val settings: MongoClientSettings = MongoClientSettings.builder()
//            .applyConnectionString(connectionString)
//            .build();
//        val mongoClient = MongoClients.create(settings)
        try {
            connectionString = ConnectionString("mongodb+srv://admin:admin@cluster0.exxc7.mongodb.net/Main?retryWrites=true&w=majority")
            val settings: MongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
            val mongoClient = MongoClients.create(settings)
            // TODO: handle loggedInUser authentication
            // TODO: Replace MongoDB calls with google cloud functions
            //Set Database
            val db: MongoDatabase = mongoClient.getDatabase("Main")

            //Set Collection
            val collection = db.getCollection("Users")

            //Check if user with given username & password exists
            //var result = collection.find(eq("username", username)).first()
            val criteria = BasicDBObject()
            criteria.append("username", username)
            criteria.append("password", password)
            val result = collection.find(criteria).first()

            //mongoClient!!.close()
            //Check if exists and return user data
            if (result != null) {
                val foundUser = LoggedInUser(username, result["displayName"].toString())
                return Result.Success(foundUser)
            } else {
                return Result.Error(IOException("Error logging in: User Not Found"))
            }
        } catch (e: Throwable) {
            println(e.cause)
            println(e.message)
            print(e.stackTrace)

            return Result.Error(MongoException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}