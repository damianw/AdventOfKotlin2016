package wtf.log.xmas2016.day3

import wtf.log.xmas2016.openResource

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/3/16
 * (C) 2016
 */
fun isValidTriangle(values: List<Int>): Boolean {
  val combinations = listOf(
      0 to (1 to 2),
      1 to (0 to 2),
      2 to (0 to 1)
  )
  return combinations.all { (s, x) -> values[s] < values[x.first] + values[x.second] }
}

fun <T> List<T>.toChunks(chunkSize: Int): List<List<T>> {
  val result = arrayListOf<List<T>>()
  var chunk = arrayListOf<T>()
  forEachIndexed { i, element ->
    if (i != 0 && i % chunkSize == 0) {
      result.add(chunk)
      chunk = arrayListOf()
    }
    chunk.add(element)
  }
  result.add(chunk)
  return result
}

fun <T> List<List<T>>.transpose(): List<List<T>> = (0..first().lastIndex).map { c -> map { it[c] } }

fun readRows(): List<List<Int>> = openResource("Day3.txt").useLines { lines ->
  lines.map { it.trim().split("\\s+".toRegex()).map(String::toInt) }.toList()
}

fun day3(): Pair<String, String> = part1().toString() to part2().toString()

fun part1(): Int = readRows().count(::isValidTriangle)

fun part2(): Int = readRows().transpose().map { it.toChunks(3) }.flatten().count(::isValidTriangle)
