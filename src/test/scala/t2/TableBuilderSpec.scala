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

class TableBuilderSpec extends org.scalatest.flatspec.AnyFlatSpec with TableData {
  it should "create row-oriented table" in {
    val builder = TableBuilder.forRows
    check(buildTable(builder, goodRows))
    assert(builder.rowCount + builder.columnCount == 0)
  }

  it should "create column-oriented table" in {
    val builder = TableBuilder.forColumns
    check(buildTable(builder, goodColumns))
    assert(builder.rowCount + builder.columnCount == 0)
  }

  it should "reset builder after adding rows" in {
    val builder = TableBuilder.forRows.addAll(goodRows).reset()
    assert(builder.rowCount + builder.columnCount == 0)
  }

  it should "reset builder after adding columns" in {
    val builder = TableBuilder.forColumns.addAll(goodRows).reset()
    assert(builder.rowCount + builder.columnCount == 0)
  }

  it should "not create row-oriented table with inconsistent data" in {
    assertThrows[IllegalArgumentException](buildTable(TableBuilder.forRows, badRows))
  }

  it should "not create column-oriented table with inconsistent data" in {
    assertThrows[IllegalArgumentException](buildTable(TableBuilder.forColumns, badColumns))
  }

  private def buildTable(builder: TableBuilder, data: Seq[Seq[String]]): Table =
    builder.addAll(data).build()
}
