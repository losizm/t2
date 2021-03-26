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
 * The following is sample output for default table writer.
 *
 * <pre>
 * ============================================================================
 *  Effective Date  Day Of Week  Currency Code  SDR Per Unit  Percent Variance 
 * ----------------------------------------------------------------------------
 *  2021-01-05      Tue          CAD                0.543502              0.40 
 *  2021-01-05      Tue          EUR                0.847472             -0.15 
 *  2021-01-05      Tue          GBP                0.938667             -0.52 
 *  2021-01-05      Tue          INR                0.009447             -0.07 
 *  2021-01-05      Tue          USD                0.690627              0.06 
 *  2021-01-06      Wed          CAD                0.543422             -0.01 
 *  2021-01-06      Wed          EUR                0.850499              0.36 
 *  2021-01-06      Wed          GBP                0.940831              0.23 
 *  2021-01-06      Wed          INR                0.009425             -0.23 
 *  2021-01-06      Wed          USD                0.689332             -0.19 
 * ============================================================================
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

/** Provides `TableWriter` factory. */
object TableWriter {
  /** Creates table writer with default configuration. */
  def apply(): TableWriter =
    apply(Map.empty)

  /**
   * Creates table writer with supplied configuration.
   *
   * == Writer Configuration ==
   *
   * The following keys can be supplied to configure writer.
   *
   * |Key                   |Default Value   |
   * |----------------------|----------------|
   * |ansiColorEnabled      |`"false"`       |
   * |fontColor^*^          |`Console.RESET` |
   * |tableBorderChar       |`"="`           |
   * |tableBorderColor^*^   |`Console.RESET` |
   * |rowHeaderIncluded     |`"false"`       |
   * |rowHeaderFontColor^*^ |`Console.RESET` |
   * |rowSeparatorChar      |`"-"`           |
   * |rowSeparatorColor^*^  |`Console.RESET` |
   * |columnMaxSize         |`"20"`          |
   * |columnRightAlign      |`""`            |
   * |cellSpaceSize         |`"2"`           |
   * |leadSpaceSize         |`"1"`           |
   * |trailSpaceSize        |`"1"`           |
   * |nullValue             |`""`            |
   * |truncateCellValue     |`"false"`       |
   * <span></span>
   *
   * ^*^ Color should be supplied as `AnsiColor` value. It can also be supplied
   * as `"black"`, `"red"`, `"green"`, `"yellow"`, `"blue"`, `"magenta"`,
   * `"cyan"`, or `"white"`.
   *
   * @param config writer configuration
   */
  def apply(config: Map[String, String]): TableWriter =
    new TableWriterImpl(config)
}
