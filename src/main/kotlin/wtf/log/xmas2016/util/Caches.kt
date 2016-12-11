package wtf.log.xmas2016.util

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/10/16
 * (C) 2016
 */

/**
 * Convenience for constructing a [CacheLoader]
 */
inline fun <K : Any, V : Any> CacheLoader(crossinline load: (K) -> V): CacheLoader<K, V> = object : CacheLoader<K, V>() {
  override fun load(key: K): V = load(key)
}

/**
 * Convenience for building a Guava cache
 */
inline fun <K : Any, V : Any> CacheBuilder<Any?, Any?>.build(crossinline load: (K) -> V): LoadingCache<K, V> {
  return build(CacheLoader(load))
}
