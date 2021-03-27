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
  private val rowHeaderColor      = configColor("rowHeaderColor", defaultColor)
  private val rowSeparatorChar    = configChar("rowSeparatorChar", "-")
  private val rowSeparatorColor   = configColor("rowSeparatorColor", defaultColor)
  private val rowSeparatorEnabled = configBoolean("rowSeparatorEnabled", true)
  private val columnHeaderEnabled = configBoolean("columnHeaderEnabled", true)
  private val columnHeaderColor   = configColor("columnHeaderColor", defaultColor)
  private val columnRightAlign    = configAlignment("columnRightAlign", Set.empty)
  private val maxValueSize        = configInt("maxValueSize", 20)
  private val cellColor           = configColor("cellColor", defaultColor)
  private val cellPad             = " " * configInt("cellPadSize", 1)
  private val firstPad            = " " * configInt("firstPadSize", 1)
  private val lastPad             = " " * configInt("lastPadSize", 1)
  private val nullValue           = configString("nullValue", "")

  def write(out: Writer, table: Table): Unit = {
    val header    = headerRow(table)
    val body      = bodyRows(table)
    val size      = rowSize(table)
    val headerFmt = headerFormat(table)
    val bodyFmt   = bodyFormat(table)
    val border    = horizontalRule(size, tableBorderChar)
    val separator = horizontalRule(size, rowSeparatorChar)

    if (tableBorderEnabled)
      out.write(output("%s", border, tableBorderColor))

    header.foreach { row =>
      out.write(output(headerFmt, row))

      if (rowSeparatorEnabled)
        out.write(output("%s", separator, rowSeparatorColor))
    }

    body.foreach { row =>
      out.write(output(bodyFmt, row))
    }

    if (tableBorderEnabled)
      out.write(output("%s", border, tableBorderColor))
  }

  private def output(format: String, row: String, color: String): String =
    String.format(color ++ format ++ resetColor ++ "%n", row)

  private def output(format: String, row: Seq[String]): String =
    String.format(format ++ "%n", row.map(adjustValue) : _*)

  private def headerRow(table: Table): Option[Seq[String]] =
    if (columnHeaderEnabled) table.rows.headOption else None

  private def bodyRows(table: Table): Seq[Seq[String]] =
    if (columnHeaderEnabled) table.rows.tail else table.rows

  private def rowSize(table: Table): Int =
    columnSizes(table).sum +
      firstPad.size +
      lastPad.size +
      (table.columnCount * (cellPad.size * 2))

  private def headerFormat(table: Table): String =
    columnSizes(table)
      .zipWithIndex
      .map {
        case (size, index) =>
          if (columnRightAlign.contains(index))
            s"${cellPad}%${size}s${cellPad}"
          else
            s"${cellPad}%-${size}s${cellPad}"

      }.mkString(columnHeaderColor ++ firstPad, "", lastPad ++ resetColor)

  private def bodyFormat(table: Table): String = {
    val leadColor  = if (rowHeaderEnabled) rowHeaderColor else cellColor
    val trailColor = if (rowHeaderEnabled) resetColor ++ cellColor else ""

    columnSizes(table)
      .zipWithIndex
      .map {
        case (size, 0)  =>
          if (columnRightAlign.contains(0))
            s"${cellPad}%${size}s${cellPad}${trailColor}"
          else
            s"${cellPad}%-${size}s${cellPad}${trailColor}"

        case (size, index) =>
          if (columnRightAlign.contains(index))
            s"${cellPad}%${size}s${cellPad}"
          else
            s"${cellPad}%-${size}s${cellPad}"

      }.mkString(leadColor ++ firstPad, "", lastPad ++ resetColor)
  }

  private def columnSizes(table: Table): Seq[Int] =
    table.columns.map(_.map(replaceNull(_).size).max.min(maxValueSize))

  private def horizontalRule(size: Int, char: String): String =
    char * size

  private def adjustValue(value: String): String =
    replaceNull(value).take(maxValueSize)

  private def replaceNull(value: String): String =
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
