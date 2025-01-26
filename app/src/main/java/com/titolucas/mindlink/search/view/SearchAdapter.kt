package com.titolucas.mindlink.search.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.titolucas.mindlink.R
import com.titolucas.mindlink.generalData.UserResponse

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val data = mutableListOf<UserResponse>()

    fun submitList(newData: List<UserResponse>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImage: ShapeableImageView = itemView.findViewById(R.id.profile_image)
        private val nameText: TextView = itemView.findViewById(R.id.name_text)
        private val bioText: TextView = itemView.findViewById(R.id.bio_text)

        fun bind(user: UserResponse) {
            nameText.text = user.name
            bioText.text = user.bio ?: "Sem descrição"
            Glide.with(itemView.context).load(user.photoURL).into(profileImage)

            //COMECEI A FAZER O ONCLICK PRA IR PRO CALENDARIO DE AGENDAMENTO
            itemView.setOnClickListener {
                Log.i("", "Clicou em um dos psicólogos")
            }
        }
    }
}