package com.ermilov.sf.add_ad.ui

import `in`.madapps.placesautocomplete.PlaceAPI
import `in`.madapps.placesautocomplete.adapter.PlacesAutoCompleteAdapter
import `in`.madapps.placesautocomplete.model.Place
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ermilov.sf.R
import com.ermilov.sf.util.FirebaseInstance
import com.ermilov.sf.util.Validate
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment__add__ad.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class Fragment_Add_Ad : Fragment() {

    companion object{
        val GALLERY_REQUEST_CODE = 56
        val validate = Validate()
        val user = Firebase.auth.currentUser
    }
    lateinit var navController: NavController
    var imageByte: ByteArray = ByteArray(0)
    private var adressAd: String = String()
    private val adressmap: HashMap<String, Any> = HashMap()
    var city: String = String()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment__add__ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val placesApi = PlaceAPI.Builder().apiKey("AIzaSyChjP5hsInhv3gwcpz5mKdSMBn6MxIegns").build(
                requireContext()
        )
        autoCompleteEditText.setAdapter(PlacesAutoCompleteAdapter(requireContext(), placesApi))
        autoCompleteEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val place = parent.getItemAtPosition(position) as Place
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val address = geocoder.getFromLocationName(place.description, 1)
                adressAd = address[0].getAddressLine(0).toString()
                city = address[0].locality
                autoCompleteEditText.setText(adressAd)
                view.hideKeyboard()
            }


        imageButton_choose_image?.setOnClickListener {
            chooseImagefromGallery()
        }

        buttonAddAdtoFirebase.setOnClickListener {
            val namead = editText_name_ad.text.toString()
            val descriptionad = editText_details_ad.text.toString()
            if (validate.validateAddAd(imageByte, adressAd, namead, descriptionad, requireContext())){
                progressBar_add_ad.visibility = ProgressBar.VISIBLE
                adressmap["NameAd"] = namead
                adressmap["Description"] = descriptionad
                adressmap["User"]  = user?.uid.toString()
                adressmap["Time"] = currentDate()
                adressmap["Address"] = adressAd
                uploadFirebase(imageByte)
                adressAd = ""
            }
        }
    }


    fun chooseImagefromGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
                Intent.createChooser(intent, "Please select..."),
                GALLERY_REQUEST_CODE
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE&&resultCode==Activity.RESULT_OK&&data!=null&&data.data!=null){
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data.data)
            imageButton_choose_image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, false))
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
            imageByte = baos.toByteArray()
        }
    }

    private fun uploadFirebase(byteArrayImage: ByteArray) {
        val user = Firebase.auth.currentUser
            FirebaseInstance.firebaseStorageImage.child(user!!.uid).child(imageByte.toString()).putBytes(byteArrayImage)
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener { uri ->
                            Log.i("imageUrl", uri.toString())
                            adressmap["ImageUrl"] = uri.toString()
                            addtoFirebaseDatabase(city).addOnSuccessListener {
                                addtoFirebaseDatabaseMyAd()
                                progressBar_add_ad.visibility = ProgressBar.INVISIBLE
                                Toast.makeText(context, R.string.ad_succefull_add, Toast.LENGTH_SHORT).show()
                                navController.navigate(R.id.action_addAd_fragment_to_account_fragment)
                            }.addOnFailureListener {
                                Toast.makeText(context, R.string.add_ad_error_toast, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

    }

    private fun addtoFirebaseDatabase(city: String): Task<Void> {
        return FirebaseInstance.firebaseFirestoreInstanse.collection("adresses")
                .document(city).collection(city).document().set(adressmap)
    }

    private fun addtoFirebaseDatabaseMyAd(): Task<Void> {
        return FirebaseInstance.firebaseFirestoreInstanse.collection("MyAd")
                .document(user?.uid.toString()).collection(user?.uid.toString()).document().set(adressmap)
    }

    private fun currentDate() : String{
        val date = Date()
        val newDate = Date(date.time)
        val dt = SimpleDateFormat("yyyy-MM-dd HH:mm",  Locale.getDefault())
        return dt.format(newDate)
    }

    private fun checkMaxAd(){

    }



    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}