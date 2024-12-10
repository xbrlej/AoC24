package utils

import java.io.File

object InputUtils {
    fun parseInputFileByRow(file: File, spaces: Boolean): Array<Array<String>> {
        var lines = file.readLines()
        if (spaces) {
            return lines.map { it.split("\\s+".toRegex()).toTypedArray() }.toTypedArray()
        } else {
            return lines.map {it.toCharArray().map { it.toString() }.toTypedArray()}.toTypedArray()
        }
    }

    fun parseInputFileByColumn(file: File, spaces: Boolean): Array<Array<String>> {
        var parsedByRow = parseInputFileByRow(file, spaces)
        return invertMatrix(parsedByRow)
    }

    inline fun<reified T> invertMatrix(matrix : Array<Array<T>>): Array<Array<T>> {
        var inverted = List<ArrayList<T>> (matrix[0].size) { ArrayList<T>() }
        for (j in matrix[0].indices) {
            for (i in matrix.indices) {
                inverted.get(j).add(matrix[i][j])
            }
        }
        return inverted.map { it.toTypedArray() }.toTypedArray()
    }

    fun readFile(file: File): String {
        return file.readText();
    }
}