package wtf.log.xmas2016.day2

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/2/16
 * (C) 2016
 */
class BasicNumpad : Numpad {

  var currentPosition = 4
    private set

  override val selectedButton: Char
    get() = (currentPosition + 1).toString().first()

  override fun moveLeft() {
    if (currentPosition % 3 != 0) {
      currentPosition -= 1
    }
  }

  override fun moveUp() {
    val newPosition = currentPosition - 3
    if (newPosition >= 0) {
      currentPosition = newPosition
    }
  }

  override fun moveRight() {
    if ((currentPosition + 1) % 3 != 0) {
      currentPosition += 1
    }
  }

  override fun moveDown() {
    val newPosition = currentPosition + 3
    if (newPosition <= 8) {
      currentPosition = newPosition
    }
  }

}
