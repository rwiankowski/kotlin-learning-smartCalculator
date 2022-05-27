package calculator

fun main() {

    var input: String

    do {
        input = readln()

        when  {
            """""".toRegex().matches(input) -> continue
            """/exit""".toRegex().matches(input) -> println("Bye!")
            """/help""".toRegex().matches(input) -> println("The program calculates expressions like : 4 + 6 - 8, 2 - 3 - 4, and so on. It supports both unary and binary minus operators. The program accepts several same operators following each other.")
            """/\w+""".toRegex().matches(input) -> println("Unknown command")
            else -> {
                try {
                    println(calculateSum(input))
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    } while (input != "/exit")
}

fun calculateSum(expression: String): Int {

    val items = expression.split(" ").map { it }.toMutableList()
    val numbers = mutableListOf<Int>()
    var operator = "+"

    for (item in items) {
        when {
            """\++\d""" .toRegex().matches(item) -> if (operator == "+") numbers.add(item.replace("+", "").toInt()) else numbers.add(-1 * item.replace("+", "").toInt())
            """-?\d+""".toRegex().matches(item) -> if (operator == "+") numbers.add(item.toInt()) else numbers.add(-1 * item.toInt())
            """\++""".toRegex().matches(item) -> operator = "+"
            """-+""".toRegex().matches(item) -> operator = if (item.length % 2 == 0) "+" else "-"
            else -> throw (Exception("Invalid expression"))
        }
    }
    return numbers.sum()
}
