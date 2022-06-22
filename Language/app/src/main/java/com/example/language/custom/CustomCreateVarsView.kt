package com.example.language.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.language.databinding.CustomCreateVarsBinding

class CustomCreateVarsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _name: String = ""
    private var _data: String = "0"


    private val binding = CustomCreateVarsBinding.inflate(LayoutInflater.from(context), this)

    fun varName(name: String) {
        _name = name
    }

    fun varData(data: String) {
        _data = data
    }

    private fun checkOnVoidName(name: String): Boolean {
        val nameReg = Regex("\\" + "s")
        val newName = nameReg.replace(name, "")

        if (newName.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun checkOnIncorrectName(name: String): Boolean {
        val checkOnCorrectName = Regex("[A-Z0-9~!@#$%^&*+-]")
        val correctName = name.contains(checkOnCorrectName)

        if (!correctName) {
            return true
        }
        return false
    }

    private fun checkOnVoidData(data: String): Boolean {
        val dataReg = Regex("\\" + "s")
        val newData = dataReg.replace(data, "")

        if (newData.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun checkOnIncorrectData(data: String): Boolean {
        val checkOnCorrectData = Regex("[~!@#$%^&]")
        val correctData = data.contains(checkOnCorrectData)

        if (!correctData) {
            return true
        }
        return false
    }


    fun readyString(): String {
        if (checkOnVoidName(_name)) {

            if (checkOnIncorrectName(_name)) {

                return if (checkOnVoidData(_data)) {

                    if (checkOnIncorrectData(_data)) {

                        ("$_name=$_data\n")
                    }
                    else {
                        "Error in Data"
                    }
                }
                else {
                    "Error void Data"
                }
            }
            else {
                return "Error in Name"
            }
        }
        else {
            return "Error void Name"
        }
    }
}
