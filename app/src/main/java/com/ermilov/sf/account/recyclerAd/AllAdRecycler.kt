package com.ermilov.sf.account.recyclerAd

import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ermilov.sf.R
import com.ermilov.sf.account.model.AdModel

class AllAdRecycler(var list: List<AdModel>, val listener: OnItemClickListener) : RecyclerView.Adapter<AllAdRecycler.MainHolder>() {


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

    inner class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val nameAd = itemView.findViewById<TextView>(R.id.textView_recycler_name_ad)
        private val date = itemView.findViewById<TextView>(R.id.textView_recycler_time)
        private val imageAd = itemView.findViewById<ImageView>(R.id.imageView_recycler_ad)
        fun bind(adModel: AdModel){
            nameAd.text = adModel.name_ad
            date.text = adModel.time
            Glide.with(itemView.context).load(adModel.image).into(imageAd)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val name = list[position].name_ad
            val user = list[position].user
            val image = list[position].image
            val details = list[position].details
            val time = list[position].time
            val autor = list[position].user
            listener.onItemClick(name, user, image, details, time, autor)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(name: String, user: String, image: String, details: String, time: String, autor: String)
    }
}