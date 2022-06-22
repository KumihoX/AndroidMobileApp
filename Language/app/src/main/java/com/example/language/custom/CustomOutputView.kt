package com.example.language.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.language.databinding.CustomOutputBinding

class CustomOutputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _variables: String = ""

    private val binding = CustomOutputBinding.inflate(LayoutInflater.from(context), this)

    fun varVariables(variables: String){
        _variables = variables
    }

    private fun checkOnVoidVariables(variables: String): Boolean {
        val elemReg = Regex("\\" + "s")
        val newElem = elemReg.replace(variables, "")

        if (newElem.isNotEmpty()) {
            return true
        }
        return false
    }

    fun readyString(): String {
        return if (checkOnVoidVariables(_variables)) {
            ("PRINT $_variables\n")
        } else{
            "Error void elem"
        }
    }
}