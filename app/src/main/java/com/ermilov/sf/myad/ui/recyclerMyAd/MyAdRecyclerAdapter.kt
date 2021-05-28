package com.ermilov.sf.myad.ui.recyclerMyAd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ermilov.sf.R
import com.ermilov.sf.account.model.AdModel

class MyAdRecyclerAdapter(var list: List<AdModel>) : RecyclerView.Adapter<MyAdRecyclerAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_recycler_item, parent, false)
        return MainHolder(view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MainHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val nameAd = itemView.findViewById<TextView>(R.id.textView_recycler_name_ad)
        private val date = itemView.findViewById<TextView>(R.id.textView_recycler_time)
        private val imageAd = itemView.findViewById<ImageView>(R.id.imageView_recycler_ad)
        fun bind(adModel: AdModel){
            nameAd.text = adModel.name_ad
            date.text = adModel.time
            Glide.with(itemView.context).load(adModel.image).into(imageAd)
        }
    }

}