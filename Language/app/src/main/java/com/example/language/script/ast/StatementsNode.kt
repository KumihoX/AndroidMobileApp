package com.example.language.script.ast

class StatementsNode: ExpressionNode() {
    var codeStrings = arrayOf<ExpressionNode>()

    fun addNode(node: ExpressionNode){
        codeStrings += node
    }
}