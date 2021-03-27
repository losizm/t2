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
 * == Sample Output ==
 *
 * Output generated using the default table writer would look something like the
 * following if printed to a terminal.
 *
 * <pre style="background: black; color: white;">
 * ==============================================
 *  Effective Date  Currency Code  Exchange Rate
 * ----------------------------------------------
 *  2021-01-04      USD                 0.690236
 *  2021-01-05      USD                 0.690627
 *  2021-01-06      USD                 0.689332
 * ==============================================
 * </pre>
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
 * |rowHeaderEnabled      |`"false"`       |
 * |rowHeaderColor^1^     |defaultColor    |
 * |rowSeparatorEnabled   |`"true"`        |
 * |rowSeparatorChar      |`"-"`           |
 * |rowSeparatorColor^1^  |defaultColor    |
 * |columnRightAlign^2^   |`""`            |
 * |columnHeaderEnabled   |`"false"`       |
 * |columnHeaderColor^1^  |defaultColor    |
 * |maxValueSize          |`"20"`          |
 * |cellColor^1^          |defaultColor    |
 * |cellSpaceSize         |`"2"`           |
 * |leadSpaceSize         |`"1"`           |
 * |trailSpaceSize        |`"1"`           |
 * |nullValue             |`""`            |
 * <span></span>
 *
 * ^1^ Defined as `AnsiColor` value; also accepts values `"black"`, `"red"`,
 * `"green"`, `"yellow"`, `"blue"`, `"magenta"`, `"cyan"`, and `"white"`.
 *
 * ^2^ Defined as comma-delimited list of column indexes.
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
