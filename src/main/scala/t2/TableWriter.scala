/*
 * Copyright 2021 Carlos Conyers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package t2

import java.io.{ OutputStream, PrintWriter, Writer }

/**
 * Defines table writer.
 *
 * ## Usage
 *
 * Below is an example of how to create, configure, and use the default
 * `TableWriter`.
 *
 * {{{
 * // Build table with first row as table header
 * val table = t2.TableBuilder()
 *   .add("#", "Effective Date", "Currency Code", "Exchange Rate")
 *   .add("1", "2021-01-04", "USD", "0.690236")
 *   .add("2", "2021-01-05", "USD", "0.690627")
 *   .add("3", "2021-01-06", "USD", "0.689332")
 *   .build()
 *
 * // Create table writer with supplied configuration
 * val writer = t2.TableWriter(
 *   "ansiColorEnabled" -> "true",
 *   "tableBorderColor" -> "cyan",
 *   "tableHeaderColor" -> "black,yellowBackground",
 *   "bodyRuleColor"    -> "yellow",
 *   "rowHeaderEnabled" -> "true",
 *   "rowHeaderColor"   -> "bold,cyan",
 *   "columnRightAlign" -> "0,3" // Right align first and last columns
 * )
 *
 * // Write table to stdout
 * writer.write(System.out, table)
 * }}}
 *
 * The table writer can be configured for changing such things as cell padding,
 * character used for table borders, and more.
 *
 * @see [[t2.TableWriter$.apply(config:Map[String,String])* TableWriter(Map[String, String])]],
 * [[t2.TableWriter$.apply(config:(String,String)*)* TableWriter((String,String)*)]]
 */
trait TableWriter:
  /**
   * Writes table to given output.
   *
   * @param out   output writer
   * @param table input table
   */
  def write(out: Writer, table: Table): Unit

  /**
   * Writes table to given output.
   *
   * @param out   output stream
   * @param table input table
   */
  def write(out: OutputStream, table: Table): Unit =
    val writer = PrintWriter(out, true)
    try write(writer, table)
    finally writer.flush()

/**
 * Provides `TableWriter` factory.
 *
 * ## Writer Configuration
 *
 * The following keys can be supplied to configure writer.
 *
 * | Key                                   | Default Value    |
 * |---------------------------------------|------------------|
 * | ansiColorEnabled                      |`"false"`         |
 * | defaultColor <sup>&dagger;</sup>      |`AnsiColor.RESET` |
 * | leftMarginSize                        |`"0"`             |
 * | rightMarginSize                       |`"0"`             |
 * | tableBorderEnabled                    |`"true"`          |
 * | tableBorderColor <sup>&dagger;</sup>  |defaultColor      |
 * | tableBorderChar                       |`"="`             |
 * | tableHeaderEnabled                    |`"true"`          |
 * | tableHeaderColor <sup>&dagger;</sup>  |defaultColor      |
 * | tableFooterEnabled                    |`"false"`         |
 * | tableFooterColor <sup>&dagger;</sup>  |defaultColor      |
 * | bodyRuleEnabled                       |`"true"`          |
 * | bodyRuleColor <sup>&dagger;</sup>     |defaultColor      |
 * | bodyRuleChar                          |`"-"`             |
 * | rowHeaderEnabled                      |`"false"`         |
 * | rowHeaderColor <sup>&dagger;</sup>    |defaultColor      |
 * | columnRightAlign <sup>&ddagger;</sup> |`""`              |
 * | cellColor <sup>&dagger;</sup>         |defaultColor      |
 * | cellPadSize                           |`"1"`             |
 * | cellSpaceSize                         |`"0"`             |
 * | cellSpaceColor <sup>&dagger;</sup>    |`""`              |
 * | maxValueSize                          |`"20"`            |
 * | nullValue                             |`""`              |
 * | truncateEnabled                       |`"true"`          |
 *
 * <small>&dagger; Defined as `AnsiColor` values (e.g., `AnsiColor.BLACK`).</small>
 *
 * <small>&ddagger; Defined as comma- or space-delimited list of column indexes.</small>
 *
 * ANSI colors can also be suppplied as a comma- or space-delimited list
 * of any combination of following values:
 *
 * - reset
 * - bold
 * - underlined
 * - blink
 * - reversed
 * - invisible
 * - black
 * - red
 * - green
 * - yellow
 * - blue
 * - magenta
 * - cyan
 * - white
 * - blackBackground
 * - redBackground
 * - greenBackground
 * - yellowBackground
 * - blueBackground
 * - magentaBackground
 * - cyanBackground
 * - whiteBackground
 */
object TableWriter:
  /** Creates table writer with supplied configuration. */
  def apply(config: (String, String)*): TableWriter =
    apply(config.toMap)

  /** Creates table writer with supplied configuration. */
  def apply(config: Map[String, String]): TableWriter =
    TableWriterImpl(config)
