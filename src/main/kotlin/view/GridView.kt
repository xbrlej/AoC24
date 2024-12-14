package view

import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JComponent
import javax.swing.JFrame

class GridView(val rows: Int, val cols: Int, var locations: Set<Pair<Int, Int>>, var i: Int): JFrame() {
    val cellSize: Int
    init {
        title = "Iteration $i"
        setSize(1500, 1500)
        isVisible = true
        cellSize = 10
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    override fun paint(g: Graphics?) {
        for (i in 0..<rows) {
            for (j in 0..<cols) {
                if (g != null) {
                    g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize)
                    if (Pair(i, j) in locations) {
                        g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize)
                    }
                }
            }
        }
    }

    fun reset(i: Int) {
        this.i = i
        title = "Iteration $i"
        this.isVisible = false
        this.isVisible = true
    }

}