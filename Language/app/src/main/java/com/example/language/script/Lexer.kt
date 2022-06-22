package com.example.language.script

import com.example.language.script.ast.StatementsNode

class Lexer(val code: String) {
    var pos = 0;
    var tokenList = arrayOf<Token>()
    var newNode: StatementsNode? = null

    fun lexAnalysis(): Array<Token>{
        while(nextToken()){
        }
        return tokenList
    }
    fun nextToken(): Boolean{
        if (pos >= code.length){
            return false
        }
        for ((name, type) in TokenTypes){
            val stringReg = "^" + type.reg
            val regular = Regex(stringReg)
            val result =  regular.find(code.substring(pos))
            val strRes = result?.value
            if (strRes != null){
                val token = Token(type, strRes, pos)
                pos += strRes.length
                if (token.type.name != TokenTypes["space"]?.name) {
                    tokenList += token
                }
                return true
            }
        }
        throw Exception("Выход за пределы массива")
    }
}