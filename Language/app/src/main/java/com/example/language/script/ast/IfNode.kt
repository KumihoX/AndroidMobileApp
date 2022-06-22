package com.example.language.script.ast

import com.example.language.script.Token

class IfNode(var op: Token, var leftNode: ExpressionNode, var rightNode: ExpressionNode, var commandArray: Array<ExpressionNode>, var elseNode: ExpressionNode? ): ExpressionNode() {
}