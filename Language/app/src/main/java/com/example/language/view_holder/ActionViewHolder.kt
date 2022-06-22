package com.example.language.view_holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.language.R

class ActionViewHolder(
    itemView: View,
    private val onItemClicked: ((position: Int) -> Unit)
) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

    var actionTitle: TextView

    init {
        actionTitle = itemView.findViewById(R.id.courseTitle)
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val position = adapterPosition
        onItemClicked?.let { it(position) }
    }
}