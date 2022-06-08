package calculator

fun main() {

    var input: String
    val variables = mutableMapOf<String, Int>()

    do {
        input = readln()
        if (input.count { it == '(' } != input.count { it == ')' }) println("Invalid Expression")
        if (input.contains("(")) {
            input = input.replace("\\(".toRegex(), "( ")
            input = input.replace("\\)".toRegex(), " )")
        }


        when  {
            """""".toRegex().matches(input) -> continue
            """/exit""".toRegex().matches(input) -> println("Bye!")
            """/help""".toRegex().matches(input) -> println("The program calculates expressions like : 4 + ( 6 * 8 ), 2 - 3 - 4, and so on. It supports both unary and binary minus operators. The program accepts several same operators following each other.")
            """/\w+""".toRegex().matches(input) -> println("Unknown command")
            """\+\d+""".toRegex().matches(input) -> println(input.replace("+", ""))
            """\s*[a-zA-Z]+\s*""".toRegex().matches(input) -> if (variables.containsKey(input.replace(" ", ""))) println(variables[input.replace(" ", "")]) else println("Unknown variable")
            """\s*\w*\s*=\s*-?\w*\s*""".toRegex().matches(input) -> assignVariable(input, variables)
            else -> {
                try {
                    println(calculatePostfix(convertToPostfix(input), variables))
                } catch (e: Exception) {
                    println(e.message)
                }
            }

        }

    } while (input != "/exit")
}

fun calculatePostfix(postfix: MutableList<String>, variables: MutableMap<String, Int>): Int {
    val stack = mutableListOf<Int>()

    for (item in postfix) {
        when {
            """-?\d+""".toRegex().matches(item) -> stack.add(item.toInt())
            """\w+""".toRegex().matches(item) -> stack.add(variables[item]!!)
            """-+|\++|\*|/""".toRegex().matches(item) -> {
                val first = stack.last()
                stack.removeLast()
                val second = stack.last()
                stack.removeLast()
                when {
                    """\++""".toRegex().matches(item) -> stack.add(first + second)
                    """-+""".toRegex().matches(item) -> if (item.length % 2 == 0) stack.add(first + second) else stack.add(second - first)
                    """\*""".toRegex().matches(item) -> stack.add(first * second)
                    """/""".toRegex().matches(item) -> stack.add(second / first)
                }
            }
        }
    }

    return stack.last()
}

fun convertToPostfix(expression: String): MutableList<String> {
    val postfix = mutableListOf<String>()
    val infix = expression.split(" ").map { it }.toMutableList()
    val stack = mutableListOf<String>()

    for (item in infix) {
        when {
            """-?(\d+|[a-zA-Z]+)""".toRegex().matches(item) -> postfix.add(item)
            """-+|\++|\*|/|\(|\)""".toRegex().matches(item) -> {
                when {
                    stack.isEmpty() || stack.last() == "(" -> stack.add(item)
                    item == "(" -> stack.add(item)
                    item == ")" -> {
                        while(stack.last() != "(") {
                            postfix.add(stack.last())
                            stack.removeLast()
                        }
                        stack.removeLast()
                    }
                    precedence(item) > precedence(stack.last()) -> stack.add(item)
                    precedence(item) <= precedence(stack.last()) -> {
                        while(stack.isNotEmpty() && precedence(item) <= precedence(stack.last()) && stack.last() != "(") {
                            postfix.add(stack.last())
                            stack.removeLast()
                        }
                        stack.add(item)

                    }

                }
            }
            else -> throw (Exception("Invalid expression"))
        }
    }
    while (stack.isNotEmpty()) {
        val item = stack.last()
        if (item != "(" && item != ")") postfix.add(item)
        stack.removeLast()
    }
    return postfix
}

fun precedence(operator: String): Int {

    return when {
        """\*|/""".toRegex()matches(operator) -> 2
        """\+|-""".toRegex()matches(operator) -> 1
        else -> 0
    }
}

fun assignVariable(expression: String, variables: MutableMap<String, Int>) {

    val items = expression.split("=").map { it }.toMutableList()

    val variableName = items[0].replace(" ", "")
    val variableValue = items[1].replace(" ", "")
    when {
        """.*\d.*""".toRegex().matches(variableName) -> println("Invalid identifier")
        """-?\d+""".toRegex().matches(variableValue) -> variables[variableName] = variableValue.toInt()
        """[a-zA-Z]*""".toRegex().matches(variableValue) -> if (variables.containsKey(variableValue))  variables[variableName] = variables[variableValue]!! else println("Unknown variable")
        else -> println("Invalid assignment")
    }
}
