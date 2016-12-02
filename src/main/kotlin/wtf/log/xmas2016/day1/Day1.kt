package wtf.log.xmas2016.day1

import wtf.log.xmas2016.openResource
import java.awt.Point

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/1/16
 * (C) 2016
 */

data class Instruction(val rotation: Rotation, val distance: Int) {

  enum class Rotation {
    LEFT,
    RIGHT
  }

  companion object {

    fun parse(input: String): Instruction {
      val rotation = when (input[0]) {
        'L' -> Rotation.LEFT
        'R' -> Rotation.RIGHT
        else -> throw UnsupportedOperationException()
      }
      val distance = input.substring(1).toInt()
      return Instruction(rotation, distance)
    }

  }

}

class Player {

  private val position = Point(0, 0)

  var orientation: Orientation = Orientation.UP
    private set

  val taxicabDistance: Int
    get() = position.x + position.y

  fun move(instruction: Instruction) {
    val (rotation, distance) = instruction
    orientation = when (rotation) {
      Instruction.Rotation.LEFT -> orientation.left()
      Instruction.Rotation.RIGHT -> orientation.right()
    }
    when (orientation) {
      Orientation.LEFT -> position.translate(-distance, 0)
      Orientation.UP -> position.translate(0, distance)
      Orientation.RIGHT -> position.translate(distance, 0)
      Orientation.DOWN -> position.translate(0, -distance)
    }
  }

  enum class Orientation {
    LEFT,
    UP,
    RIGHT,
    DOWN;

    fun left(): Orientation = when (ordinal) {
      0 -> DOWN
      else -> values()[ordinal - 1]
    }

    fun right(): Orientation = when (ordinal) {
      3 -> LEFT
      else -> values()[ordinal + 1]
    }

  }

}

fun day1(): String {
  val player = Player()
  openResource("Day1.txt")
      .readText()
      .trim()
      .split(", ")
      .map(Instruction.Companion::parse)
      .forEach(player::move)
  return player.taxicabDistance.toString()
}
