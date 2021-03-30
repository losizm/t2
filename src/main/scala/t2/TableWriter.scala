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
 * == Usage ==
 *
 * Below is an example of how to create, configure, and use the default
 * `TableWriter`.
 *
 * {{{
 * // Build table
 * val table = t2.TableBuilder()
 *   .add("#", "Effective Date", "Currency Code", "Exchange Rate")
 *   .add("1", "2021-01-04", "USD", "0.690236")
 *   .add("2", "2021-01-05", "USD", "0.690627")
 *   .add("3", "2021-01-06", "USD", "0.689332")
 *   .build()
 *
 * // Create table writer with supplied configuration
 * val writer = t2.TableWriter(
 *   "ansiColorEnabled"    -> "true",
 *   "tableBorderColor"    -> Console.CYAN,
 *   "columnHeaderColor"   -> (Console.YELLOW_B ++ Console.BLACK),
 *   "columnRightAlign"    -> "0,3", // Right align first and last columns
 *   "rowHeaderEnabled"    -> "true",
 *   "rowHeaderColor"      -> (Console.BOLD ++ Console.CYAN),
 *   "rowSeparatorColor"   -> Console.YELLOW
 * )
 *
 * // Write table to standard output
 * writer.write(System.out, table)
 * }}}
 *
 * The generated output would look something like the following if printed to a
 * color-enabled terminal.
 *
 * <pre style="background: black; color: white;">
 * <span style="color: #0cc;">===================================================</span>
 * <span style="background: #cc0; color: black;">  #  Effective Date  Currency Code  Exchange Rate  </span>
 * <span style="color: #cc0;">---------------------------------------------------</span>
 * <span style="color: #0cc; font-weight: bold;">  1 </span> 2021-01-04      USD                 0.690236
 * <span style="color: #0cc; font-weight: bold;">  2 </span> 2021-01-05      USD                 0.690627
 * <span style="color: #0cc; font-weight: bold;">  3 </span> 2021-01-06      USD                 0.689332
 * <span style="color: #0cc;">===================================================</span>
 * </pre>
 *
 * The table writer can be reconfigured for changing such things as cell padding,
 * characters used for table borders and row separator, and more.
 *
 * @see [[t2.TableWriter$.apply(config:Map[String,String])* TableWriter(Map[String, String])]],
 * [[t2.TableWriter$.apply(config:(String,String)*)* TableWriter((String,String)*)]]
 */
trait TableWriter {
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
  def write(out: OutputStream, table: Table): Unit = {
    val writer = new PrintWriter(out, true)
    try write(writer, table)
    finally writer.flush()
  }
}

/**
 * Provides `TableWriter` factory.
 *
 * @define writerdoc
 *
 * Creates table writer with supplied configuration.
 *
 * == Writer Configuration ==
 *
 * The following keys can be supplied to configure writer.
 *
 * |Key                   |Default Value   |
 * |----------------------|----------------|
 * |ansiColorEnabled      |`"false"`       |
 * |defaultColor^1^       |`Console.RESET` |
 * |tableBorderEnabled    |`"true"`        |
 * |tableBorderChar       |`"="`           |
 * |tableBorderColor^1^   |defaultColor    |
 * |columnHeaderEnabled   |`"true"`        |
 * |columnHeaderColor^1^  |defaultColor    |
 * |columnRightAlign^2^   |`""`            |
 * |rowHeaderEnabled      |`"false"`       |
 * |rowHeaderColor^1^     |defaultColor    |
 * |rowSeparatorEnabled   |`"true"`        |
 * |rowSeparatorChar      |`"-"`           |
 * |rowSeparatorColor^1^  |defaultColor    |
 * |maxValueSize          |`"20"`          |
 * |leftMarginSize        |`"0"`           |
 * |rightMarginSize       |`"0"`           |
 * |cellColor^1^          |defaultColor    |
 * |cellSpaceSize         |`"1"`           |
 * |cellSpaceColor^1^     |`""`            |
 * |cellPadSize           |`"1"`           |
 * |firstPadSize          |`"1"`           |
 * |lastPadSize           |`"1"`           |
 * |nullValue             |`""`            |
 * <span></span>
 *
 * ^1^ Defined as `AnsiColor` value; also accepts values `"black"`, `"red"`,
 * `"green"`, `"yellow"`, `"blue"`, `"magenta"`, `"cyan"`, and `"white"`.
 *
 * ^2^ Defined as comma- or space-delimited list of column indexes.
 */
object TableWriter {
  /**
   * $writerdoc
   *
   * @param config writer configuration
   */
  def apply(config: (String, String)*): TableWriter =
    apply(config.toMap)

  /**
   * $writerdoc
   *
   * @param config writer configuration
   */
  def apply(config: Map[String, String]): TableWriter =
    new TableWriterImpl(config)
}
