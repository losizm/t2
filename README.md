# t2

Utility for text tables.

[![Maven Central](https://img.shields.io/maven-central/v/com.github.losizm/t2_2.13.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.losizm%22%20AND%20a:%22t2_2.13%22)

## Getting Started
To use **t2**, add it as a dependency to your project:

```scala
libraryDependencies += "com.github.losizm" %% "t2" % "0.1.0"
```

## Usage

Below is an example of how to use **t2**. Here a table is built, and its content
is written to stdout.

```scala
// Build table data with first row as header
val table = t2.TableBuilder()
  .add("Effective Date", "Currency Code", "Exchange Rate")
  .add("2021-01-04", "USD", "0.690236")
  .add("2021-01-05", "USD", "0.690627")
  .add("2021-01-06", "USD", "0.689332")
  .build()

// Create table writer with supplied configuration
val writer = t2.TableWriter(
  "ansiColorEnabled"   -> "true",
  "tableBorderColor"   -> Console.CYAN,
  "rowHeaderEnabled"   -> "true",
  "rowHeaderFontColor" -> s"${Console.YELLOW_B}${Console.BLACK}",
  "rowSeparatorColor"  -> Console.YELLOW,
  "columnRightAlign"   -> "2" // Right align column index 2
)

// Write table to standard output
writer.write(System.out, table)
```

The generated output would look something like the following if printed to
terminal.

```
==============================================
 Effective Date  Currency Code  Exchange Rate
----------------------------------------------
 2021-01-04      USD                 0.690236
 2021-01-05      USD                 0.690627
 2021-01-06      USD                 0.689332
==============================================
```

Table output can be adjusted using other configuration for such things as
changing cell spacing, characters used for table borders and row separator, and a
few others.

## API Documentation

See [scaladoc](https://losizm.github.io/t2/latest/api/t2/index.html)
for additional details.

## License
**t2** is licensed under the Apache License, Version 2. See [LICENSE](LICENSE)
file for more information.
