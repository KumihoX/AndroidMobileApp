package com.example.language.script

class Run(val code: String) {
    fun go(): String {
        val lexer = Lexer(code)
        val arrayTokens = lexer.lexAnalysis()
        val parser = Parser(arrayTokens)
        val root = parser.parseCode()
        val res = parser.run(root)
        val output = parser.output()
        return output
    }
}