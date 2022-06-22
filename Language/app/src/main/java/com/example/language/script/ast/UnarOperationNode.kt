package com.example.language.script.ast

import com.example.language.script.Token

class UnarOperationNode(var op: Token, var operand: ExpressionNode): ExpressionNode() {
}