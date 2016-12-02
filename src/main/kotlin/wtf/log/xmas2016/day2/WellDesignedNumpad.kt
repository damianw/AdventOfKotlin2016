package wtf.log.xmas2016.day2

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/2/16
 * (C) 2016
 */

class WellDesignedNumpad : Numpad {

  private val grid = arrayOf(
      charArrayOf(' ', ' ', '1', ' ', ' '),
      charArrayOf(' ', '2', '3', '4', ' '),
      charArrayOf('5', '6', '7', '8', '9'),
      charArrayOf(' ', 'A', 'B', 'C', ' '),
      charArrayOf(' ', ' ', 'D', ' ', ' ')
  )

  private var row = 2
  private var col = 0

  private fun isValid(newRow: Int = row, newCol: Int = col) =
      newRow in 0..4 && newCol in 0..4 && grid[newRow][newCol] != ' '

  private fun updatePosition(newRow: Int = row, newCol: Int = col) {
    if (isValid(newRow, newCol)) {
      row = newRow
      col = newCol
    }
  }

  override val selectedButton: Char
    get() = grid[row][col]

  override fun moveLeft() {
    updatePosition(newCol = col - 1)
  }

  override fun moveUp() {
    updatePosition(newRow = row - 1)
  }

  override fun moveRight() {
    updatePosition(newCol = col + 1)
  }

  override fun moveDown() {
    updatePosition(newRow = row + 1)
  }

}
