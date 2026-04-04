package com.hanapAral.app.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hanapAral.app.data.model.Announcement
import com.hanapAral.app.data.model.ChatMessage
import com.hanapAral.app.data.model.StudyGroup
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class GroupRepository {

    private val db = FirebaseFirestore.getInstance()
    private val groupsCollection = db.collection("study_groups")

    suspend fun createGroup(group: StudyGroup): String {
        val docRef = groupsCollection.document()
        val groupWithId = group.copy(id = docRef.id)
        docRef.set(groupWithId).await()
        return docRef.id
    }

    fun getGroupsFlow(): Flow<List<StudyGroup>> = callbackFlow {
        val listener = groupsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val groups = snapshot?.documents?.mapNotNull { it.toObject(StudyGroup::class.java) } ?: emptyList()
                trySend(groups)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getAllGroups(): List<StudyGroup> {
        val snapshot = groupsCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(StudyGroup::class.java) }
    }

    suspend fun getGroupById(groupId: String): StudyGroup? {
        val doc = groupsCollection.document(groupId).get().await()
        return doc.toObject(StudyGroup::class.java)
    }

    suspend fun joinGroup(groupId: String, userId: String) {
        groupsCollection.document(groupId)
            .update("members", FieldValue.arrayUnion(userId)).await()
    }

    suspend fun leaveGroup(groupId: String, userId: String) {
        groupsCollection.document(groupId)
            .update("members", FieldValue.arrayRemove(userId)).await()
    }