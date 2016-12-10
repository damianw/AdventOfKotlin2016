package wtf.log.xmas2016.day9

import com.google.common.io.ByteStreams
import com.google.common.io.CountingOutputStream
import okio.Sink
import okio.Source
import wtf.log.xmas2016.util.asSink

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/9/16
 * (C) 2016
 */
interface Decompressor {

  fun decompress(source: Source, sink: Sink)

  /**
   * Default implementation is "dumb" -- just measure the output size. Effective for V1, not so much for V2.
   */
  fun measureDecompressedSize(source: Source): Long {
    val output = CountingOutputStream(ByteStreams.nullOutputStream())
    return output.asSink().use { sink ->
      decompress(source, sink)
      output.count
    }
  }

}
