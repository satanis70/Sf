package com.ermilov.sf.adopenfromRecycler

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ermilov.sf.R
import com.ermilov.sf.util.FirebaseInstance
import kotlinx.android.synthetic.main.fragment_ad_open_info.*


class AdOpenInfo : Fragment() {

    val args : AdOpenInfoArgs by navArgs()
    lateinit var navController: NavController
    var autor = String()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ad_open_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val imageUrl = args.adImage
        Glide.with(requireContext()).load(imageUrl).into(adOpen_imageView)
        adOpen_textView_name.text = args.adName
        adOpen_textView_details.text = args.adDetail
        adOpen_textView_Time.text = args.adTime
        autor = args.adUser
        if (FirebaseInstance.firebaseUser.currentUser!!.uid==autor){
            adOpen_button_chat.visibility = View.GONE
        }
        adOpen_button_chat.setOnClickListener {
            val action = AdOpenInfoDirections.actionAdOpenInfoToChatFragment(autor)
            navController.navigate(action)
        }
    }
}