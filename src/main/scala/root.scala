/**
 * Defines types to create, access, and print tables.
 *
 * ## Usage
 *
 * Below is an example of how to use `TableBuilder` and `TableWriter`. Using
 * these utilities, it builds a `Table` and writes the content to stdout.
 *
 * {{{
 * // Build table with first row as table header
 * val table = t2.TableBuilder()
 *   .add("#", "Effective Date", "Currency Code", "Exchange Rate")
 *   .add("1", "2021-01-04", "USD", "0.690236")
 *   .add("2", "2021-01-05", "USD", "0.690627")
 *   .add("3", "2021-01-06", "USD", "0.689332")
 *   .build()
 *
 * // Create table writer with supplied configuration
 * val writer = t2.TableWriter(
 *   "ansiColorEnabled" -> "true",
 *   "tableBorderColor" -> "cyan",
 *   "tableHeaderColor" -> "black,yellowBackground",
 *   "bodyRuleColor"    -> "yellow",
 *   "rowHeaderEnabled" -> "true",
 *   "rowHeaderColor"   -> "bold,cyan",
 *   "columnRightAlign" -> "0,3" // Right align first and last columns
 * )
 *
 * // Write table to stdout
 * writer.write(System.out, table)
 * }}}
 *
 * The generated output would look something like the following if printed to a
 * color-enabled terminal.
 *
 * <pre style="background: black; color: white;">
 * <span style="color: #0cc;">===================================================</span>
 * <span style="background: #cc0; color: black;">  #  Effective Date  Currency Code  Exchange Rate  </span>
 * <span style="color: #cc0;">---------------------------------------------------</span>
 * <span style="color: #0cc; font-weight: bold;">  1 </span> 2021-01-04      USD                 0.690236
 * <span style="color: #0cc; font-weight: bold;">  2 </span> 2021-01-05      USD                 0.690627
 * <span style="color: #0cc; font-weight: bold;">  3 </span> 2021-01-06      USD                 0.689332
 * <span style="color: #0cc;">===================================================</span>
 * </pre>
 */
