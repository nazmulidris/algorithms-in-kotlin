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
import support.printHeading

/** Makes it easy to run just this file. */
fun main() {
  Dynamic.main(args = emptyArray())
}

object Dynamic : Main {

  override fun main(args: Array<String>) {
    "dynamic programming".printHeading()

    run {
      val coins = Coins()
      val total = 49
      numCoins_nonrecursive(total, coins)
      colorConsole {
        printLine(prefixWithTimestamp = false, spanSeparator = "") {
          span(Colors.Purple, "numCoins_nonrecursive($total)")
          span(Colors.Yellow, " ➡ ")
          span(Colors.Blue, "$coins")
        }
      }
    }

    run {
      "numCoins (recursive)".printHeading()
      val coins = mutableMapOf<Int, Int>()
      val totalNumberOfCoins =
        numCoins(
          49,
          listOf(5, 1, 7, 11).sortedDescending(),
          coins
        )
      println("\ntotal # coins = $totalNumberOfCoins")
      println("{key:denomination = val:amount)}".white())
      println(coins.toString().cyan().bold())
    }

    run {
      "calculate factorial w/ recursion".printHeading()
      val arg = 5
      println("${::fact.name}($arg)".magenta() + " = ".yellow() + "${fact(arg)}".cyan())
    }

  }

  /**
   * Use the process of induction to figure the min number of coins it takes to come up with the
   * given [total]. The coin denominations you can used are in [denominations]; this list must
   * be sorted already (in descending order), eg: [11, 7, 5, 1].
   * [coinsUsedMap] has keys that represent the denomination, and value that represent the number of
   * coins used of that denomination.
   */
  fun numCoins(
    total: Int,
    denominations: List<Int>,
    coinsUsedMap: MutableMap<Int, Int>
  ): Int {
    // Show the function call stack.
    println(
      "🪙 numCoins(total=$total, denominations=$denominations) ⇢ ".magenta()
        + " $coinsUsedMap ".bgBrightCyan().black().bold()
    )

    // Stop recursion when these simple exit conditions are met.
    if (total == 0) return 0
    if (denominations.isEmpty()) return 0

    // Breakdown the problem further.
    val coinDenomination = denominations[0]
    val coinsUsed = total / coinDenomination

    // Remember how many coins of which denomination are used.
    if (coinsUsed > 0) coinsUsedMap.computeIfAbsent(coinDenomination) { coinsUsed }

    // Breakdown the problem into smaller chunk using recursion.
    return coinsUsed +
      numCoins(
        total = total - coinsUsed * coinDenomination,
        denominations = with(denominations) { subList(1, size) },
        coinsUsedMap = coinsUsedMap
      )
  }

  /**
   * Brute force version of the recursive function [numCoins] above.
   *
   * - As you can see, there's a lot more code and complexity to compensate
   *   for very simplistic logic.
   * - The coin denominations are hard coded to be 1, 5, 7, 11.
   */
  fun numCoins_nonrecursive(total: Int, coins: Coins) {
    // Exit condition
    if (total == 0) return

    var currencyRemoved = 0

    // Remove all the 11 coins.
    val numberOf11s = (total / 11)
    if (numberOf11s > 0) {
      coins.numberOf11s += numberOf11s
      currencyRemoved += numberOf11s * 11
    }

    // Remove all the 7 coins.
    val numberOf7s = (total - currencyRemoved) / 7
    if (numberOf7s > 0) {
      coins.numberOf7s += numberOf7s
      currencyRemoved += numberOf7s * 7
    }

    // Remove all the 5 coins.
    val numberOf5s = (total - currencyRemoved) / 5
    if (numberOf5s > 0) {
      coins.numberOf5s += numberOf5s
      currencyRemoved += numberOf5s * 5
    }

    // Remove all the 1 coins.
    val numberOf1s = (total - currencyRemoved) / 1
    if (numberOf1s > 0) {
      coins.numberOf1s += numberOf1s
      currencyRemoved += numberOf1s * 1
    }

  }

  data class Coins(
    var numberOf1s: Int = 0,
    var numberOf5s: Int = 0,
    var numberOf7s: Int = 0,
    var numberOf11s: Int = 0
  ) {
    override fun toString() = StringBuilder().apply {
      val result = mutableListOf<String>()
      arrayOf(
        ::numberOf1s,
        ::numberOf5s,
        ::numberOf7s,
        ::numberOf11s
      ).forEach {
        if (it.get() > 0)
          result += it.name.blue() +
            " x ".yellow() +
            " ${it.get()} ".bgBrightYellow().black()
      }
      append(result.joinToString(", ", "{", "}"))
    }.toString()
  }

  fun fact(n: Int): Int = if (n == 0) 1 else n * fact(n - 1)
}