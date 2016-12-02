package wtf.log.xmas2016.day2

import wtf.log.xmas2016.openResource

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/2/16
 * (C) 2016
 */
fun runNumpad(numpad: Numpad): String {
  val solution = StringBuilder()
  openResource("Day2.txt").forEachLine { line ->
    line.forEach { c ->
      when (c) {
        'L' -> numpad.moveLeft()
        'U' -> numpad.moveUp()
        'R' -> numpad.moveRight()
        'D' -> numpad.moveDown()
      }
    }
    solution.append(numpad.selectedButton)
  }
  return solution.toString()
}

fun day2(): Pair<String, String> = runNumpad(BasicNumpad()) to runNumpad(WellDesignedNumpad())
