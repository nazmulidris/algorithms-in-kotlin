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
        val text1 = "is first elem of list $list null? = "
        val text2 = "${isFirstElementNull(list)}"

        printLine(spanSeparator = "", prefixWithTimestamp = false) {
          span(Colors.Purple, text1)
          span(Colors.Yellow, " ➡ ")
          span(Colors.Blue, text2)
        }
      }
    }

    run {
      "o(n)".printHeading()
      RuntimeStats().also { rs ->
        colorConsole {
          val value = "efg"
          val list = listOf("abc", "def", "123", "xyz")
          val text1 = "is '$value` in list $list = ${containsValue(list, value, rs)}"
          val text2 = "$rs"

          printLine(spanSeparator = "", prefixWithTimestamp = false) {
            span(Colors.Purple, text1)
            span(Colors.Yellow, " ➡ ")
            span(Colors.Blue, text2)
          }
        }
      }
    }

    run {
      "o(n^2)".printHeading()
      colorConsole {
        val list = listOf("abc", "def", "123", "abc", "123", "567")
        val stats = containsDupes(list)
        val text1 =
          "list $list" + if (stats.dupes > 0) "contains dupes" else "does not contain dupes"
        val text2 = "$stats"

        printLine(spanSeparator = "", prefixWithTimestamp = false) {
          span(Colors.Purple, text1)
          span(Colors.Yellow, " ➡ ")
          span(Colors.White, text2)
        }
      }
    }

    run {
      "o(2^n)".printHeading()
      val value = 20
      with(RuntimeStats()) {
        colorConsole {
          printLine(spanSeparator = "", prefixWithTimestamp = false) {
            val text1 = "fibonacci($value) = ${fibonacci(value, this@with)}"
            val text2 = this@with.toString()
            span(Colors.Purple, text1)
            span(Colors.Yellow, " ➡ ")
            span(Colors.White, text2)
          }
        }
      }
    }

    run {
      "o(log n)".printHeading()
      colorConsole {
        printLine(spanSeparator = "", prefixWithTimestamp = false) {
          span(Colors.Purple, "binarySearch()")
        }
      }
      val item = "zany"
      val list = listOf(
        "nazmul", "idris", "maret", "john", "harry", "tom", "tony", "pepper", "andrew"
      ).sorted()
      with(RuntimeStats()) {
        colorConsole {
          val result = binarySearch(item, list, this@with)
          printLine(spanSeparator = "", prefixWithTimestamp = false) {
            span(Colors.Purple, "found:")
            span(Colors.Yellow, " ➡ ")
            span(Colors.Green, "$result")
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
        span(Colors.Blue, "\nbinarySearch($item)")
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
  fun containsDupes(list: List<String>) = RuntimeStats().apply {
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