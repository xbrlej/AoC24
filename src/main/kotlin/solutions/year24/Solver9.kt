package solutions.year24

class Solver9(private val inputArray: IntArray) {

    fun solveFirstPart(): Long {
        val memory = createMemory()
        rearrangeMemoryFragmented(memory)
        return calculateChecksum(memory)
    }

    fun solveSecondPart(): Long {
        val blocks = rearrangeMemoryWithoutFragments(inputArray)
        return calculateChecksumBlocks(blocks)
    }

    fun createMemory(): Array<Int?> {
        val memory = mutableListOf<Int?>()
        var emptyBlock = false
        var counter = 0
        for (elem in inputArray) {
            for (i in 0 until elem) {
                if (emptyBlock) {
                    memory.add(null)
                } else {
                    memory.add(counter)
                }
            }
            if (!emptyBlock) {
                counter++
            }
            emptyBlock = !emptyBlock
        }
        return memory.toTypedArray()
    }

    fun rearrangeMemoryFragmented(memory: Array<Int?>) {
        var forwardPointer = 0
        var backwardPointer = memory.size - 1

        while (forwardPointer < backwardPointer) {
            if (memory[forwardPointer] == null) {
                if (memory[backwardPointer] != null) {
                    memory[forwardPointer] = memory[backwardPointer]
                    memory[backwardPointer] = null
                } else {
                    backwardPointer -= 1
                }
            } else {
                forwardPointer += 1
            }
        }
    }

    fun rearrangeMemoryWithoutFragments(compact: IntArray): List<Triple<Int, Boolean, Int>> {
        // Blocks = [(fileId, isFile, fileSize), ...]
        var blocks = compact.withIndex().map {Triple(it.index / 2, it.index % 2 == 0, it.value)}.toMutableList()
        val fileBlocks = blocks.filter { it.second }
        for(i in fileBlocks.size-1 downTo 0) {
            val fileBlock = fileBlocks[i]
            val fileIndex = blocks.indexOf(fileBlock)
            for ((spaceIndex, spaceBlock) in blocks.withIndex()) {
                if (fileIndex < spaceIndex) {
                    break
                }
                if (spaceBlock.second) {
                    continue
                }
                if (spaceBlock.third >= fileBlock.third) {
                    blocks = fitBlock(blocks, spaceBlock, fileBlock, fileIndex, spaceIndex)
                    break
                }
            }
        }
        return blocks
    }

    fun fitBlock(blocks: MutableList<Triple<Int, Boolean, Int>>, spaceBlock: Triple<Int, Boolean, Int>, fileBlock: Triple<Int, Boolean, Int>, fileIndex: Int, spaceIndex: Int): MutableList<Triple<Int, Boolean, Int>> {
        if (spaceIndex > fileIndex) {
            return blocks
        }
         // Replace file block with empty mem
        blocks.removeAt(fileIndex)
        blocks.add(fileIndex, Triple(fileBlock.first, false, fileBlock.third))

        // Remove space block, add file block and potential padding block
        blocks.removeAt(spaceIndex)
        blocks.add(spaceIndex, fileBlock)
        if (fileBlock.third < spaceBlock.third) {
            blocks.add(spaceIndex+1, Triple(spaceBlock.first, false, spaceBlock.third - fileBlock.third))
        }
        return blocks
    }

    fun calculateChecksumBlocks(blocks: List<Triple<Int, Boolean, Int>>): Long {
        var idx = 0
        var result = 0L
        for (block in blocks) {
            if (block.second) {
                result += IntRange(idx, idx+block.third-1).sum().toLong() * block.first.toLong()
            }
            idx += block.third
        }
        return result
    }

    fun calculateChecksum(memory: Array<Int?>): Long {
        var cs = 0L
        for ((i, elem) in memory.withIndex()) {
            if (elem == null) {
                continue
            }
            cs += i * elem
        }
        return cs
    }
}