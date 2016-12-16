package wtf.log.xmas2016.day15

import java.util.*

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/15/16
 * (C) 2016
 */

private val DISCS1: SortedSet<Disc> = sortedSetOf(
    Disc(index = 1, positionCount = 17, initialPosition = 5),
    Disc(index = 2, positionCount = 19, initialPosition = 8),
    Disc(index = 3, positionCount = 7, initialPosition = 1),
    Disc(index = 4, positionCount = 13, initialPosition = 7),
    Disc(index = 5, positionCount = 5, initialPosition = 1),
    Disc(index = 6, positionCount = 3, initialPosition = 0)
)

private val DISCS2: SortedSet<Disc> = (DISCS1 + Disc(index = 7, positionCount = 11, initialPosition = 0)).toSortedSet()

private class Disc(val index: Int, val positionCount: Int, val initialPosition: Int) : Comparable<Disc> {

  val timeToAlignment: Int = getTimeToPosition(-index)

  val positionOfAlignment: Int = getPositionAtTime(timeToAlignment)

  fun getPositionAtTime(t: Int): Int = (initialPosition + t) % positionCount

  fun getTimeToPosition(position: Int): Int {
    return ((position % positionCount - initialPosition) % positionCount + positionCount) % positionCount
  }

  fun getTimesToPosition(position: Int): Sequence<Int> {
    return generateSequence(getTimeToPosition(position)) { it + positionCount }
  }

  fun getTimesToAlignment(): Sequence<Int> = getTimesToPosition(-index)

  override fun compareTo(other: Disc): Int = this.positionCount.compareTo(other.positionCount)
}

private fun computeOptimalReleaseTime(discs: SortedSet<Disc>): Int {
  val max = discs.last()
  val rest = discs.headSet(max)
  return max.getTimesToAlignment().first { time ->
    rest.all { it.getPositionAtTime(time) == it.positionOfAlignment }
  }
}

fun day15(): Pair<String, String> {
  return computeOptimalReleaseTime(DISCS1).toString() to computeOptimalReleaseTime(DISCS2).toString()
}
