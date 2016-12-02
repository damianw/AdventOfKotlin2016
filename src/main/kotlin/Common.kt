import okio.BufferedSource
import okio.Okio

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/1/16
 * (C) 2016
 */

/**
 * Convenience function for opening a resource as a [BufferedSource]
 */
fun openResource(name: String): BufferedSource = Okio.buffer(Okio.source(ClassLoader.getSystemResourceAsStream(name)))
