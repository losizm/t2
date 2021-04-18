# t2

Utility for text tables.

[![Maven Central](https://img.shields.io/maven-central/v/com.github.losizm/t2_2.13.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.losizm%22%20AND%20a:%22t2_2.13%22)

## Getting Started
Add **t2** as a dependency to your project:

```scala
libraryDependencies += "com.github.losizm" %% "t2" % "0.3.0"
```

## Usage

Below is an example of **t2** in action. Here it builds a table and writes the
content to stdout.

```scala
// Build table with first row as table header
val table = t2.TableBuilder()
  .add("#", "Effective Date", "Currency Code", "Exchange Rate")
  .add("1", "2021-01-04", "USD", "0.690236")
  .add("2", "2021-01-05", "USD", "0.690627")
  .add("3", "2021-01-06", "USD", "0.689332")
  .build()

// Create table writer with supplied configuration
val writer = t2.TableWriter(
  "ansiColorEnabled" -> "true",
  "tableBorderColor" -> Console.CYAN,
  "tableHeaderColor" -> (Console.YELLOW_B ++ Console.BLACK),
  "columnRightAlign" -> "0,3", // Right align first and last columns
  "rowHeaderEnabled" -> "true",
  "rowHeaderColor"   -> (Console.BOLD ++ Console.CYAN),
  "bodyRuleColor"    -> Console.YELLOW
)

// Write table to stdout
writer.write(System.out, table)
```

The generated output would look something like the following if printed to a
color-enabled terminal.

<div style="background: #111">
  <img style="padding: 0.2em;" src="images/table.png" width="470" />
</div>

Table output can be restyled using configuration for such things as changing
cell padding, character used for table borders, and more.

## API Documentation

See [scaladoc](https://losizm.github.io/t2/latest/api/t2/index.html)
for additional details.

## License
**t2** is licensed under the Apache License, Version 2. See [LICENSE](LICENSE)
file for more information.
