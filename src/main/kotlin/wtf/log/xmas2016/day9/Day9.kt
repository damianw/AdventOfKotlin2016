package wtf.log.xmas2016.day9

import okio.BufferedSource
import okio.Sink
import wtf.log.xmas2016.openResourceAsSource
import wtf.log.xmas2016.util.bufferedSource

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/9/16
 * (C) 2016
 *
 * The fastest decompression tool of the best compression format out there.
 */

/**
 * Reads until a particular character is encountered. If it is found, the source is consumed up until that position
 * and this returns true. If it is not found, the source is fully consumed (if not already) and false is returned.
 */
fun BufferedSource.readUntil(sink: Sink, vararg chars: Char): Boolean {
  var index = -1L
  for (char in chars) {
    index = indexOf(char.toByte())
    if (index != -1L) break
  }

  val buffer = buffer()
  if (index == -1L) {
    val size = buffer.size()
    if (size != 0L) {
      sink.write(buffer, size)
      return false
    }
    return false
  }
  sink.write(buffer, index)
  skip(1)
  return true
}

private fun decompressUsing(decompressor: Decompressor): Long {
  return openResourceAsSource("Day9.txt").bufferedSource().use(decompressor::measureDecompressedSize)
}

fun day9() = decompressUsing(DecompressorV1).toString() to decompressUsing(DecompressorV2).toString()
