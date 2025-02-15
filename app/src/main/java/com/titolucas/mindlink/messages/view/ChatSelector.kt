package com.titolucas.mindlink.messages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.messages.data.Chat
import com.titolucas.mindlink.network.RetrofitInstance
import kotlinx.coroutines.launch

class ChatSelector : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_chat_selector)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        //Requisição para obter os dados
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            lifecycleScope.launch {
                val conversations = RetrofitInstance.apiService.getConversationsByUserId(userId)
                val chatList = conversations.map { conversation ->
                    Chat(
                        nomeChat = conversation.contactName,
                        lastMessageChat = conversation.messages.lastOrNull()?.text ?: "",
                        hora = conversation.messages.lastOrNull()?.createdAt ?: ""
                    )
                }


                recyclerView.adapter = ChatAdapter(chatList)
            }
        }

    }
}