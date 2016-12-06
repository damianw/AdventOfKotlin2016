package wtf.log.xmas2016.day6

import wtf.log.xmas2016.openResource

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/6/16
 * (C) 2016
 */

fun List<String>.transpose(): List<String> = (0..first().lastIndex).map { c -> joinToString("") { it[c].toString() } }

fun String.count(c: Char) = count { it == c }

fun columns(): List<String> = openResource("Day6.txt").readLines().transpose()

fun part1(): String = columns().map { it.maxBy(it::count) }.joinToString("")

fun part2(): String = columns().map { it.minBy(it::count) }.joinToString("")

fun day6() = part1() to part2()
