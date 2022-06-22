package com.example.language.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.language.R
import com.example.language.model.Action
import com.example.language.view_holder.ActionViewHolder

class ActionAdapter(private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<ActionViewHolder>() {
    private var actionList: List<Action>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.course_item, parent, false)

        return ActionViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder.actionTitle.text = actionList!![position].title
    }

    override fun getItemCount(): Int {
        return if (actionList == null) 0 else actionList!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAllActions(actions: List<Action>) {
        actionList = actions
        notifyDataSetChanged()
    }
}