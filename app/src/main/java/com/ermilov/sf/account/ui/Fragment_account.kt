package com.ermilov.sf.account.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ermilov.sf.R
import com.ermilov.sf.account.model.AdModel
import com.ermilov.sf.account.recyclerAd.AllAdRecycler
import com.ermilov.sf.account.room.CityViewModel
import com.ermilov.sf.util.FirebaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_account.*


class Fragment_account : Fragment(), AllAdRecycler.OnItemClickListener {

    lateinit var toogle: ActionBarDrawerToggle
    lateinit var navController: NavController
    lateinit var cityViewModel: CityViewModel
    companion object{
        val firebaseInstance = FirebaseInstance
    }
    var alladList = ArrayList<AdModel>()
    var cityfromDb: String = String()
    var buttonCount = 0



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        alladList.clear()
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        toogle = ActionBarDrawerToggle(requireActivity(), drawer_layout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawer_layout.addDrawerListener(toogle)
        toogle.syncState()
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.myAd ->  navController.navigate(R.id.action_fragment_account_to_myAd)
                R.id.myMessages -> navController.navigate(R.id.action_fragment_account_to_myMessage)
                R.id.exitAccount -> signOut()
            }
            true
        }
        drawer_layout.closeDrawer(GravityCompat.START)

        roomDbGetCity()

        floatingActionButton_Add_ad.setOnClickListener {
            navController.navigate(R.id.action_account_fragment_to_addAd_fragment)

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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getAllAdfromFirebase() {
        firebaseInstance.firebaseFirestoreInstanse().collection("adresses")
                .document(cityfromDb)
                .collection(cityfromDb)
                .orderBy("Time", Query.Direction.DESCENDING)
                .get().addOnSuccessListener {
                    if (it.isEmpty) {
                        textView_ad_isEmpty.visibility = View.VISIBLE
                    }
                    for (document in it) {
                        val modelAd = AdModel(document.get("NameAd").toString(), document.get("User").toString(),
                                document.get("Address").toString(), document.get("ImageUrl").toString(), document.get("Description").toString(), document.get("Time").toString())
                        alladList.add(modelAd)
                        val adapter = AllAdRecycler(alladList, this)
                        recycler_view_ad.layoutManager = LinearLayoutManager(context)
                        recycler_view_ad.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
    }

    private fun roomDbGetCity() {
        var cityfromroom: String
        cityViewModel.readCity.observe(viewLifecycleOwner, {
            if(it==null){
                val action = Fragment_accountDirections.actionFragmentAccountToChooseCityFragment("account")
                navController.navigate(action)
            } else {
                cityfromroom = it.city.toString()
                cityfromDb = cityfromroom
                Log.i("tag", cityfromroom)
                getAllAdfromFirebase()
            }
        })
    }

    override fun onItemClick(name: String, user: String, image: String, details: String, time: String, autor: String) {
        val action = Fragment_accountDirections.actionFragmentAccountToAdOpenInfo(image, name, details, time, autor)
        navController.navigate(action)
    }

    fun signOut(){
        firebaseInstance.firebaseUser.signOut()
        navController.navigate(R.id.action_fragment_account_to_fragment_sign_in)
    }
}