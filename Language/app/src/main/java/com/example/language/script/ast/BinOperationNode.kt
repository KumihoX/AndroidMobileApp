package com.example.language.script.ast

import com.example.language.script.Token

class BinOperationNode(var op: Token, var leftNode: ExpressionNode, var rightNode: ExpressionNode ): ExpressionNode() {
}