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