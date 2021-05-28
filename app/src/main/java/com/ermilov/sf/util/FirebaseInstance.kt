package com.ermilov.sf.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

object FirebaseInstance {

     fun firebaseFirestoreInstanse(): FirebaseFirestore {
        return Firebase.firestore
    }

     val firebaseStorageImage = FirebaseStorage.getInstance().reference.child("images/")
     val firebaseUser: FirebaseAuth = Firebase.auth
}

