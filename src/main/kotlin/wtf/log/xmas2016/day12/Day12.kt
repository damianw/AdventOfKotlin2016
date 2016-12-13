package wtf.log.xmas2016.day12

import wtf.log.xmas2016.openResource
import java.text.ParseException

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/12/16
 * (C) 2016
 */

sealed class Instruction {

  data class Copy(val from: Operand, val to: Operand.Register) : Instruction()

  data class Increment(val register: Operand.Register) : Instruction()

  data class Decrement(val register: Operand.Register) : Instruction()

  data class ConditionalJump(val condition: Operand, val offset: Int) : Instruction()

  sealed class Operand {

    data class Literal(val value: Int) : Operand()

    data class Register(val index: Int) : Operand()

  }

  companion object {

    private val REGISTERS = charArrayOf('a', 'b', 'c', 'd')

    private fun parseRegister(input: String) = Operand.Register(REGISTERS.indexOf(input.single()))

    private fun parseOperand(input: String): Operand = when {
      input.singleOrNull()?.let(REGISTERS::contains) ?: false -> parseRegister(input)
      else -> Operand.Literal(input.toInt())
    }

    fun parse(input: String): Instruction = input.split(' ').let { split ->
      when (split[0]) {
        "cpy" -> Copy(parseOperand(split[1]), parseRegister(split[2]))
        "inc" -> Increment(parseRegister(split[1]))
        "dec" -> Decrement(parseRegister(split[1]))
        "jnz" -> ConditionalJump(parseOperand(split[1]), split[2].toInt())
        else -> throw ParseException("Unknown opcode: ${split[0]}", 0)
      }
    }

  }

}

class Machine(val program: List<Instruction>) {

  private val _registers = IntArray(4)

  private var pc = -1

  private val Instruction.Operand.value: Int
    get() = when (this) {
      is Instruction.Operand.Literal -> value
      is Instruction.Operand.Register -> _registers[index]
    }

  private var Instruction.Operand.Register.inMemory: Int
    get() = _registers[index]
    set(value) {
      _registers[index] = value
    }

  val isHalted: Boolean
    get() = pc !in 0..program.lastIndex

  val registers: IntArray
    get() = _registers.copyOf()

  private fun copy(from: Instruction.Operand, to: Instruction.Operand.Register) {
    to.inMemory = from.value
  }

  private fun increment(register: Instruction.Operand.Register) {
    register.inMemory += 1
  }

  private fun decrement(register: Instruction.Operand.Register) {
    register.inMemory -= 1
  }

  private fun conditionalJump(condition: Instruction.Operand, offset: Int) {
    if (condition.value != 0) {
      pc += (offset - 1)
    }
  }

  private fun execute(instruction: Instruction): Unit = when (instruction) {
    is Instruction.Copy -> instruction.let { (from, to) -> copy(from, to) }
    is Instruction.Increment -> instruction.let { (register) -> increment(register) }
    is Instruction.Decrement -> instruction.let { (register) -> decrement(register) }
    is Instruction.ConditionalJump -> instruction.let { (condition, offset) -> conditionalJump(condition, offset) }
  }

  fun run() {
    pc = 0
    while (!isHalted) {
      execute(program[pc++])
    }
    pc = -1
  }

}

private fun readProgram(): List<Instruction> = openResource("Day12.txt").useLines { lines ->
  lines.map((Instruction)::parse).toList()
}

private fun part1(): Int = Machine(readProgram()).apply(Machine::run).registers.first()

private fun part2(): Int {
  val program = listOf(Instruction.Copy(Instruction.Operand.Literal(1), Instruction.Operand.Register(2))) + readProgram()
  return Machine(program).apply(Machine::run).registers.first()
}

fun day12(): Pair<String, String> = part1().toString() to part2().toString()
