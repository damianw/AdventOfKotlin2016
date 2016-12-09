AdventOfKotlin2016
===

Solutions to the [2016 Advent Of Code](http://adventofcode.com/2016) problems, written in Kotlin (1.1-M02).

Try not to take anything too seriously. It's all for `fun`, after all. OOP is `fun`! FP is `fun`! Gradle is `fun`! What's code without the `fun`?

### Building

```
$ ./gradlew clean assemble
```

A `AdventOfKotlin2016` binary will be output to the `build` directory.

### Running

Running the `AdventOfKotlin2016` binary will run all the current solutions. You may optionally specify the days to run (comma separated, e.g. `1,2,3`).

```
$ build/AdventOfKotlin2016 --help
Usage: AdventOfKotlin2016 [options]
  Options:
    --days, -d
       Days of the advent calendar to solve
    --help, -h
       Prints usage information
       Default: false

$ build/AdventOfKotlin2016
========
Day 1
========
-> Time elapsed: 0 seconds
-> Part 1: 307
-> Part 2: TODO I forgot these things have two parts

========
Day 2
========
-> Time elapsed: 0 seconds
-> Part 1: 92435
-> Part 2: C1A88

========
Day 3
========
-> Time elapsed: 0 seconds
-> Part 1: 983
-> Part 2: 1836

========
Day 5
========
-> Time elapsed: 34 seconds
-> Part 1: c6697b55
-> Part 2: 8c35d1ab

========
Day 6
========
-> Time elapsed: 0 seconds
-> Part 1: usccerug
-> Part 2: cnvvtafc

========
Day 7
========
-> Time elapsed: 0 seconds
-> Part 1: 115
-> Part 2: 231

========
Day 8
========
-> Time elapsed: 0 seconds
-> Part 1: 115
-> Part 2: ┌──────────────────────────────────────────────────┐
           │████ ████ ████ █   ██  █ ████ ███  ████  ███   ██ │
           │█    █    █    █   ██ █  █    █  █ █      █     █ │
           │███  ███  ███   █ █ ██   ███  █  █ ███    █     █ │
           │█    █    █      █  █ █  █    ███  █      █     █ │
           │█    █    █      █  █ █  █    █ █  █      █  █  █ │
           │████ █    ████   █  █  █ █    █  █ █     ███  ██  │
           └──────────────────────────────────────────────────┘
```

Enjoy?
