package com.example.language.view_holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.language.R

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var categoryTitle: TextView

    init {
        categoryTitle = itemView.findViewById(R.id.categoryTitle)
    }
}