
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
    "defaultColor"        -> "white",
    "tableBorderEnabled"  -> "true",
    "tableBorderChar"     -> "x",
    "tableBorderColor"    -> "red",
    "rowHeaderEnabled"    -> "true",
    "rowHeaderColor"      -> s"${Console.BOLD}${Console.GREEN_B}${Console.RED}",
    "rowSeparatorEnabled" -> "true",
    "rowSeparatorChar"    -> "o",
    "rowSeparatorColor"   -> "yellow",
    "columnHeaderEnabled" -> "true",
    "columnHeaderColor"   -> s"${Console.BOLD}${Console.YELLOW_B}${Console.BLACK}",
    "columnMaxSize"       -> "20",
    "columnRightAlign"    -> "0,4,5",
    "cellTruncateEnabled" -> "true",
    "cellColor"           -> s"${Console.WHITE_B}${Console.BLUE}",
    "cellSpaceSize"       -> "4",
    "leadSpaceSize"       -> "1",
    "trailSpaceSize"      -> "1",
    "nullValue"           -> " "
  )

  val columnHeader = Seq("#", "Effective Date", "Day Of Week", "Currency Code", "SDR Per Unit", "Percent Variance")

  val tableData = Seq(
    Seq( "1", "2021-01-04", "Mon", "CAD", "0.541319", null),
    Seq( "2", "2021-01-04", "Mon", "EUR", "0.848709", null),
    Seq( "3", "2021-01-04", "Mon", "GBP", "0.943548", null),
    Seq( "4", "2021-01-04", "Mon", "INR", "0.009454", null),
    Seq( "5", "2021-01-04", "Mon", "USD", "0.690236", null),
    Seq( "6", "2021-01-05", "Tue", "CAD", "0.543502", "0.40"),
    Seq( "7", "2021-01-05", "Tue", "EUR", "0.847472", "-0.15"),
    Seq( "8", "2021-01-05", "Tue", "GBP", "0.938667", "-0.52"),
    Seq( "9", "2021-01-05", "Tue", "INR", "0.009447", "-0.07"),
    Seq("10", "2021-01-05", "Tue", "USD", "0.690627", "0.06"),
    Seq("11", "2021-01-06", "Wed", "CAD", "0.543422", "-0.01"),
    Seq("12", "2021-01-06", "Wed", "EUR", "0.850499", "0.36"),
    Seq("13", "2021-01-06", "Wed", "GBP", "0.940831", "0.23"),
    Seq("14", "2021-01-06", "Wed", "INR", "0.009425", "-0.23"),
    Seq("15", "2021-01-06", "Wed", "USD", "0.689332", "-0.19"),
  )

  it should "write table with ANSI colors" in {
    val out = new StringWriter()
    val writer = TableWriter(config)
    writer.write(out, Table(columnHeader +: tableData))

    info("table output:\n" + out.toString)
  }
}
