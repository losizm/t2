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

trait TableData {
  val goodRows = Seq(
    Seq("a", "b", "c"),
    Seq("x", "y", "z")
  )

  val goodColumns = Seq(
    Seq("a", "x"),
    Seq("b", "y"),
    Seq("c", "z")
  )

  val badRows = Seq(
    Seq("a", "b", "c"),
    Seq("x", "y")
  )

  val badColumns = Seq(
    Seq("a", "x"),
    Seq("b", "y"),
    Seq("z")
  )

  def check(table: Table): Unit = {
    assert(table.rowCount == 2)
    assert(table.columnCount == 3)
    assert(table.rows == goodRows)
    assert(table.columns == goodColumns)
    assert(table(0, 0) == "a")
    assert(table(0, 1) == "b")
    assert(table(0, 2) == "c")
    assert(table(1, 0) == "x")
    assert(table(1, 1) == "y")
    assert(table(1, 2) == "z")
  }
}
