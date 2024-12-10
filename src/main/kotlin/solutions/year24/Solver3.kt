package solutions.year24

class Solver3(private val input: String) {

    fun solveFirstPart(): Int {
        val regex = Regex("mul\\(\\d+,\\d+\\)")
        val matches = regex.findAll(input)
        var result = 0
        matches.map { parseMul(it.value) }
            .map { pair -> pair.first * pair.second }
            .forEach { result += it }
        return result
    }

    fun parseMul(expr: String): Pair<Int, Int> {
        var nums = expr.split("mul(")[1].split(")")[0].split(",")
        return Pair(nums[0].toInt(), nums[1].toInt())
    }

    fun solveSecondPart(): Int {
        val mulRegex = Regex("mul\\(\\d+,\\d+\\)")
        val doRegex = Regex("do\\(\\)")
        val dontRegex = Regex("don't\\(\\)")

        val matches = mulRegex.findAll(input)
        val doMatches = doRegex.findAll(input)
        val dontMatches = dontRegex.findAll(input)

        val matchList = matches.toMutableList()
        matchList.addAll(doMatches)
        matchList.addAll(dontMatches)
        matchList.sortBy { result -> result.range.first }
        return processMatchList(matchList)
    }

    fun processMatchList(matchList: List<MatchResult> ): Int {
        var result = 0
        var nextEnabled = true
        for (match in matchList) {
            if (match.value.contains("don't")) {
                nextEnabled = false
            } else if (match.value.contains("do")) {
                nextEnabled = true
            } else {
                if (nextEnabled) {
                    val pair = parseMul(match.value)
                    result += pair.first * pair.second
                }
            }
        }
        return result
    }
}