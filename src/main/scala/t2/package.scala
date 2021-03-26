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

/**
 * Defines core types.
 *
 * == Getting Started ==
 *
 * Below is an example of how to use `TableBuilder` and `TableWriter`. Using
 * these utilities, it builds a `Table` and writes the content to stdout.
 *
 * {{{
 * // Build table with first row as column header
 * val table = t2.TableBuilder()
 *   .add("Effective Date", "Currency Code", "Exchange Rate")
 *   .add("2021-01-04", "USD", "0.690236")
 *   .add("2021-01-05", "USD", "0.690627")
 *   .add("2021-01-06", "USD", "0.689332")
 *   .build()
 *
 * // Create table writer with supplied configuration
 * val writer = t2.TableWriter(
 *   "ansiColorEnabled"    -> "true",
 *   "tableBorderColor"    -> Console.CYAN,
 *   "rowSeparatorColor"   -> Console.YELLOW,
 *   "columnHeaderEnabled" -> "true",
 *   "columnHeaderColor"   -> s"\${Console.YELLOW_B}\${Console.BLACK}",
 *   "columnRightAlign"    -> "2" // Right align column index 2
 * )
 *
 * // Write table to standard output
 * writer.write(System.out, table)
 * }}}
 *
 * The generated output would look something like the following if printed to
 * a terminal.
 *
 * <pre style="background: black; color: white;">
 * <span style="color: #0cc;">==============================================</span>
 * <span style="background: #cc0; color: black;"> Effective Date  Currency Code  Exchange Rate </span>
 * <span style="color: #cc0;">----------------------------------------------</span>
 *  2021-01-04      USD                 0.690236
 *  2021-01-05      USD                 0.690627
 *  2021-01-06      USD                 0.689332
 * <span style="color: #0cc;">==============================================</span>
 * </pre>
 *
 * Table output can be adjusted using configuration for such things as changing
 * cell spacing, characters used for table borders and row separator, and more.
 */
package object t2
