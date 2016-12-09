package wtf.log.xmas2016

import com.beust.jcommander.IValueValidator
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import rx.Observable
import rx.Subscription
import wtf.log.xmas2016.day1.day1
import wtf.log.xmas2016.day2.day2
import wtf.log.xmas2016.day3.day3
import wtf.log.xmas2016.day5.day5
import wtf.log.xmas2016.day6.day6
import wtf.log.xmas2016.day7.day7
import wtf.log.xmas2016.day8.day8
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime

private val DAY_SOLUTIONS = listOf(::day1, ::day2, ::day3, null, ::day5, ::day6, ::day7, ::day8)

private object ProgramArguments {

  @Parameter(
      names = arrayOf("--help", "-h"),
      description = "Prints usage information",
      help = true
  )
  var help: Boolean = false

  class DayValidator : IValueValidator<List<Int>> {
    override fun validate(name: String, value: List<Int>) {
      val errors = value.filter { DAY_SOLUTIONS.getOrNull(it - 1) == null }.distinct()
      // the lesson here is: never build sentences in code, kids
      when (errors.size) {
        0 -> return
        1 -> throw ParameterException("I haven't implemented day ${errors.single()}!")
        2 -> throw ParameterException("I haven't implemented days ${errors[0]} or ${errors[1]}!")
        else -> {
          val joined = errors.joinToString(limit = errors.lastIndex, truncated = "or ${errors.last()}")
          throw ParameterException("I haven't implemented days $joined!")
        }
      }
    }
  }

  @Parameter(
      names = arrayOf("--days", "-d"),
      description = "Days of the advent calendar to solve",
      validateValueWith = DayValidator::class
  )
  var days: List<Int>? = null

}

private object Spinner {

  private val spinBars = charArrayOf('-', '\\', '|', '/', '-', '\\', '|', '/')

  private var subscription: Subscription? = null

  fun start() {
    subscription?.unsubscribe()
    subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
        .map { spinBars[it.toInt() % spinBars.size] }
        .subscribe { print("\r$it ") }
  }

  fun stop() {
    subscription?.unsubscribe()
    print('\r')
  }

}

private inline fun <T> measureSeconds(block: () -> T): Pair<T, Long> {
  var result: T? = null
  val time = measureNanoTime {
    result = block()
  }
  return (result as T) to (time / 1000000000)
}

private fun String.indent(amount: Int): String = buildString {
  for (i in 0 until amount) {
    append(' ')
  }
  append(this@indent)
}

private fun printIndented(header: String, input: String) {
  print(header)
  val lines = input.lines()
  println(lines.first())
  lines.drop(1).map { it.indent(header.length) }.forEach(::println)
}

fun main(args: Array<String>) {
  val commander = JCommander(ProgramArguments).apply {
    setProgramName("AdventOfKotlin2016")
  }

  fun exitWithUsage(message: String? = null): Nothing {
    message?.let { System.err.println("[Error] $it") }
    commander.usage()
    exitProcess(1)
  }

  try {
    commander.parse(*args)
  } catch (e: Exception) {
    exitWithUsage(e.message)
  }

  if (ProgramArguments.help) {
    exitWithUsage()
  }

  val days = ProgramArguments.days ?: 1..DAY_SOLUTIONS.size

  for (day in days) {
    val function = DAY_SOLUTIONS[day - 1] ?: continue
    println("========")
    println("Day $day")
    println("========")
    Spinner.start()
    val (result, time) = measureSeconds(function)
    val (part1, part2) = result
    Spinner.stop()
    println("-> Time elapsed: $time seconds")
    printIndented("-> Part 1: ", part1)
    printIndented("-> Part 2: ", part2)
    println()
  }

}
