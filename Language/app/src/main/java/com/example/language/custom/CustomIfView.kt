package com.example.language.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.language.databinding.CustomIfBinding

class CustomIfView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _condition: String = ""


    private val binding = CustomIfBinding.inflate(LayoutInflater.from(context), this)

    fun varCondition(condition: String){
        _condition = condition
    }

    private fun checkOnVoidCondition(condition: String): Boolean {
        val condReg = Regex("\\" + "s")
        val newCond = condReg.replace(condition, "")

        if (newCond.isNotEmpty()) {
            return true
        }
        return false
    }

    fun readyString(): String {
        return if (checkOnVoidCondition(_condition)) {
            ("IF $_condition\n")
        } else{
            "Error void Condition"
        }
    }
}