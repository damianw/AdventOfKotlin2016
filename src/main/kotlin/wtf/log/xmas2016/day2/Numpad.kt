package wtf.log.xmas2016.day2

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/2/16
 * (C) 2016
 */
interface Numpad {

  val selectedButton: Char

  fun moveLeft()
  fun moveUp()
  fun moveRight()
  fun moveDown()

}
