
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

import java.io.StringWriter

class TableWriterSpec extends org.scalatest.flatspec.AnyFlatSpec {
  val config = Map(
    "ansiColorEnabled"    -> "true",
    "fontColor"           -> "blue",
    "tableBorderChar"     -> "x",
    "tableBorderColor"    -> "red",
    "rowHeaderIncluded"   -> "true",
    "rowSeparatorChar"    -> "o",
    "rowSeparatorColor"   -> "yellow",
    "rowHeaderFontColor"  -> s"${Console.BOLD}${Console.YELLOW_B}${Console.BLACK}",
    "columnMaxSize"       -> "20",
    "columnRightAlign"    -> "3,4",
    "cellSpaceSize"       -> "4",
    "leadSpaceSize"       -> "1",
    "trailSpaceSize"      -> "1",
    "nullValue"           -> " ",
    "truncateCellValue"   -> "true"
  )

  val rowHeader = Seq("Effective Date", "Day Of Week", "Currency Code", "SDR Per Unit", "Percent Variance")

  val tableData = Seq(
    Seq("2021-01-04", "Mon", "CAD", "0.541319", null),
    Seq("2021-01-04", "Mon", "EUR", "0.848709", null),
    Seq("2021-01-04", "Mon", "GBP", "0.943548", null),
    Seq("2021-01-04", "Mon", "INR", "0.009454", null),
    Seq("2021-01-04", "Mon", "USD", "0.690236", null),
    Seq("2021-01-05", "Tue", "CAD", "0.543502", "0.40"),
    Seq("2021-01-05", "Tue", "EUR", "0.847472", "-0.15"),
    Seq("2021-01-05", "Tue", "GBP", "0.938667", "-0.52"),
    Seq("2021-01-05", "Tue", "INR", "0.009447", "-0.07"),
    Seq("2021-01-05", "Tue", "USD", "0.690627", "0.06"),
    Seq("2021-01-06", "Wed", "CAD", "0.543422", "-0.01"),
    Seq("2021-01-06", "Wed", "EUR", "0.850499", "0.36"),
    Seq("2021-01-06", "Wed", "GBP", "0.940831", "0.23"),
    Seq("2021-01-06", "Wed", "INR", "0.009425", "-0.23"),
    Seq("2021-01-06", "Wed", "USD", "0.689332", "-0.19"),
  )

  it should "write table with ANSI colors" in {
    val out = new StringWriter()
    val writer = TableWriter(config)
    writer.write(out, Table(rowHeader +: tableData))

    info("table output:\n" + out.toString)
  }
}
