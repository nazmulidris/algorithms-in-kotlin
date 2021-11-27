/*
 * Copyright 2021 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package algorithms

import color_console_log.*
import color_console_log.ColorConsoleContext.Companion.colorConsole
import support.Main
import support.RuntimeStats
import support.printHeading

/** Makes it easy to run just this file. */
fun main() {
  Strings.main(args = emptyArray())
}

object Strings : Main {

  override fun main(args: Array<String>) {
    "substring".printHeading()

    val arg1 = "Hello world".toCharArray()
    val arg2 = "o".toCharArray()

    val textArg1 = arg1.joinToString("・")
    val textArg2 = arg2.joinToString("・")

    colorConsole {
      RuntimeStats().also { stats ->
        val substringResult = substring(arg1, arg2, stats)
        prettyPrint(textArg1, textArg2, substringResult, stats)
      }

      RuntimeStats().also { stats ->
        val substringOptimizedResult = substring_optimized(arg1, arg2, stats)
        prettyPrint(textArg1, textArg2, substringOptimizedResult, stats)
      }
    }
  }

  private fun ColorConsoleContext.prettyPrint(
    textArg1: String,
    textArg2: String,
    result: Any,
    stats: RuntimeStats
  ) {
    printLine(spanSeparator = "", prefixWithTimestamp = false) {
      span(Colors.Purple, "substring(\n\t$textArg1, ")
      span(Colors.Cyan, "\n\t$textArg2".bold())
      span(Colors.Purple, "\n) = $result")
    }
    printLine(spanSeparator = "", prefixWithTimestamp = false) {
      span(Colors.ANSI_RESET, "$stats\n")
    }
  }

  /**
   * O(m * n), where m = str.size, and n = substr.size.
   *
   * This is an inefficient brute force algorithm which has quadratic complexity O(n^2).
   */
  fun substring(str: CharArray, substr: CharArray, stats: RuntimeStats): Any {
    // substr can't be longer than str.
    if (substr.size > str.size) return "not found"

    // Iterate str using cursor1 and for each index look ahead to see if matches exist for substr.
    var occurrences = 0
    for (cursor1 in 0 until str.size) {
      stats.operations++
      var matchCount = 0
      for (cursor2 in 0 until substr.size) {
        stats.operations++
        if (str[cursor1 + cursor2] == substr[cursor2]) matchCount++
        stats.comparisons++
      }
      // Found a match.
      if (matchCount == substr.size) occurrences++
    }

    return object {
      val numberOfMatches = occurrences
      val matchFound = occurrences > 0
      override fun toString(): String = StringBuilder().apply {
        append("{match found = $matchFound")
        append(", # matches = $numberOfMatches}")
      }.toString().blue()
    }
  }

  /**
   * O(m + n), where m = str.size, and n = substr.size.
   *
   * This function uses a deterministic finite automation (DFA) method which entails the use of a
   * state machine to keep track of progress in a game.
   */
  fun substring_optimized(
    str: CharArray,
    substr: CharArray,
    stats: RuntimeStats
  ): Any {

    class StateMachine(val pattern: CharArray) {
      var cursor = 0
      fun add(character: Char) {
        stats.comparisons++
        if (pattern[cursor] == character) cursor++
        else cursor = 0
      }

      fun isMatch() = cursor == pattern.size
      fun reset() {
        cursor = 0
      }
    }

    val stateMachine = StateMachine(substr)
    var numberOfOccurrences = 0

    for (cursor in 0 until str.size) {
      stats.operations++
      stateMachine.add(str[cursor])
      if (stateMachine.isMatch()) {
        stateMachine.reset()
        numberOfOccurrences++
      }
    }

    return object {
      val occurrences = numberOfOccurrences
      val matchFound = numberOfOccurrences > 0
      override fun toString(): String = StringBuilder().apply {
        append("{occurrences = $occurrences")
        append(", matchFound = $matchFound}")
      }.toString().brightBlue()
    }

  }
}