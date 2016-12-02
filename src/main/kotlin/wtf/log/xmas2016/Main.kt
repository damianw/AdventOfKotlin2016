package wtf.log.xmas2016

import com.beust.jcommander.IValueValidator
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import wtf.log.xmas2016.day1.day1
import java.lang.Exception
import kotlin.system.exitProcess

private val DAY_SOLUTIONS = listOf(::day1)

private object ProgramArguments {

  @Parameter(
      names = arrayOf("--help", "-h"),
      description = "Prints usage information",
      help = true
  )
  var help: Boolean = false

  class DayValidator : IValueValidator<List<Int>> {
    override fun validate(name: String, value: List<Int>) {
      val errors = value.filter { it !in 1..DAY_SOLUTIONS.size }.distinct()
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

  days.forEach { day ->
    val (part1, part2) = DAY_SOLUTIONS[day - 1]()
    println("========")
    println("Day $day")
    println("========")
    print("-> Part 1: ")
    println(part1)
    print("-> Part 2: ")
    println(part2)
    println()
  }

}
