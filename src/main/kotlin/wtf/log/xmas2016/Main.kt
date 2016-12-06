package wtf.log.xmas2016

import com.beust.jcommander.IValueValidator
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import wtf.log.xmas2016.day1.day1
import wtf.log.xmas2016.day2.day2
import wtf.log.xmas2016.day3.day3
import wtf.log.xmas2016.day5.day5
import java.lang.Exception
import kotlin.system.exitProcess

private val DAY_SOLUTIONS = listOf(::day1, ::day2, ::day3, null, ::day5)

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
    val (part1, part2) = function()
    print("-> Part 1: ")
    println(part1)
    print("-> Part 2: ")
    println(part2)
    println()
  }

}
