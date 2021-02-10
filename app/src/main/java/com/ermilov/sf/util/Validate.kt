package com.ermilov.sf.util

import android.content.ClipDescription
import android.content.Context
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.ermilov.sf.R


class Validate {

    fun validateSignIn(userEmail: String, userPassword: String, context: Context) : Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()&&userPassword.length>=6){
            return true
        } else if(userEmail.isEmpty()){
            Toast.makeText(context, R.string.email_empty, Toast.LENGTH_SHORT).show()
            return false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(context, R.string.email_not_valid, Toast.LENGTH_SHORT).show()
            return false
        } else if (userPassword.isEmpty()){
            Toast.makeText(context, R.string.password_empty, Toast.LENGTH_SHORT).show()
        }
        else if (userPassword.length<6){
            Toast.makeText(context, R.string.password_not_valid, Toast.LENGTH_SHORT).show()
            return false
        }

        return false
    }

    fun validateRegistration(userEmail: String, userPassword: String, confirmPass: String, userName: String, context: Context):Boolean{
        if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()&&userPassword.length>=6
            &&userPassword==confirmPass&&userName.isNotEmpty()){
            return true
        } else if(userEmail.isEmpty()){
            Toast.makeText(context, R.string.email_empty, Toast.LENGTH_SHORT).show()
            return false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(context, R.string.email_not_valid, Toast.LENGTH_SHORT).show()
            return false
        } else if (userPassword.isEmpty()){
            Toast.makeText(context, R.string.password_empty, Toast.LENGTH_SHORT).show()
        }
        else if (userPassword.length<6){
            Toast.makeText(context, R.string.password_not_valid, Toast.LENGTH_SHORT).show()
            return false
        } else if (confirmPass.isEmpty()){
            Toast.makeText(context, R.string.confirm_pass_empty, Toast.LENGTH_SHORT).show()
            return false
        } else if(userPassword!=confirmPass){
            Toast.makeText(context, R.string.confirm_dont_match_pass, Toast.LENGTH_SHORT).show()
            return false
        } else if (userName.isEmpty()) {
            Toast.makeText(context, R.string.user_name_isEmpty, Toast.LENGTH_SHORT).show()
            return false
        }

        return false
    }

    fun validateAddAd(imageByte: ByteArray, adressAd: String, nameAd: String, descriptionAd: String, context: Context): Boolean {
        if (imageByte.isNotEmpty() && adressAd.isNotEmpty() && nameAd.isNotEmpty() && descriptionAd.isNotEmpty()) {
            return true
        } else if (nameAd.isEmpty()) {
            Toast.makeText(context, R.string.name_ad_toast, Toast.LENGTH_SHORT).show()
            return false
        } else if (descriptionAd.isEmpty()) {
            Toast.makeText(context, R.string.description_ad_toast, Toast.LENGTH_SHORT).show()
            return false
        } else if (adressAd.isEmpty()) {
            Toast.makeText(context, R.string.address_ad_toast, Toast.LENGTH_SHORT).show()
            return false
        } else if(imageByte.isEmpty()){
            Toast.makeText(context, R.string.choose_image_toast, Toast.LENGTH_SHORT).show()
            return false
        }

        return false
    }
}
