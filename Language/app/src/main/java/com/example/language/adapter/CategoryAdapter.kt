package com.example.language.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.language.MainActivity.Companion.showCoursesByCategory
import com.example.language.R
import com.example.language.model.Category
import com.example.language.view_holder.CategoryViewHolder

class CategoryAdapter(var context: Context, private var categories: List<Category>):
    RecyclerView.Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val categoryItems =
            LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(categoryItems)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.categoryTitle.text = categories[position].title
        holder.itemView.setOnClickListener { showCoursesByCategory(categories[holder.adapterPosition].id) }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}