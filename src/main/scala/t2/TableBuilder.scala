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

/** Defines table builder. */
trait TableBuilder {
  /** Gets current row count. */
  def rowCount: Int

  /** Gets current column count. */
  def columnCount: Int

  /**
   * Tests for row orientation.
   *
   * @return `true` if row-oriented; `false` if column-oriented
   */
  def isRowOriented: Boolean

  /**
   * Adds values.
   *
   * A new row is added if `isRowOriented`; otherwise, a new column is added.
   *
   * @param values oriented values
   *
   * @return this builder
   *
   * @note After first set of values are added, additional sets must contain
   * same number of values.
   */
  def add(values: Seq[String]): this.type

  /**
   * Adds values.
   *
   * A new row is added if `isRowOriented`; otherwise, a new column is added.
   *
   * @param one  value
   * @param more additional values
   *
   * @return this builder
   *
   * @note After first set of values are added, additional sets must contain
   * same number of values.
   */
  def add(one: String, more: String*): this.type =
    add(one +: more)

  /**
   * Builds and returns table.
   *
   * @note Builder is reset after operation.
   */
  def build(): Table
}

/** Provides `TableBuilder` factory. */
object TableBuilder {
  /**
   * Creates table builder for specified orientation.
   *
   * @param rowOriented indicator for row orientation
   *
   * @return row-oriented builder if `rowOriented`; otherwise, column-oriented
   * builder
   */
  def apply(rowOriented: Boolean = true): TableBuilder =
    new TableBuilderImpl(rowOriented)

  /** Creates row-oriented table builder. */
  def forRows: TableBuilder =
    apply(true)

  /** Creates column-oriented table builder. */
  def forColumns: TableBuilder =
    apply(false)
}
