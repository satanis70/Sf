package com.ermilov.sf.util

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseInstance {
    companion object{
        val firebaseFirestoreInstanse = Firebase.firestore
        val firebaseStorageImage = FirebaseStorage.getInstance().reference.child("images/")
    }
}