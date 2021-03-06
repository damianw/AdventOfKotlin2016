package wtf.log.xmas2016.day8

import org.funktionale.partials.partially1
import org.funktionale.partials.partially2
import wtf.log.xmas2016.openResource
import java.text.ParseException

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/8/16
 * (C) 2016
 */

class CharGrid(val width: Int, val height: Int) : Iterable<Char> {

  private val matrix = CharArray(width * height) { ' ' }

  private fun wrap(v: Int, size: Int): Int {
    val mod = v % size
    return if (mod < 0) size + mod else mod
  }

  private fun wrapY(y: Int): Int = wrap(y, height)

  private fun wrapX(x: Int): Int = wrap(x, width)

  private fun indexOf(x: Int, y: Int): Int = wrapY(y) * width + wrapX(x)

  operator fun get(x: Int, y: Int) = matrix[indexOf(x, y)]

  operator fun set(x: Int, y: Int, c: Char) {
    matrix[indexOf(x, y)] = c
  }

  override fun iterator(): Iterator<Char> = matrix.iterator()

  override fun toString() = buildString(capacity = matrix.size + 2 * (width + height)) {
    append('┌')
    for (x in 1..width) append('─')
    append("┐\n")
    for (y in 0 until height) {
      append('│')
      for (x in 0 until width) append(get(x, y))
      append("│\n")
    }
    append('└')
    for (x in 1..width) append('─')
    append('┘')
  }

}

class Screen(val width: Int = 50, val height: Int = 6) {

  private val grid = CharGrid(width, height)

  fun rect(width: Int, height: Int) {
    for (x in 0 until width) {
      for (y in 0 until height) {
        grid[x, y] = '█'
      }
    }
  }

  private fun rotate(amount: Int, bound: Int, get: (Int) -> Char, set: (Int, Char) -> Unit, start: () -> Char) {
    for (pass in 0 until amount) {
      var last = start()
      for (cell in 0 until bound) {
        val current = get(cell)
        set(cell, last)
        last = current
      }
    }
  }

  fun rotateColumn(x: Int, amount: Int) {
    rotate(amount, height, grid::get.partially1(x), grid::set.partially1(x)) { grid[x, -1] }
  }

  fun rotateRow(y: Int, amount: Int) {
    rotate(amount, width, grid::get.partially2(y), grid::set.partially2(y)) { grid[-1, y] }
  }

  fun countIlluminated(): Int = grid.count { it == '█' }

  override fun toString(): String = grid.toString()

}

private sealed class Instruction {

  data class Rect(val width: Int, val height: Int) : Instruction()

  data class RotateRow(val y: Int, val amount: Int) : Instruction()

  data class RotateColumn(val x: Int, val amount: Int) : Instruction()

  companion object {

    fun parse(input: String): Instruction = when {
      input.startsWith("rect ") -> input.substring(5).split('x').let { (width, height) ->
        Rect(width.toInt(), height.toInt())
      }
      input.startsWith("rotate r") -> input.substring(13).split(" by ").let { (y, amount) ->
        RotateRow(y.toInt(), amount.toInt())
      }
      input.startsWith("rotate c") -> input.substring(16).split(" by ").let { (x, amount) ->
        RotateColumn(x.toInt(), amount.toInt())
      }
      else -> throw ParseException("Could not parse: $input", 0)
    }

  }

}

private inline fun forEachInstruction(action: (Instruction) -> Unit) {
  return openResource("Day8.txt").useLines { lines ->
    lines.map { Instruction.parse(it) }.forEach(action)
  }
}

fun day8(): Pair<String, String> {
  val screen = Screen()
  forEachInstruction { instruction ->
    when (instruction) {
      is Instruction.Rect -> instruction.let { (width, height) -> screen.rect(width, height) }
      is Instruction.RotateRow -> instruction.let { (y, amount) -> screen.rotateRow(y, amount) }
      is Instruction.RotateColumn -> instruction.let { (x, amount) -> screen.rotateColumn(x, amount) }
    }
  }
  val part1 = screen.countIlluminated()
  val part2 = screen.toString()
  return part1.toString() to part2
}
