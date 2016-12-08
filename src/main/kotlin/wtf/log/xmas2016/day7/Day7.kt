package wtf.log.xmas2016.day7

import wtf.log.xmas2016.openResource

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/7/16
 * (C) 2016
 */
data class IpV7Address(val parts: List<String>, val hypernets: List<String>) {

  val supportsTls: Boolean = parts.any { it.containsAbba() } && hypernets.none { it.containsAbba() }

  val supportsSsl: Boolean = parts.any { part ->
    part.abaSequence()
        .map { it.invertAba() }
        .any { aba ->
          hypernets.any { it.abaSequence().contains(aba) }
        }
  }

  companion object {

    private fun String.isAbba(): Boolean = let { (a, b, c, d) -> b == c && a == d && a != b }

    private fun String.containsAbba() = partials(4).any { it.isAbba() }

    private fun String.isAba(): Boolean = let { (a, b, c) -> a == c && a != b }

    private fun String.invertAba(): String = let { (a, b) -> "$b$a$b" }

    private fun String.abaSequence() = partials(3).filter { it.isAba() }

    fun parse(address: String): IpV7Address {
      val firstSplit = address.split('[')
      val firstPart = firstSplit.first()
      val rest = firstSplit.drop(1).map { it.split(']') }
      val hypernets = rest.map { it.first() }
      val otherParts = rest.map { (_, p) -> p }
      return IpV7Address(otherParts + firstPart, hypernets)
    }

  }

}

private operator fun String.component1() = get(0)
private operator fun String.component2() = get(1)
private operator fun String.component3() = get(2)
private operator fun String.component4() = get(3)

private fun String.partials(size: Int): Sequence<String> = when {
  length < size -> emptySequence()
  else -> (0..(length - size)).asSequence().map { substring(it, it + size) }
}

private fun readAddresses() = openResource("Day7.txt").useLines { lines ->
  lines.map(IpV7Address.Companion::parse).toList()
}

fun part1(): Int = readAddresses().count(IpV7Address::supportsTls)

fun part2(): Int = readAddresses().count(IpV7Address::supportsSsl)

fun day7() = part1().toString() to part2().toString()
