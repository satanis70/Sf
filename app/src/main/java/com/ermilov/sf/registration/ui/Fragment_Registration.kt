package com.ermilov.sf.registration.ui

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ermilov.sf.R
import com.ermilov.sf.util.Validate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_registration.*
import java.util.jar.Manifest


class Fragment_Registration : Fragment() {
    
    companion object{
        val validate = Validate()
        private lateinit var auth: FirebaseAuth

    }
    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        auth = Firebase.auth
        button_registration.setOnClickListener { 
            val editEmail = editTextEmailRegistration.text.toString()
            val editPassword = editTextPassRegistration.text.toString()
            val editPassConfirm = editTextPassRegistrationConfirm.text.toString()
            val editUserName = editTextUserName.text.toString()
            validate.validateRegistration(editEmail, editPassword, editPassConfirm, editUserName, requireContext())

            if (validate.validateRegistration(editEmail, editPassword, editPassConfirm, editUserName, requireContext())){
                auth.createUserWithEmailAndPassword(editEmail, editPassword).addOnCompleteListener{
                    if (it.isSuccessful){
                        firebaseDatabaseAddNametoId(editUserName)
                        Toast.makeText(context, R.string.registration_successfull, Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.action_fragment_Registration_to_fragment_account)
                    }
                }.addOnFailureListener {
                    if (it.message == "The email address is already in use by another account."){
                        Toast.makeText(context, R.string.email_use_already, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.registration_error, Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
    }

    private fun firebaseDatabaseAddNametoId(username: String){
        FirebaseDatabase.getInstance().getReference("Users").child(auth.uid!!).setValue(username)
    }



}
