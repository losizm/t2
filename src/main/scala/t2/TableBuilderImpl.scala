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

import scala.collection.mutable.ListBuffer

private class TableBuilderImpl(val isRowOriented: Boolean) extends TableBuilder:
  private val data = ListBuffer[Seq[String]]()

  def rowCount = isRowOriented match
    case true  => orientedCount
    case false => transposedCount

  def columnCount = isRowOriented match
    case true  => transposedCount
    case false => orientedCount

  def add(values: Seq[String]) = synchronized {
    if values == null then
      throw NullPointerException()

    if values.isEmpty then
      throw IllegalArgumentException("No values")

    if values.size != transposedCount && transposedCount != 0 then
      throw IllegalArgumentException("Wrong number of values")

    data += values
    this
  }

  def addAll(values: Seq[Seq[String]]) = synchronized {
    values.foreach(add)
    this
  }

  def reset() = synchronized {
    data.clear()
    this
  }

  def build() = synchronized {
    try
      TableImpl(data.toSeq, isRowOriented)
    finally
      data.clear()
  }

  override def toString =
    s"TableBuilder(rowCount=$rowCount,columnCount=$columnCount)"

  private def orientedCount =
    synchronized(data.size)

  private def transposedCount =
    synchronized(data.headOption.map(_.size).getOrElse(0))
