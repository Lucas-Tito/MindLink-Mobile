package com.titolucas.mindlink.search.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
            .inflate(R.layout.item_psychologist, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImage: ImageView = itemView.findViewById(R.id.psychologist_image)
        private val nameText: TextView = itemView.findViewById(R.id.psychologist_name)
        private val specialtyText: TextView = itemView.findViewById(R.id.psychologist_specialty)
        private val ratingText: TextView = itemView.findViewById(R.id.psychologist_rating)
        private val scheduleButton: Button = itemView.findViewById(R.id.schedule_button)

        fun bind(user: UserResponse) {
            nameText.text = user.name
            specialtyText.text = user.bio ?: "Especialidade não informada"
            ratingText.text = "★ ${user.rating ?: "N/A"}"

            Glide.with(itemView.context)
                .load(user.photoURL)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(profileImage)

            scheduleButton.setOnClickListener {
                //onScheduleClick(user)
            }
        }
    }
}