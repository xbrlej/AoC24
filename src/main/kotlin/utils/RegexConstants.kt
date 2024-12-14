package utils

object RegexConstants {

    val POSITIVE_INTEGERS = Regex("\\d+")
    val NEGATIVE_INTEGERS = Regex("-\\d+")
    val INTEGERS = Regex("-?\\d+")
    val POSITIVE_NUMBER = Regex("\\d*\\.?\\d+")
    val NEGATIVE_NUMBER = Regex("-\\d*\\.?\\d+")
    val NUMBER = Regex("-?\\d*\\.{0,1}\\d+")
}