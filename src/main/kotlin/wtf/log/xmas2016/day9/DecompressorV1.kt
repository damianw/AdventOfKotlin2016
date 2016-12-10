package wtf.log.xmas2016.day9

import okio.Buffer
import okio.Sink
import okio.Source
import wtf.log.xmas2016.util.bufferedSink
import wtf.log.xmas2016.util.bufferedSource

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/9/16
 * (C) 2016
 *
 * Fully decompresses the input file and measures the output size.
 */
object DecompressorV1 : Decompressor {

  override fun decompress(source: Source, sink: Sink) {
    val bufferedSource = source.bufferedSource()
    val bufferedSink = sink.bufferedSink()
    val buffer = Buffer()
    while (bufferedSource.readUntil(bufferedSink, '(', '\n')) {
      if (bufferedSource.exhausted()) break
      buffer.clear()
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
    bufferedSink.flush()
  }

}
