package com.ermilov.sf.chat

import android.graphics.Color
import android.graphics.Color.GRAY
import android.graphics.Color.GREEN
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ermilov.sf.R
import com.ermilov.sf.util.FirebaseInstance
import kotlinx.android.synthetic.main.message_item.view.*
import java.util.*

class ChatAdapter(var list: List<MessageModel>) : RecyclerView.Adapter<ChatAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MainHolder(view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        private var autor: String? = null
        private val message = itemView.findViewById<TextView>(R.id.textView_message_text)
        private val time = itemView.findViewById<TextView>(R.id.textView_message_time)
        fun bind(messageModel: MessageModel){
            autor = messageModel.autor
            message.text = messageModel.message
            time.text = messageModel.time
            if (autor==FirebaseInstance.firebaseUser.uid){
                itemView.message_root.gravity = Gravity.END
                itemView.message_root_relative.setBackgroundResource(R.drawable.rect_round_primary_color2)
            } else {
                itemView.message_root_relative.setBackgroundResource(R.drawable.rect_round_primary_color)
            }
        }


    }
}