package wtf.log.xmas2016.day13

import java.util.*

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/13/16
 * (C) 2016
 */

/**
 * Some real wizardry right here. Uglier because Kotlin doesn't have "real" bitwise operators or octal literals.
 * HAKMEM solution: https://blogs.msdn.microsoft.com/jeuge/2005/06/08/bit-fiddling-3/
 */
private val Int.bitCount: Int
  get() {
    val count = this - ((this ushr 1) and 0xdb6db6db.toInt()) - ((this ushr 2) and 0x49249249)
    return (count + (count shr 3) and 0xc71c71c7.toInt()) % 63
  }

private data class Point(val x: Int, val y: Int)

private data class Distance(val to: Point, val length: Int) : Comparable<Distance> {
  override fun compareTo(other: Distance): Int = this.length.compareTo(other.length)
}

private class Room(val seed: Int) {

  operator fun get(x: Int, y: Int): Boolean {
    return x >= 0 && y >= 0 && (seed + ((x * x) + (3 * x) + (2 * x * y) + y + (y * y))).bitCount % 2 == 0
  }

  operator fun get(point: Point): Boolean = get(point.x, point.y)

  fun openNeighborsOf(point: Point): Sequence<Point> = point.let { (x, y) ->
    sequenceOf(Point(x - 1, y), Point(x + 1, y), Point(x, y - 1), Point(x, y + 1)).filter(this::get)
  }

  /**
   * Dijkstra knows all. Returns the shortest path (including the starting point).
   */
  fun computeShortestPath(from: Point, to: Point): List<Point> {
    val queue = PriorityQueue<Distance>().apply {
      add(Distance(from, 0))
    }
    val dist = mutableMapOf<Point, Int>(from to 0)
    val prev = mutableMapOf<Point, Point>()

    var current = from
    while (current != to && queue.isNotEmpty()) {
      current = queue.remove().to

      for (neighbor in openNeighborsOf(current)) {
        val alt = dist[current]?.let { it + 1 } ?: Int.MAX_VALUE
        if (alt < dist[neighbor] ?: Int.MAX_VALUE) {
          dist[neighbor] = alt
          prev[neighbor] = current
          queue.add(Distance(neighbor, alt))
        }
      }
    }

    return generateSequence(current, prev::get).toList().asReversed()
  }

  fun findAccessiblePoints(from: Point, range: Int): Set<Point> {
    val points = hashMapOf<Point, Int>()
    findAccessiblePointsImpl(from, points, range)
    return points.keys
  }

  private fun findAccessiblePointsImpl(point: Point, seen: MutableMap<Point, Int>, remaining: Int) {
    if (seen[point]?.let { it >= remaining } ?: false) return
    seen[point] = remaining
    if (remaining == 0) return
    val furtherRemaining = remaining - 1

    for (neighbor in openNeighborsOf(point)) {
      findAccessiblePointsImpl(neighbor, seen, furtherRemaining)
    }
  }

  override fun toString(): String = toString(9, 6)

  /**
   * Treat yourself to some nice visuals. Longest function in here. But damn it looks nice.
   */
  fun toString(maxX: Int, maxY: Int, mark: Set<Point> = emptySet()): String = buildString(maxX * maxY * 2) {
    append("┌──")
    for (x in 0..maxX) {
      append("┬──")
    }
    append("┐\n│  │")
    for (x in 0..maxX) {
      val labelX = x.toString()
      if (labelX.length == 1) {
        append(' ')
      }
      append(x)
      append('│')
    }
    append("\n├──")
    for (y in 0..maxY) {
      for (x in 0..maxX) {
        append("┼──")
      }
      append("┤\n│")
      val labelY = y.toString()
      append(labelY)
      if (labelY.length == 1) {
        append(' ')
      }
      append('│')
      for (x in 0..maxX) {
        if (get(x, y)) {
          append(if (Point(x, y) in mark) "XX" else "  ")
        } else {
          append(if (Point(x, y) in mark) "??" else "██")
        }
        append("│")
      }
      append('\n')
      if (y != maxY) {
        append("├──")
      }
    }
    append("└──")
    for (x in 0..maxX) {
      append("┴──")
    }
    append("┘\n")
  }

}

fun part1(): Int = Room(1350).computeShortestPath(Point(1, 1), Point(31, 39)).size - 1

fun part2(): Int = Room(1350).findAccessiblePoints(Point(1, 1), 50).size

fun day13(): Pair<String, String> = part1().toString() to part2().toString()

