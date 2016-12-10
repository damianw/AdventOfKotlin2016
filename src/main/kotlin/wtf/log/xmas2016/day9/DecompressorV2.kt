package wtf.log.xmas2016.day9

import okio.*
import wtf.log.xmas2016.util.bufferedSink
import wtf.log.xmas2016.util.bufferedSource

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/9/16
 * (C) 2016
 */
object DecompressorV2 : Decompressor {

  /**
   * Warning: will probably produce very large output and take a very long time because it is very dumb.
   * Luckily it doesn't actually use a lot of memory... you'll probably want to point the sink straight at a file though.
   * #FirstTry
   */
  override fun decompress(source: Source, sink: Sink) {
    val bufferedSource = source.bufferedSource()
    val bufferedSink = sink.bufferedSink()
    val compressedBuffer = Buffer()
    var queueBuffer = Buffer()
    var nextQueueBuffer = Buffer()
    while (true) {
      val fromBuffer = queueBuffer.readUntil(compressedBuffer, '(', '\n')
      if (fromBuffer) {
        bufferedSink.writeAll(compressedBuffer)
        decompressSegment(queueBuffer, nextQueueBuffer)
        nextQueueBuffer.writeAll(queueBuffer)
        val previousQueueBuffer = queueBuffer
        queueBuffer = nextQueueBuffer
        nextQueueBuffer = previousQueueBuffer
        continue
      } else if (queueBuffer.exhausted()) {
        bufferedSink.writeAll(compressedBuffer)
      }
      bufferedSink.flush()

      val fromSource = bufferedSource.readUntil(compressedBuffer, '(', '\n')
      if (fromSource) {
        bufferedSink.writeAll(compressedBuffer)
        decompressSegment(bufferedSource, queueBuffer)
      } else if (bufferedSource.exhausted()) {
        bufferedSink.writeAll(bufferedSource)
        break
      }
      bufferedSink.flush()
    }
    bufferedSink.flush()
  }

  private fun decompressSegment(bufferedSource: BufferedSource, bufferedSink: BufferedSink) {
    if (bufferedSource.exhausted()) return
    val buffer = Buffer()
    bufferedSource.readUntil(buffer, 'x')
    val length = buffer.readUtf8().toLong()
    bufferedSource.readUntil(buffer, ')')
    val count = buffer.readUtf8().toInt()
    bufferedSource.readFully(buffer, length)
    val sinkBuffer = bufferedSink.buffer()
    for (i in 1..count) {
      buffer.copyTo(sinkBuffer, 0L, length)
    }
  }

  /**
   * The "efficient" way to measure size. :)
   */
  override fun measureDecompressedSize(source: Source): Long {
    return decompressImpl(source.bufferedSource())
  }


  private fun BufferedSource.countUntilNextCompressedSequence(): Long {
    val index = indexOf('('.toByte())

    if (index == -1L) {
      val buffer = buffer()
      val size = buffer.size()
      val endsWithLineBreak = size > 0 && indexOf('\n'.toByte()) == size - 1
      buffer.skip(size)
      return if (endsWithLineBreak) size - 1 else size
    }
    skip(index + 1)
    return index
  }

  private fun decompressImpl(bufferedSource: BufferedSource): Long {
    val precedingSegmentLength = bufferedSource.countUntilNextCompressedSequence()
    if (bufferedSource.exhausted()) return precedingSegmentLength
    val buffer = Buffer()
    bufferedSource.readUntil(buffer, 'x')
    val length = buffer.readUtf8().toLong()
    bufferedSource.readUntil(buffer, ')')
    val count = buffer.readUtf8().toInt()
    bufferedSource.readFully(buffer, length)
    val currentSegmentLength = decompressImpl(buffer) * count
    val remainingLength = decompressImpl(bufferedSource)
    return precedingSegmentLength + currentSegmentLength + remainingLength
  }

}
