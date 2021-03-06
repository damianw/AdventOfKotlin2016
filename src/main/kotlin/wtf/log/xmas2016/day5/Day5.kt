package wtf.log.xmas2016.day5

import java.math.BigInteger
import java.security.MessageDigest

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/5/16
 * (C) 2016
 */
val DIGEST = MessageDigest.getInstance("MD5")!!

fun String.md5() = BigInteger(1, DIGEST.digest(this.toByteArray())).toString(16).let { string ->
  buildString {
    (string.length until 32).forEach { append('0') }
    append(string)
  }
}

fun hashSequence(input: String): Sequence<String> {
  return (1..Int.MAX_VALUE).asSequence()
      .map { (input + it).md5() }
      .filter { it.startsWith("00000") }
}

const val INPUT = "ffykfhsq"

fun part1(): String {
  return hashSequence(INPUT)
      .take(8)
      .map { it[5] }
      .joinToString("")
}

fun part2(): String {
  val password = CharArray(8)
  val sequence = hashSequence(INPUT)
      .filter { it[5].toInt() - 48 in 0..password.lastIndex }
      .map { (it[5].toInt() - 48) to it[6] }
  for ((position, char) in sequence) {
    if (password[position] == 0.toChar()) {
      password[position] = char
      if (password.all { it != 0.toChar() }) {
        break
      }
    }
  }
  return String(password)
}

fun day5() = part1() to part2()
