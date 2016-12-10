package wtf.log.xmas2016

import okio.Source
import wtf.log.xmas2016.util.asSource
import java.io.BufferedReader

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/1/16
 * (C) 2016
 */

/**
 * Convenience function for opening a resource as a [BufferedReader]
 */
fun openResource(name: String): BufferedReader = ClassLoader.getSystemResourceAsStream(name).bufferedReader()

/**
 * Convenience function for opening a resource as a [Source]
 */
fun openResourceAsSource(name: String): Source = ClassLoader.getSystemResource(name).openStream().asSource()

