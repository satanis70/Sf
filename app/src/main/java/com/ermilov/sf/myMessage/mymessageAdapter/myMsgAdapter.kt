package com.ermilov.sf.myMessage.mymessageAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ermilov.sf.R
import com.ermilov.sf.myMessage.model.mymessageModel

class myMsgAdapter(val list: List<mymessageModel>) : RecyclerView.Adapter<myMsgAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_message_item, parent, false)
        return MainHolder(view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MainHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.profileChat_image)
        private val name = view.findViewById<TextView>(R.id.nicknameChat_textView)
        private val lastMessage = view.findViewById<TextView>(R.id.lastmessage_textView)

        fun bind(mymessageModel: mymessageModel){
            image.setImageResource(R.drawable.user_chat_circle)
            name.text = mymessageModel.name
            lastMessage.text = mymessageModel.lastMessage
        }
    }
}