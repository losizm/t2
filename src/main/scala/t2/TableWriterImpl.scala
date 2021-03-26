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

import java.io.Writer

private class TableWriterImpl(config: Map[String, String]) extends TableWriter {
  private val ansiColorEnabled    = configBoolean("ansiColorEnabled", false)
  private val resetColor          = if (ansiColorEnabled) Console.RESET else ""
  private val defaultColor        = configColor("defaultColor", resetColor)
  private val tableBorderEnabled  = configBoolean("tableBorderEnabled", true)
  private val tableBorderChar     = configChar("tableBorderChar", "=")
  private val tableBorderColor    = configColor("tableBorderColor", defaultColor)
  private val rowHeaderEnabled    = configBoolean("rowHeaderEnabled", false)
  private val rowHeaderFontColor  = configColor("rowHeaderFontColor", defaultColor)
  private val rowSeparatorChar    = configChar("rowSeparatorChar", "-")
  private val rowSeparatorColor   = configColor("rowSeparatorColor", defaultColor)
  private val rowSeparatorEnabled = configBoolean("rowSeparatorEnabled", true)
  private val columnMaxSize       = configInt("columnMaxSize", 20)
  private val columnRightAlign    = configAlignment("columnRightAlign", Set.empty)
  private val cellTruncateEnabled = configBoolean("cellTruncateEnabled", false)
  private val cellFontColor       = configColor("cellFontColor", defaultColor)
  private val cellSpace           = " " * configInt("cellSpaceSize", 2)
  private val leadSpace           = " " * configInt("leadSpaceSize", 1)
  private val trailSpace          = " " * configInt("trailSpaceSize", 1)
  private val nullValue           = configString("nullValue", "")

  def write(out: Writer, table: Table): Unit = {
    val header    = rowHeader(table)
    val rows      = rowData(table)
    val format    = rowFormat(table)
    val size      = rowSize(table)
    val border    = horizontalRule(size, tableBorderChar)
    val separator = horizontalRule(size, rowSeparatorChar)

    if (tableBorderEnabled)
      out.write(output("%s%n", border, tableBorderColor))

    header.foreach { row =>
      out.write(output(format, row, rowHeaderFontColor))

      if (rowSeparatorEnabled)
        out.write(output("%s%n", separator, rowSeparatorColor))
    }

    rows.foreach { row =>
      out.write(output(format, row, cellFontColor))
    }

    if (tableBorderEnabled)
      out.write(output("%s%n", border, tableBorderColor))
  }

  private def output(format: String, row: String, fontColor: String): String =
    String.format(fontColor ++ format ++ resetColor, row)

  private def output(format: String, row: Seq[String], fontColor: String): String =
    String.format(fontColor ++ format ++ resetColor, row.map(adjustValue) : _*)

  private def rowHeader(table: Table): Option[Seq[String]] =
    if (rowHeaderEnabled) table.rows.headOption else None

  private def rowData(table: Table): Seq[Seq[String]] =
    if (rowHeaderEnabled) table.rows.tail else table.rows

  private def rowSize(table: Table): Int =
    columnSizes(table).sum +
      leadSpace.size +
      trailSpace.size +
      ((table.columnCount - 1) * cellSpace.size)

  private def rowFormat(table: Table): String =
    columnSizes(table)
      .zipWithIndex
      .map { case (size, index) =>
        if (columnRightAlign.contains(index))
          s"%${size}s"
        else
          s"%-${size}s"
      }.mkString(leadSpace, cellSpace, trailSpace ++ "%n")

  private def columnSizes(table: Table): Seq[Int] =
    table.columns.map(_.map(replaceNull(_).size).max.min(columnMaxSize))

  private def horizontalRule(size: Int, char: String): String =
    char * size

  private def adjustValue(value: String): String =
    cellTruncateEnabled match {
      case true  => replaceNull(value).take(columnMaxSize)
      case false => replaceNull(value)
    }

  private def replaceNull(value: String): String =
    if (value == null) nullValue else value

  private def truncateValue(value: String): String =
    if (value == null) nullValue else value

  private def configString(name: String, default: => String): String =
    config.get(name).getOrElse(default)

  private def configInt(name: String, default: => Int): Int =
    config.get(name).map(_.toInt).getOrElse(default)

  private def configBoolean(name: String, default: => Boolean): Boolean =
    config.get(name).map(_.toBoolean).getOrElse(default)

  private def configChar(name: String, default: => String): String =
    config.get(name).map(_.take(1)).getOrElse(default)

  private def configColor(name: String, default: => String): String =
    ansiColorEnabled match {
      case true  =>
        config.get(name).map(_.toLowerCase).map {
          case "black"   => Console.BLACK
          case "red"     => Console.RED
          case "green"   => Console.GREEN
          case "yellow"  => Console.YELLOW
          case "blue"    => Console.BLUE
          case "magenta" => Console.MAGENTA
          case "cyan"    => Console.CYAN
          case "white"   => Console.WHITE
          case value     => value
        }.getOrElse(default)

      case false => ""
    }

  private def configAlignment(name: String, default: => Set[Int]): Set[Int] =
    config
      .get(name)
      .map(_.split(",").map(_.toInt).toSet)
      .getOrElse(default)
}
