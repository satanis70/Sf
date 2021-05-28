package com.ermilov.sf.myad.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ermilov.sf.R
import com.ermilov.sf.account.model.AdModel
import com.ermilov.sf.myad.ui.recyclerMyAd.MyAdRecyclerAdapter
import com.ermilov.sf.util.FirebaseInstance
import kotlinx.android.synthetic.main.fragment_my_ad.*


class MyAdFragment : Fragment() {

    lateinit var navController: NavController
    companion object{
        val firebaseInstance = FirebaseInstance
    }
    var listMyad = ArrayList<AdModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        listMyad.clear()
        getMyAd()
    }


    fun getMyAd(){
        firebaseInstance.firebaseFirestoreInstanse().collection("MyAd")
                .document(firebaseInstance.firebaseUser.currentUser!!.uid)
                .collection(firebaseInstance.firebaseUser.currentUser!!.uid)
                .get().addOnSuccessListener {
                    if (it.isEmpty){
                        textView_Myad_isEmpty.visibility = View.VISIBLE
                    }

                    for (document in it){
                        val modelAd = AdModel(document.get("NameAd").toString(), document.get("User").toString(),
                                document.get("Address").toString(), document.get("ImageUrl").toString(), document.get("Description").toString(), document.get("Time").toString())
                        listMyad.add(modelAd)
                        val adapter = MyAdRecyclerAdapter(listMyad)
                        myAd_recycler_view.layoutManager = LinearLayoutManager(context)
                        myAd_recycler_view.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
    }
}