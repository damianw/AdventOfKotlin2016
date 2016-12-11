package wtf.log.xmas2016.day10

import com.google.common.cache.CacheBuilder
import com.google.common.cache.LoadingCache
import wtf.log.xmas2016.openResource
import wtf.log.xmas2016.util.build
import java.text.ParseException

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/10/16
 * (C) 2016
 */
private interface MicrochipSource {

  fun retrieve(): Int

  class Literal(val value: Int): MicrochipSource {
    override fun retrieve() = value
    override fun toString() = value.toString()
  }

  object Unassigned : MicrochipSource {
    override fun retrieve() = -1
    override fun toString() = "UNASSIGNED"
  }

}

private sealed class MicrochipSink(val id: Int) {

  abstract fun assign(source: MicrochipSource)

}

private class Robot(id: Int): MicrochipSink(id) {

  private var leftSource: MicrochipSource = MicrochipSource.Unassigned

  private var rightSource: MicrochipSource = MicrochipSource.Unassigned

  val leftValue: Int by lazy { leftSource.retrieve() }

  val rightValue: Int by lazy { rightSource.retrieve() }

  val min = object : MicrochipSource {
    override fun retrieve(): Int = Math.min(leftValue, rightValue)
    override fun toString(): String = "MIN from: ${this@Robot}"
  }

  val max = object : MicrochipSource {
    override fun retrieve(): Int = Math.max(leftValue, rightValue)
    override fun toString(): String = "MAX from: ${this@Robot}"
  }

  override fun assign(source: MicrochipSource) {
    when {
      leftSource == MicrochipSource.Unassigned -> leftSource = source
      rightSource == MicrochipSource.Unassigned -> rightSource = source
      else -> throw IllegalStateException("Robot $id has already been assigned to: ($leftSource, $rightSource)")
    }
  }

  override fun toString() = "Robot $id: ($leftValue, $rightValue)"

}

private class Output(id: Int) : MicrochipSink(id), MicrochipSource {

  private var source: MicrochipSource = MicrochipSource.Unassigned

  private val value: Int by lazy { source.retrieve() }

  override fun retrieve(): Int = value

  override fun assign(source: MicrochipSource) {
    this.source = source
  }

  override fun toString(): String = "Output $id: (${retrieve()})"

}

private class Factory {

  val robots: LoadingCache<Int, Robot> = CacheBuilder.newBuilder().build(::Robot)

  val outputs: LoadingCache<Int, Output> = CacheBuilder.newBuilder().build(::Output)

  override fun toString(): String {
    val allValues = robots.asMap().values + outputs.asMap().values
    return allValues.sortedBy(MicrochipSink::id).joinToString(separator = "\n", transform = Any::toString)
  }

}

private class Parser(val factory: Factory) {

  private val transfer = Regex("bot ([0-9]+) gives (low|high) to (bot|output) ([0-9]+) and (low|high) to (bot|output) ([0-9]+)")
  private val literal = Regex("value ([0-9]+) goes to (bot|output) ([0-9]+)")

  private fun parseSingleTransfer(transferType: String, source: Robot, sink: MicrochipSink) = when (transferType) {
    "low" -> sink.assign(source.min)
    "high" -> sink.assign(source.max)
    else -> throw UnsupportedOperationException("Unknown transfer type: $transferType")
  }

  private fun parseSink(sinkType: String, id: Int): MicrochipSink = when (sinkType) {
    "bot" -> factory.robots[id]
    "output" -> factory.outputs[id]
    else -> throw UnsupportedOperationException("Unknown sink type: $sinkType")
  }

  private fun parseTransfer(match: MatchResult.Destructured) {
    val (source, transferType1, sinkType1, sinkId1, transferType2, sinkType2, sinkId2) = match
    val sourceRobot = factory.robots[source.toInt()]
    parseSingleTransfer(transferType1, sourceRobot, parseSink(sinkType1, sinkId1.toInt()))
    parseSingleTransfer(transferType2, sourceRobot, parseSink(sinkType2, sinkId2.toInt()))
  }

  private fun parseLiteral(match: MatchResult.Destructured) {
    val (value, sinkType, sinkId) = match
    parseSink(sinkType, sinkId.toInt()).assign(MicrochipSource.Literal(value.toInt()))
  }

  fun parse(input: String) {
    transfer.matchEntire(input)?.destructured?.let { parseTransfer(it); return }
    literal.matchEntire(input)?.destructured?.let { parseLiteral(it); return }
    throw ParseException("Could not parse: $input", 0)
  }

}

fun day10(): Pair<String, String> {
  val factory = Factory()
  val parser = Parser(factory)
  openResource("Day10.txt").forEachLine(parser::parse)
  val part1Robot = factory.robots.asMap().values.find { robot ->
    (robot.leftValue == 61 && robot.rightValue == 17) || (robot.leftValue == 17 && robot.rightValue == 61)
  }!!
  val part2Product = (0..2).map(factory.outputs::get).fold(1) { c, output -> c * output!!.retrieve() }
  return part1Robot.id.toString() to part2Product.toString()
}
