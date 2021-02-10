package com.ermilov.sf.signIn.ui

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
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
        const val FINE_LOCATION_CODE = 174
        const val COARSE_LOCATION_CODE = 175
    }

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkforpermissionsLocation(android.Manifest.permission.ACCESS_FINE_LOCATION, FINE_LOCATION_CODE)
        navController = Navigation.findNavController(view)
        val user = Firebase.auth.currentUser
        if (user != null) {
            navController.navigate(R.id.action_singinFragment_to_Account_fragment)
        }



        textView_registration.setOnClickListener {
                navController.navigate(R.id.action_singinFragment_to_registrationFragment)
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

    }



    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


    private fun checkforpermissionsLocation(permission:String, requestcode: Int){
            when{
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED ->{ }
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED ->{
                     showDialogPermisssion(permission, requestcode)
                }
                else ->
                    requestPermissions(arrayOf(permission), requestcode)
            }

    }

    private fun showDialogPermisssion(permission:String, requestcode: Int){
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage(R.string.dialog_permission_message)
            setTitle(R.string.dialog_permission_title)
            setPositiveButton("Ok"){dialog, which ->
                requestPermissions(arrayOf(permission), requestcode)
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(requireContext(), R.string.toast_permission_denied, Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
    }




}