package com.example.language.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.language.databinding.CustomElseBinding

class CustomElseView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {


    private val binding = CustomElseBinding.inflate(LayoutInflater.from(context), this)

    fun readyString(): String {
        return ("ELSE" + "\n")
    }
}