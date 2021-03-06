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

/**
 * Defines table.
 *
 * ## How to Create Table
 *
 * A `Table` can be created using a factory defined in companion object, or it
 * can be built incrementally using a `TableBuilder`.
 *
 * The two tables created in the example below are effectively the same.
 *
 * {{{
 * // Create table with data supplied as Seq[Seq[String]]
 * val table1 = t2.Table(
 *   Seq(
 *     Seq("#", "Effective Date", "Currency Code", "Exchange Rate"),
 *     Seq("1", "2021-01-04", "USD", "0.690236"),
 *     Seq("2", "2021-01-05", "USD", "0.690627"),
 *     Seq("3", "2021-01-06", "USD", "0.689332")
 *   )
 * )
 *
 * // Incrementally build table by adding value sequences
 * val table2 = t2.TableBuilder()
 *   .add("#", "Effective Date", "Currency Code", "Exchange Rate")
 *   .add("1", "2021-01-04", "USD", "0.690236")
 *   .add("2", "2021-01-05", "USD", "0.690627")
 *   .add("3", "2021-01-06", "USD", "0.689332")
 *   .build()
 *
 * // Assert equality
 * assert(table1.rows == table2.rows)
 * }}}
 *
 * @see [[Table.forRows]], [[Table.forColumns]], [[TableBuilder]]
 */
trait Table:
  /** Gets row count. */
  def rowCount: Int

  /** Gets column count. */
  def columnCount: Int

  /** Gets row-oriented data. */
  def rows: Seq[Seq[String]]

  /** Gets column-oriented data. */
  def columns: Seq[Seq[String]]

  /**
   * Gets row values at given index.
   *
   * @param index row index
   */
  def row(index: Int): Seq[String]

  /**
   * Gets column values at given index.
   *
   * @param index column index
   */
  def column(index: Int): Seq[String]

  /**
   * Gets value at given location.
   *
   * @param row    row index
   * @param column column index
   */
  def apply(row: Int, column: Int): String

/** Provides `Table` factory. */
object Table:
  /**
   * Creates table with supplied data.
   *
   * @param data        table data
   * @param rowOriented indicator for row orientation
   *
   * @return row-oriented table if `rowOriented`; otherwise, column-oriented
   * table
   */
  def apply(data: Seq[Seq[String]], rowOriented: Boolean = true): Table =
    data.headOption.map(_.size).foreach { size =>
      if !data.tail.forall(_.size == size) then
        throw IllegalArgumentException("Inconsistent table data")
    }

    TableImpl(data, rowOriented)

  /**
   * Creates row-oriented table with supplied data.
   *
   * @param data table data
   */
  def forRows(data: Seq[Seq[String]]): Table =
    apply(data, true)

  /**
   * Creates column-oriented table with supplied data.
   *
   * @param data table data
   */
  def forColumns(data: Seq[Seq[String]]): Table =
    apply(data, false)
