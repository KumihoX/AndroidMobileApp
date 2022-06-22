package com.example.language.script

import com.example.language.script.ast.*
import java.lang.Exception

class Parser(val tokens: Array<Token>) {
    var pos = 0
    var scope = mutableMapOf<String, Int?>()
    var strPrint = ""

    fun match(type: TokenType?): Token{
        if (pos < tokens.size){
            val currenToken = tokens[pos]
            if (type?.name  == currenToken.type.name){
                pos += 1
                return currenToken
            }
        }
        return Token(TokenType("", ""), "", 0)
    }

    fun require(type: TokenType?): Token{
        val token = match(type)
        if (token.type.name == ""){
            throw Exception("Ожидался другой токен")
        }
        return token
    }

    fun matchForFormula(): Token{
        var token = match(TokenTypes["minus"])
        if (token.type.name == ""){
            token = match(TokenTypes["plus"])
            if (token.type.name == ""){
                token = match(TokenTypes["multi"])
                if (token.type.name == ""){
                    token = match(TokenTypes["division"])
                }
            }
        }
        if (token.type.name != ""){
            return token
        }
        else{
            return Token(TokenType("", ""), "", 0)
        }
    }

    fun matchForIf(): Token{
        var token = match(TokenTypes["assAssing"])
        if (token.type.name == ""){
            token = match(TokenTypes["noAssign"])
            if (token.type.name == ""){
                token = match(TokenTypes["moreAssign"])
                if (token.type.name == ""){
                    token = match(TokenTypes["lessAssign"])
                    if (token.type.name == ""){
                        token = match(TokenTypes["more"])
                        if (token.type.name == ""){
                            token = match(TokenTypes["less"])
                        }
                    }
                }
            }
        }
        if (token.type.name != ""){
            return token
        }
        else{
            return Token(TokenType("", ""), "", 0)
        }
    }

    fun parseVariableOrNumber(): ExpressionNode {
        val number = match(TokenTypes["number"])
        if (number.type.name != "") {
            return NumberNode(number)
        }
        val variable = match(TokenTypes["variable"])
        if (variable.type.name != ""){
            return VarNode(variable)
        }
        throw Exception("Ошибка Позиция ${pos}")
    }

    fun parsePrint():ExpressionNode{
        val operatorLog = match(TokenTypes["print"])
        if (operatorLog.type.name != ""){
            return UnarOperationNode(operatorLog, parseFormula())
        }
        throw Exception("Нечего выводить")
    }

    fun parseParentheses(): ExpressionNode{
        val lPair = match(TokenTypes["lPair"])
        if (lPair.type.name != ""){
            val node = parseFormula()
            require(TokenTypes["rPair"])
            return node
        }
        else{
            return parseVariableOrNumber()
        }
    }

    fun parseFormula(): ExpressionNode{
        var left = parseParentheses()
        var operator = matchForFormula()
        while (operator.type.name != ""){
            val right = parseParentheses()
            left = BinOperationNode(operator, left, right)
            operator = matchForFormula()
        }
        return  left
    }

    fun parseIf(): ExpressionNode{
        var left = parseParentheses()
        var operator = matchForIf()
        val right = parseParentheses()
        var arrayNode = arrayOf<ExpressionNode>()
        while (match(TokenTypes["end"]).type.name == "" && match(TokenTypes["else"]).type.name == ""){
            if (pos > tokens.size){
                throw Exception("Ожидалось END или ELSE")
            }
            val codeStringNode = parseExpression()
            arrayNode += codeStringNode
        }
        pos -= 1
        if (match(TokenTypes["else"]).type.name != ""){
            val elseNode = parseElse()
            val ifNode = IfNode(operator, left, right, arrayNode, elseNode)
            return ifNode
        }
        if (match(TokenTypes["end"]).type.name != "") {
            val ifNode = IfNode(operator, left, right, arrayNode, null)
            return ifNode
        }
        throw Exception("Ожидалось END или ELSE; Позиция ${pos}")
    }

    fun parseWhile(): ExpressionNode{
        val ifNode = parseIf()
        val whileNode = WhileNode(ifNode)
        return whileNode
    }

    fun parseCreate(): ExpressionNode{
        val variable = match(TokenTypes["variable"])
        return CreateVarNode(variable)
    }

    fun parseExpression(): ExpressionNode{
        if (match(TokenTypes["while"]).type.name != "") {
            val whileNode = parseWhile()
            return whileNode
        }

        if (match(TokenTypes["if"]).type.name != "") {
            val ifNode = parseIf()
            return ifNode
        }

        if (match(TokenTypes["print"]).type.name != ""){
            pos -= 1
            val printNode = parsePrint()
            return printNode
        }

        if (match(TokenTypes["variable"]).type.name != ""){
            pos -= 1
            val variableNode = parseVariableOrNumber()
            val assignOperation = match(TokenTypes["assign"])
            if (assignOperation.type.name != ""){
                val rightNode = parseFormula()
                val binOperationNode = BinOperationNode(assignOperation, variableNode, rightNode)
                return binOperationNode
            }
            pos -= 1
            val createVarNode = parseCreate()
            return createVarNode
        }
        throw Exception("Ожидалось VAR, IF, PRINT или WHILE")
    }

    fun parseElse(): ExpressionNode{
        var arrayNode = arrayOf<ExpressionNode>()
        while (match(TokenTypes["end"]).type.name == ""){
            if (pos > tokens.size){
                throw Exception("Ожидалось END")
            }
            val codeStringNode = parseExpression()
            arrayNode += codeStringNode
        }
        val elseNode = ElseNode(arrayNode)
        return elseNode
    }

    fun parseCode(): ExpressionNode{
        var root = StatementsNode()
        while (pos < tokens.size){
            val codeStringNode = parseExpression()
            root.addNode(codeStringNode)
        }
        return root
    }

    fun run(node: ExpressionNode): Int? {


        if (node is StatementsNode) {
            node.codeStrings.forEach { codeString ->
                run(codeString)
            }
            return null
        }
        if (node is NumberNode) {
            return node.number.text.toInt()
        }
        if (node is UnarOperationNode) {
            when (node.op.type.name) {
                TokenTypes["print"]?.name -> customPrint(run(node.operand))
            }
        }
        if (node is VarNode) {
            if (scope[node.variable.text] != null) {
                return scope[node.variable.text]
            } else {
                throw Exception("Не существует данной переменной")
            }
        }
        if (node is CreateVarNode) {
            scope.put(node.variable.text, null)
            return null
        }
        if (node is BinOperationNode) {
            when (node.op.type.name) {
                TokenTypes["plus"]?.name -> return run(node.leftNode)!! + run(node.rightNode)!!
                TokenTypes["minus"]?.name -> return run(node.leftNode)!! - run(node.rightNode)!!
                TokenTypes["multi"]?.name -> return run(node.leftNode)!! * run(node.rightNode)!!
                TokenTypes["division"]?.name -> return run(node.leftNode)!! / run(node.rightNode)!!
                TokenTypes["assign"]?.name -> {
                    val res = run(node.rightNode)
                    val varNode = node.leftNode
                    if (varNode is VarNode)
                        scope.put(varNode.variable.text, res!!)
                    return res
                }
            }
        }
        if (node is IfNode) {
            var truthful: Boolean? = null
            when (node.op.type.name) {
                TokenTypes["assAssing"]?.name -> truthful =
                    run(node.leftNode)!! == run(node.rightNode)!!
                TokenTypes["noAssign"]?.name -> truthful =
                    run(node.leftNode)!! != run(node.rightNode)!!
                TokenTypes["moreAssign"]?.name -> truthful =
                    run(node.leftNode)!! >= run(node.rightNode)!!
                TokenTypes["lessAssign"]?.name -> truthful =
                    run(node.leftNode)!! <= run(node.rightNode)!!
                TokenTypes["more"]?.name -> truthful = run(node.leftNode)!! > run(node.rightNode)!!
                TokenTypes["less"]?.name -> truthful = run(node.leftNode)!! < run(node.rightNode)!!
            }
            if (truthful == true) {
                for (i in node.commandArray) {
                    run(i)
                }
            }
            else{
                if (node.elseNode is ElseNode){
                    for (i in (node.elseNode as ElseNode).commandArray){
                        run(i)
                    }
                }
            }
            return null
        }
        if (node is WhileNode) {
            if (node.op is IfNode) {
                do {
                    var truthful: Boolean? = null
                    val ifNode = node.op as IfNode
                    when (ifNode.op.type.name) {
                        TokenTypes["assAssing"]?.name -> truthful =
                            run(ifNode.leftNode)!! == run(ifNode.rightNode)!!
                        TokenTypes["noAssign"]?.name -> truthful =
                            run(ifNode.leftNode)!! != run(ifNode.rightNode)!!
                        TokenTypes["moreAssign"]?.name -> truthful =
                            run(ifNode.leftNode)!! >= run(ifNode.rightNode)!!
                        TokenTypes["lessAssign"]?.name -> truthful =
                            run(ifNode.leftNode)!! <= run(ifNode.rightNode)!!
                        TokenTypes["more"]?.name -> truthful =
                            run(ifNode.leftNode)!! > run(ifNode.rightNode)!!
                        TokenTypes["less"]?.name -> truthful =
                            run(ifNode.leftNode)!! < run(ifNode.rightNode)!!
                    }
                    if (truthful == true) {
                        for (i in ifNode.commandArray) {
                            run(i)
                        }
                    }
                } while (truthful == true)
                return null
            }
            throw Exception("Нет бинарного оператора сравнения")
        }
        return 0
    }

    fun customPrint(value: Int?){
        strPrint += "\n" + value.toString()
    }

    fun output(): String{
        return strPrint
    }
}