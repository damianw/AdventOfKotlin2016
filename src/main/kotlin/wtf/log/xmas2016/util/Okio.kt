package wtf.log.xmas2016.util

import okio.Okio
import okio.Sink
import okio.Source
import java.io.InputStream
import java.io.OutputStream

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/9/16
 * (C) 2016
 */

/**
 * Converts an [OutputStream] to a [Sink]
 */
fun OutputStream.asSink() = Okio.sink(this)

/**
 * Converts an [InputStream] to a [Source]
 */
fun InputStream.asSource() = Okio.source(this)

/**
 * Buffers a [Source]
 */
fun Source.bufferedSource() = Okio.buffer(this)

/**
 * Buffers a [Sink]
 */
fun Sink.bufferedSink() = Okio.buffer(this)
