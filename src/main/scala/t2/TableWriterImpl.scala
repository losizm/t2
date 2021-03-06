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
import scala.io.AnsiColor

private class TableWriterImpl(config: Map[String, String]) extends TableWriter with AnsiColor:
  private val ansiColorEnabled   = configBoolean("ansiColorEnabled", false)
  private val resetColor         = if ansiColorEnabled then RESET else ""
  private val defaultColor       = configColor("defaultColor", resetColor)
  private val leftMargin         = " " * configInt("leftMarginSize", 0)
  private val rightMargin        = " " * configInt("rightMarginSize", 0)
  private val tableBorderEnabled = configBoolean("tableBorderEnabled", true)
  private val tableBorderChar    = configChar("tableBorderChar", "=")
  private val tableBorderColor   = configColor("tableBorderColor", defaultColor)
  private val tableHeaderEnabled = configBoolean("tableHeaderEnabled", true)
  private val tableHeaderColor   = configColor("tableHeaderColor", defaultColor)
  private val tableFooterEnabled = configBoolean("tableFooterEnabled", false)
  private val tableFooterColor   = configColor("tableFooterColor", defaultColor)
  private val bodyRuleEnabled    = configBoolean("bodyRuleEnabled", true)
  private val bodyRuleColor      = configColor("bodyRuleColor", defaultColor)
  private val bodyRuleChar       = configChar("bodyRuleChar", "-")
  private val rowHeaderEnabled   = configBoolean("rowHeaderEnabled", false)
  private val rowHeaderColor     = configColor("rowHeaderColor", defaultColor)
  private val columnRightAlign   = configAlignment("columnRightAlign", Set.empty)
  private val cellColor          = configColor("cellColor", defaultColor)
  private val cellPad            = " " * configInt("cellPadSize", 1)
  private val cellSpaceSize      = configInt("cellSpaceSize", 0)
  private val cellSpace          = configColor("cellSpaceColor", "") ++ (" " * cellSpaceSize) ++ resetColor
  private val maxValueSize       = configInt("maxValueSize", 20)
  private val nullValue          = configString("nullValue", "")
  private val truncateEnabled    = configBoolean("truncateEnabled", true)

  def write(out: Writer, table: Table): Unit =
    val header    = headerRow(table)
    val footer    = footerRow(table)
    val body      = bodyRows(table)
    val size      = rowSize(table)
    val headerFmt = headerFormat(table)
    val footerFmt = footerFormat(table)
    val bodyFmt   = bodyFormat(table)
    val border    = horizontalRule(size, tableBorderChar)
    val bodyRule  = horizontalRule(size, bodyRuleChar)

    if tableBorderEnabled then
      out.write(output("%s", border, tableBorderColor))

    header.foreach { row =>
      out.write(output(headerFmt, row))

      if bodyRuleEnabled then
        out.write(output("%s", bodyRule, bodyRuleColor))
    }

    body.foreach { row =>
      out.write(output(bodyFmt, row))
    }

    footer.foreach { row =>
      if bodyRuleEnabled then
        out.write(output("%s", bodyRule, bodyRuleColor))

      out.write(output(footerFmt, row))
    }

    if tableBorderEnabled then
      out.write(output("%s", border, tableBorderColor))

  private def output(format: String, row: String, color: String): String =
    String.format(leftMargin ++ color ++ format ++ resetColor ++ rightMargin ++ "%n", row)

  private def output(format: String, row: Seq[String]): String =
    String.format(leftMargin ++ format ++ rightMargin ++ "%n", row.map(adjustValue)*)

  private def headerRow(table: Table): Option[Seq[String]] =
    if tableHeaderEnabled then table.rows.headOption else None

  private def bodyRows(table: Table): Seq[Seq[String]] =
    (if tableHeaderEnabled then table.rows.tail else table.rows)
      .dropRight(if tableFooterEnabled then 1 else 0)

  private def footerRow(table: Table): Option[Seq[String]] =
    if tableFooterEnabled then table.rows.tail.lastOption else None

  private def rowSize(table: Table): Int =
    columnSizes(table).sum +
      (table.columnCount * (cellPad.size * 2)) +
      ((table.columnCount - 1) * cellSpaceSize)

  private def headerFormat(table: Table): String =
    columnSizes(table)
      .zipWithIndex
      .map {
        case (size, index) =>
          if columnRightAlign.contains(index) then
            s"${cellPad}%${size}s${cellPad}"
          else
            s"${cellPad}%-${size}s${cellPad}"

      }.mkString(tableHeaderColor, cellSpace ++ tableHeaderColor, resetColor)

  private def footerFormat(table: Table): String =
    columnSizes(table)
      .zipWithIndex
      .map {
        case (size, index) =>
          if columnRightAlign.contains(index) then
            s"${cellPad}%${size}s${cellPad}"
          else
            s"${cellPad}%-${size}s${cellPad}"

      }.mkString(tableFooterColor, cellSpace ++ tableFooterColor, resetColor)

  private def bodyFormat(table: Table): String = {
    val leadColor  = if rowHeaderEnabled then rowHeaderColor else cellColor
    val trailColor = if rowHeaderEnabled then resetColor ++ cellColor else ""

    columnSizes(table)
      .zipWithIndex
      .map {
        case (size, 0)  =>
          if columnRightAlign.contains(0) then
            s"${cellPad}%${size}s${cellPad}${trailColor}"
          else
            s"${cellPad}%-${size}s${cellPad}${trailColor}"

        case (size, index) =>
          if columnRightAlign.contains(index) then
            s"${cellPad}%${size}s${cellPad}"
          else
            s"${cellPad}%-${size}s${cellPad}"

      }.mkString(leadColor, cellSpace ++ trailColor, resetColor)
  }

  private def columnSizes(table: Table): Seq[Int] =
    table.columns.map(_.map(replaceNull(_).size).max.min(maxValueSize))

  private def horizontalRule(size: Int, char: String): String =
    char * size

  private def adjustValue(value: String): String =
    truncateEnabled match
      case true  => replaceNull(value).take(maxValueSize)
      case false => replaceNull(value)

  private def replaceNull(value: String): String =
    if value == null then nullValue else value

  private def configString(name: String, default: => String): String =
    config.get(name).getOrElse(default)

  private def configInt(name: String, default: => Int): Int =
    config.get(name).map(_.toInt).getOrElse(default)

  private def configBoolean(name: String, default: => Boolean): Boolean =
    config.get(name).map(_.toBoolean).getOrElse(default)

  private def configChar(name: String, default: => String): String =
    config.get(name).map(_.take(1)).getOrElse(default)

  private def configColor(name: String, default: => String): String =
    ansiColorEnabled match
      case true  =>
        val words = "(\\w+(?:[,\\s]+\\w+)*)".r
        config.get(name).map {
          case words(value) => toAnsiColor(value)
          case value        => value
        }.getOrElse(default)

      case false => ""

  private def configAlignment(name: String, default: => Set[Int]): Set[Int] =
    config
      .get(name)
      .map(_.split("[,\\s]+").map(_.toInt).toSet)
      .getOrElse(default)

  private def toAnsiColor(value: String): String =
    value
      .split("[,\\s]+")
      .map(toAnsiCode)
      .mkString

  private def toAnsiCode(value: String): String =
    value.toLowerCase match
      case "reset"             => RESET
      case "bold"              => BOLD
      case "underlined"        => UNDERLINED
      case "blink"             => BLINK
      case "reversed"          => REVERSED
      case "invisible"         => INVISIBLE
      case "black"             => BLACK
      case "red"               => RED
      case "green"             => GREEN
      case "yellow"            => YELLOW
      case "blue"              => BLUE
      case "magenta"           => MAGENTA
      case "cyan"              => CYAN
      case "white"             => WHITE
      case "blackbackground"   => BLACK_B
      case "redbackground"     => RED_B
      case "greenbackground"   => GREEN_B
      case "yellowbackground"  => YELLOW_B
      case "bluebackground"    => BLUE_B
      case "magentabackground" => MAGENTA_B
      case "cyanbackground"    => CYAN_B
      case "whitebackground"   => WHITE_B
      case _                   => ""
