package com.example.language.script

class TokenType(var name: String, var reg: String) {

    //Конструтор
}

//Список типов токенов
val TokenTypes = mapOf(
    "space" to TokenType("space", ("\\" + "s")),
    "assAssing" to TokenType("assAssign", "=="),
    "assign" to TokenType("assign", "="),
    "plus" to TokenType("plus", ("\\" + "+")),
    "minus" to TokenType("minus", "-"),
    "multi" to TokenType("multi", ("\\" + "*")),
    "division" to TokenType("division", "/"),
    "lPair" to TokenType("lPair", ("\\" + "(")),
    "rPair" to TokenType("rPair", ("\\" + ")")),
    "if" to TokenType("if", "IF"),
    "print" to TokenType("print", "PRINT"),
    "noAssign" to TokenType("noAssign", "!="),
    "moreAssign" to TokenType("moreAssign",">="),
    "lessAssign" to TokenType("lessAssign", "<="),
    "input" to TokenType("input", "INPUT"),
    "while" to TokenType("while", "WHILE"),
    "else" to TokenType("else", "ELSE"),
    "end" to TokenType("end", "END"),
    "more" to TokenType("more", ">"),
    "less" to TokenType("less", "<"),
    "number" to TokenType("number", "[0-9]+"),
    "variable" to TokenType("variable", "[a-z]+")
)