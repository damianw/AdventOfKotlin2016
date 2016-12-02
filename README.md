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

Running the `AdventOfKotlin2016` binary will run all the current solutions. You may optionally specify the days to run.

```
$ build/AdventOfKotlin2016 --help
Usage: AdventOfKotlin2016 [options]
  Options:
    --days, -d
       Days of the advent calendar to solve
    --help, -h
       Prints usage information
       Default: false

$ build/AdventOfKotlin2016 -d 1,2
========
Day 1
========
307

========
Day 2
========
# This doesn't exist yet

```

Enjoy?
