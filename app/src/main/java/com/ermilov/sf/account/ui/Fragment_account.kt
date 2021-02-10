package com.ermilov.sf.account.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.ermilov.sf.R
import com.ermilov.sf.account.model.AdModel
import com.ermilov.sf.account.model.RoomModelCity
import com.ermilov.sf.account.recyclerAd.AllAdRecycler
import com.ermilov.sf.account.room.CityDatabase
import com.ermilov.sf.util.FirebaseInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat.getSystemService

class Fragment_account : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var navController: NavController
    lateinit var roomDb: CityDatabase
    var alladList = ArrayList<AdModel>()
    var cityfromDb: String = String()
    lateinit var locationManager: LocationManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        roomDb = Room.databaseBuilder(requireContext(), CityDatabase::class.java, "city-database").build()
        getLastKnownLocation()
        roomDbGetCity()
        floatingActionButton_Add_ad.setOnClickListener{
            navController.navigate(R.id.action_account_fragment_to_addAd_fragment)
            alladList.clear()
        }



    }

    fun getAllAdfromFirebase(){
        FirebaseInstance.firebaseFirestoreInstanse.collection("adresses")
                .document(cityfromDb)
                .collection(cityfromDb)
                .orderBy("Time", Query.Direction.DESCENDING)
                .get().addOnSuccessListener {
                    if (it.isEmpty){
                        textView_ad_isEmpty.visibility = View.VISIBLE
                    }
            for (document in it) {
                val modelAd = AdModel(document.get("NameAd").toString(), document.get("User").toString(),
                        document.get("Address").toString(), document.get("ImageUrl").toString(), document.get("Description").toString(), document.get("Time").toString())
                alladList.add(modelAd)
                val adapter = AllAdRecycler(alladList)
                recycler_view_ad.layoutManager = LinearLayoutManager(context)
                recycler_view_ad.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun roomDbInsertCity(city_: String){
        val city = RoomModelCity(1, city_)
        GlobalScope.launch {
            roomDb.cityDao().insert(city)
        }
    }

    private  fun roomDbGetCity() {
        GlobalScope.launch {
            delay(5000)
            val cityfromroom = roomDb.cityDao().getCity().city
            withContext(Dispatchers.Main) {
                if (cityfromroom != null) {
                    cityfromDb = cityfromroom
                }
                getAllAdfromFirebase()
            }
        }
    }


   @SuppressLint("MissingPermission")
   fun getLastKnownLocation() {

           fusedLocationClient.lastLocation
                   .addOnSuccessListener { location->
                       if (location != null) {
                           val gcd = Geocoder(requireContext(), Locale.getDefault())
                           val latitude: Double = location.latitude
                           val longitude: Double = location.longitude
                           val addresses = gcd.getFromLocation(latitude, longitude, 1)
                           roomDbInsertCity(addresses[0].locality.toString())
                           Log.i("tag", addresses[0].getAddressLine(0))
                           Toast.makeText(requireContext(), addresses[0].locality, Toast.LENGTH_SHORT).show()
                       } else {
                           Toast.makeText(requireContext(), "locality null", Toast.LENGTH_SHORT).show()
                           getlocationfromNetwork()
                       }
                   }
   }

    @SuppressLint("MissingPermission")
    private fun getlocationfromNetwork(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object : LocationListener{
            override fun onLocationChanged(location: Location) {
                    val gcd = Geocoder(requireContext(), Locale.getDefault())
                    val latitude: Double = location.latitude
                    val longitude: Double = location.longitude
                    val addresses = gcd.getFromLocation(latitude, longitude, 1)
                    roomDbInsertCity(addresses[0].locality.toString())
                    Toast.makeText(requireContext(), addresses[0].locality, Toast.LENGTH_SHORT).show()
            }

            override fun onProviderDisabled(provider: String) {
                Toast.makeText(requireContext(), "PROVIDER DISBLED", Toast.LENGTH_SHORT).show()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
        })
    }

}