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

private case class TableImpl(data: Seq[Seq[String]], isRowOriented: Boolean) extends Table:
  lazy val rows        = if isRowOriented then data else data.transpose
  lazy val columns     = if isRowOriented then data.transpose else data
  lazy val rowCount    = if isRowOriented then data.size else data.headOption.map(_.size).getOrElse(0)
  lazy val columnCount = if isRowOriented then data.headOption.map(_.size).getOrElse(0) else data.size
    
  def row(index: Int) =
    rows(index)

  def column(index: Int) =
    columns(index)

  def apply(row: Int, column: Int) =
    rows(row)(column)

  override lazy val toString =
    s"Table(rowCount=$rowCount,columnCount=$columnCount)"
