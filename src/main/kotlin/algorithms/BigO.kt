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

import color_console_log.ColorConsoleContext.Companion.colorConsole
import color_console_log.Colors
import color_console_log.brightMagenta
import support.Main
import support.RuntimeStats
import support.printHeading

/** Makes it easy to run just this file. */
fun main() {
  BigO.main(args = emptyArray())
}

object BigO : Main {

  override fun main(args: Array<String>) {

    run {
      "o(1)".printHeading()
      colorConsole {
        val list = listOf(null, "abc", "def")
        val isFirstElementNull = isFirstElementNull(list)

        printLine(spanSeparator = "", prefixWithTimestamp = false) {
          span(Colors.Purple, "is first elem of list $list null?")
          span(Colors.Yellow, " ➡ ")
          span(Colors.Blue, "$isFirstElementNull")
        }
      }
    }

    run {
      "o(n)".printHeading()
      RuntimeStats().also { stats ->
        colorConsole {
          val value = "efg"
          val list = listOf("abc", "def", "123", "xyz")
          val containsValueResult = containsValue(list, value, stats)

          printLine(spanSeparator = "", prefixWithTimestamp = false) {
            span(Colors.Purple, "is '$value` in list $list = $containsValueResult")
            span(Colors.Yellow, " ➡ ")
            span(Colors.Blue, "$stats")
          }
        }
      }
    }

    run {
      "o(n^2)".printHeading()
      RuntimeStats().also { stats ->
        colorConsole {
          val list = listOf("abc", "def", "123", "abc", "123", "567")
          checkListForDupes(list, stats)

          printLine(spanSeparator = "", prefixWithTimestamp = false) {
            span(
              Colors.Purple,
              "list $list " + if (stats.dupes > 0) "contains dupes" else "doesn't contain dupes"
            )
            span(Colors.Yellow, " ➡ ")
            span(Colors.White, "$stats")
          }
        }
      }
    }

    run {
      "o(2^n)".printHeading()
      RuntimeStats().also { stats ->
        colorConsole {
          val value = 20
          val fibonacciResult = fibonacci(value, stats)

          printLine(spanSeparator = "", prefixWithTimestamp = false) {
            span(Colors.Purple, "fibonacci($value) = $fibonacciResult")
            span(Colors.Yellow, " ➡ ")
            span(Colors.White, "$stats")
          }
        }
      }
    }

    run {
      "o(log n)".printHeading()
      RuntimeStats().also { stats ->
        colorConsole {
          val item = "zany"
          val list = listOf(
            "nazmul", "idris", "maret", "john", "harry", "tom", "tony", "pepper", "andrew"
          ).sorted()
          val binarySearchResult = binarySearch(item, list, stats)

          printLine(spanSeparator = "", prefixWithTimestamp = false) {
            span(Colors.ANSI_RESET, "search result:".brightMagenta())
            span(Colors.Yellow, " ➡ ")
            span(Colors.Green, "$binarySearchResult")
          }
        }
      }
    }
  }

  /** O(log n) */
  fun binarySearch(
    item: String,
    sortedList: List<String>,
    stats: RuntimeStats
  ): Boolean {
    colorConsole {
      printLine(spanSeparator = "", prefixWithTimestamp = false) {
        span(Colors.Purple, "binarySearch($item)")
        span(Colors.Yellow, " ➡ ")
        span(Colors.Green, "$sortedList")
      }
    }
    stats.operations++

    // Exit conditions (base cases)
    if (sortedList.isEmpty()) {
      return false
    }

    // Setup probe
    val size = sortedList.size
    val probeIndex = size / 2
    val probeItem = sortedList[probeIndex]
    stats.comparisons++
    colorConsole {
      printLine(spanSeparator = "", prefixWithTimestamp = false) {
        span(Colors.Blue, "✨ probe")
        span(Colors.Yellow, " ➡ ")
        span(Colors.Green, "[index=$probeIndex, $probeItem]")
      }
    }

    // Does the probe match? If not, split and recurse
    when {
      item == probeItem -> return true
      item < probeItem -> return binarySearch(
        item,
        sortedList.subList(0, probeIndex),
        stats
      )
      else -> return binarySearch(
        item,
        sortedList.subList(
          probeIndex + 1,
          size
        ),
        stats
      )
    }
  }

  /**
   * O(n^2)
   *
   * For an input size of 6, 6x6 comparisons are made due to two inner loops iterating over the
   * size of the input list.
   * [More info in wiki](https://github.com/nazmulidris/algorithms-in-kotlin/wiki/Big-O-Notation).
   */
  fun checkListForDupes(list: List<String>, stats: RuntimeStats): Unit {
    stats.apply {
      with(list) {
        for (cursor1 in 0 until size) {
          for (cursor2 in 0 until size) {
            comparisons++
            if (cursor1 != cursor2) {
              if (get(cursor1) == get(cursor2)) {
                dupes++
                get(cursor1).let {
                  dupeMap[it] = dupeMap[it] ?: 0 + 1
                }
              }
            }
          }
        }
      }
    }

  }

  /** O(1) */
  fun isFirstElementNull(list: List<String?>) = list[0] == null

  /** O(n) */
  fun containsValue(
    list: List<String>,
    value: String,
    stats: RuntimeStats
  ): Boolean {
    list.forEach { it ->
      stats.comparisons++
      if (it == value) {
        return true
      }
    }
    return false
  }

  /** O(2^n) */
  fun fibonacci(number: Int, stats: RuntimeStats): Int {
    stats.operations++
    return if (number <= 1) number
    else fibonacci(number - 1, stats) + fibonacci(number - 2, stats)
  }
}