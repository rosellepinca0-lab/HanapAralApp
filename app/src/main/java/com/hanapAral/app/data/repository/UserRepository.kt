package com.hanapAral.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hanapAral.app.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun saveUser(user: User) {
        usersCollection.document(user.uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        val doc = usersCollection.document(uid).get().await()
        return doc.toObject(User::class.java)
    }

    suspend fun profileExists(uid: String): Boolean {
        val doc = usersCollection.document(uid).get().await()
        return doc.exists()
    }

    suspend fun updateFcmToken(uid: String, token: String) {
        usersCollection.document(uid).update("fcmToken", token).await()
    }
}