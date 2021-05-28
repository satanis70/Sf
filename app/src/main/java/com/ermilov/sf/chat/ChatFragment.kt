package com.ermilov.sf.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ermilov.sf.R
import com.ermilov.sf.util.FirebaseInstance
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.model.Document
import kotlinx.android.synthetic.main.fragment_chat.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {

    val args: ChatFragmentArgs by navArgs()
    lateinit var navController: NavController
    val firebaseInstance = FirebaseInstance
    var arrayListMessage = ArrayList<MessageModel>()
    var queryLastResult: DocumentSnapshot? = null
    lateinit var refChat: CollectionReference
    lateinit var layoutmanager: LinearLayoutManager
    lateinit var adapter: ChatAdapter

    private val lastVisibleItemPosition: Int
        get() = layoutmanager.findLastVisibleItemPosition()

    lateinit var lastDocumentSnapshot : DocumentSnapshot
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        layoutmanager = LinearLayoutManager(context)
        layoutmanager.stackFromEnd = true
        layoutmanager.reverseLayout = false
        refChat = firebaseInstance.firebaseFirestoreInstanse().collection("UsersChat")
                .document(firebaseInstance.firebaseUser.currentUser!!.uid)
                .collection(args.adAutor)
        sendMessage()
        showMessages()
        scrollrecycler()
    }

    private fun showMessages(){

        arrayListMessage.clear()

        val first = refChat.orderBy("time", Query.Direction.DESCENDING).limit(18)
            first.get().addOnSuccessListener { documentSnapshots ->
                if (documentSnapshots.size()!=0){
                    for (i in documentSnapshots.documents) {
                        arrayListMessage.add(MessageModel(i["autor"].toString(), i["message"].toString(), i["time"].toString()))
                        lastDocumentSnapshot = i
                    }
                    arrayListMessage.reverse()
                    adapter = ChatAdapter(arrayListMessage)
                    recycler_message.layoutManager = layoutmanager
                    recycler_message.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

            }
    }

    fun scrollrecycler(){

        scrollListener = object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING ) {
                    val next = refChat.orderBy("time", Query.Direction.DESCENDING).startAfter(lastDocumentSnapshot).limit(2).get().addOnSuccessListener {
                        for (i in it.documents){
                            arrayListMessage.add(MessageModel(i["autor"].toString(), i["message"].toString(), i["time"].toString()))
                            Toast.makeText(requireContext(), it.size().toString(), Toast.LENGTH_SHORT).show()
                            lastDocumentSnapshot = i
                        }
                        adapter = ChatAdapter(arrayListMessage)
                        recycler_message.layoutManager = layoutmanager
                        recycler_message.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }

            }
        }

        recycler_message.addOnScrollListener(scrollListener)

    }



    private fun currentDate() : String{
        val date = Date()
        val newDate = Date(date.time)
        val dt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dt.format(newDate)
    }

    fun sendMessage(){
        imageView_send.setOnClickListener {
            if (!fragmentChat_messageEditText.text?.isEmpty()!!){
                firebaseInstance.firebaseFirestoreInstanse().collection("Friends")
                        .document(firebaseInstance.firebaseUser.currentUser!!.uid)
                        .collection(args.adAutor)
                        .document(args.adAutor)
                        .set(mapOf(args.adAutor to String()))
                firebaseInstance.firebaseFirestoreInstanse().collection("Friends")
                        .document(args.adAutor)
                        .collection(firebaseInstance.firebaseUser.currentUser!!.uid)
                        .document(firebaseInstance.firebaseUser.currentUser!!.uid)
                        .set(mapOf(firebaseInstance.firebaseUser.currentUser!!.uid to String()))

                firebaseInstance.firebaseFirestoreInstanse().collection("UsersChat")
                        .document(firebaseInstance.firebaseUser.currentUser!!.uid)
                        .collection(args.adAutor)
                        .document()
                        .set(MessageModel(firebaseInstance.firebaseUser.currentUser!!.uid, fragmentChat_messageEditText.text.toString(), currentDate()))

                firebaseInstance.firebaseFirestoreInstanse().collection("UsersChat")
                        .document(args.adAutor)
                        .collection(firebaseInstance.firebaseUser.currentUser!!.uid)
                        .document()
                        .set(MessageModel(firebaseInstance.firebaseUser.currentUser!!.uid, fragmentChat_messageEditText.text.toString(), currentDate()))

                fragmentChat_messageEditText.text = null
                showMessages()
            } else {
                Toast.makeText(requireContext(), R.string.inputMessage_empty, Toast.LENGTH_LONG).show()
            }
        }
    }

}