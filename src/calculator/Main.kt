package calculator

fun main() {

    var input: String
    var variables = mutableMapOf<String, Int>()

    do {
        input = readln()

        when  {
            """""".toRegex().matches(input) -> continue
            """/exit""".toRegex().matches(input) -> println("Bye!")
            """/help""".toRegex().matches(input) -> println("The program calculates expressions like : 4 + 6 - 8, 2 - 3 - 4, and so on. It supports both unary and binary minus operators. The program accepts several same operators following each other.")
            """/\w+""".toRegex().matches(input) -> println("Unknown command")
            """[a-zA-Z]+""".toRegex().matches(input) -> if (variables.containsKey(input)) println(variables[input]) else println("Unknown variable")
            """\w*\s*=\s*\w*\s*""".toRegex().matches(input) -> assignVariable(input, variables)
            else -> {
                try {
                    println(calculateSum(input, variables))
                } catch (e: Exception) {
                    println(e.message)
                }
            }

        }

    } while (input != "/exit")
}

fun calculateSum(expression: String, variables: MutableMap<String, Int>): Int {

    val items = expression.split(" ").map { it }.toMutableList()
    val numbers = mutableListOf<Int>()
    var operator = "+"

    for (item in items) {
        when {
            """\++\d""" .toRegex().matches(item) -> if (operator == "+") numbers.add(item.replace("+", "").toInt()) else numbers.add(-1 * item.replace("+", "").toInt())
            """-?\d+""".toRegex().matches(item) -> if (operator == "+") numbers.add(item.toInt()) else numbers.add(-1 * item.toInt())
            """\w+""".toRegex().matches(item) -> if (variables.containsKey(item)) if (operator == "+") numbers.add(variables[item]!!) else numbers.add(-1 * variables[item]!!)
            """\++""".toRegex().matches(item) -> operator = "+"
            """-+""".toRegex().matches(item) -> operator = if (item.length % 2 == 0) "+" else "-"
            else -> throw (Exception("Invalid expression"))
        }
    }
    return numbers.sum()
}

fun assignVariable(expression: String, variables: MutableMap<String, Int>) {

    val items = expression.split("=").map { it }.toMutableList()

    val variableName = items[0].replace(" ", "")
    val variableValue = items[1].replace(" ", "")
    when {
        """.*\d.*""".toRegex().matches(variableName) -> println("Invalid identifier")
        """\d""".toRegex().matches(variableValue) -> variables[variableName] = variableValue.toInt()
        """[a-zA-Z]*""".toRegex().matches(variableValue) -> if (variables.containsKey(variableValue))  variables[variableName] = variables[variableValue]!! else println("Unknown variable")
        else -> println("Invalid assignment")
    }
}
