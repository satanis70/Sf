package com.ermilov.sf.signIn.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ermilov.sf.R
import com.ermilov.sf.util.Validate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_in.*


class Fragment_sign_in : Fragment() {

    companion object{
        val validate = Validate()
        private lateinit var auth: FirebaseAuth
    }
    var buttonCount = 0
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val user = Firebase.auth.currentUser
        if (user != null) {
            navController.navigate(R.id.action_singinFragment_to_Account_fragment)
        }



        textView_registration.setOnClickListener {
                navController.navigate(R.id.action_singinFragment_to_choosecityfragment)
        }

        button_sign_in.setOnClickListener {
            progressBar_sign_in.visibility = ProgressBar.VISIBLE
            val editLogin = editTextLoginSignIn.text.toString()
            val editPassword = editTextPasswordSignIn.text.toString()
            auth = Firebase.auth
            if(validate.validateSignIn(editLogin, editPassword, requireContext())){
                auth.signInWithEmailAndPassword(editLogin, editPassword)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                view.hideKeyboard()
                                Toast.makeText(context, "Вошел", Toast.LENGTH_SHORT).show()
                                progressBar_sign_in.visibility = ProgressBar.INVISIBLE
                                navController.navigate(R.id.action_singinFragment_to_Account_fragment)

                            }
                        }.addOnFailureListener {
                        if (it.message=="There is no user record corresponding to this identifier. The user may have been deleted."){
                            Toast.makeText(
                                context,
                                R.string.error_sign_in_email_not_found,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (it.message=="The password is invalid or the user does not have a password."){
                                Toast.makeText(
                                    context,
                                    R.string.error_sign_in_password,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    }
            } else {
                progressBar_sign_in.visibility = ProgressBar.INVISIBLE
            }
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (buttonCount >= 1) {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                buttonCount = 0
            } else {
                Toast.makeText(requireContext(), R.string.exitApp, Toast.LENGTH_SHORT).show()
                buttonCount++
            }

        }

    }



    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }



}