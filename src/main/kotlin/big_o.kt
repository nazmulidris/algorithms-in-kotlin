/*
 * Copyright 2019 Nazmul Idris. All rights reserved.
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
 */

package big_o

import com.importre.crayon.blue
import com.importre.crayon.green
import com.importre.crayon.red
import utils.RuntimeStats
import utils.heading

fun main(args: Array<String>) {

  run {
    println("o(1)".heading())
    val list = listOf(null, "abc", "def")
    println("is first elem of list $list null? = ${isFirstElementNull(list)}")
  }

  run {
    println("o(n)".heading())
    val value = "efg"
    val list = listOf("abc", "def", "123", "xyz")
    with(RuntimeStats()) {
      print("is '$value` in list $list = ${containsValue(list,
                                                         value,
                                                         this)}")
      println(", $this")
    }
  }

  run {
    println("o(n^2)".heading())
    val list = listOf("abc", "def", "123", "abc", "123", "567")
    println("list $list contains dupes = ${containsDupes(list)}")
  }

  run {
    println("o(2^n)".heading())
    val value = 20
    with(RuntimeStats()) {
      print("fibonacci($value) = ${fibonacci(value, this)}")
      println(", $this")
    }
  }

  run {
    println("o(log n)".heading())
    println("binarySearch()")
    val item = "zany"
    val list = listOf(
        "nazmul",
        "idris",
        "maret",
        "john",
        "harry",
        "tom",
        "tony",
        "pepper",
        "andrew")
        .sorted()
    with(RuntimeStats()) {
      print("found: ${binarySearch(item, list, this)}")
      println(", $this")
    }
  }
}

/** O(log n) */
fun binarySearch(item: String,
                 sortedList: List<String>,
                 stats: RuntimeStats
): Boolean {
  println("\tbinarySearch(${item.toString().blue()}, ${sortedList.toString().green()}")
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
  print("\t\tprobe->")
  println("[index=$probeIndex, $probeItem]".red())

  // Does the probe match? If not, split and recurse
  when {
    item == probeItem -> return true
    item < probeItem  -> return binarySearch(
        item,
        sortedList.subList(0, probeIndex),
        stats)
    else              -> return binarySearch(item,
                                             sortedList.subList(probeIndex + 1,
                                                                size),
                                             stats)
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
fun containsValue(list: List<String>,
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