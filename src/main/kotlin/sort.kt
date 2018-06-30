/*
 * Copyright 2018 Nazmul Idris All rights reserved.
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

package sort

import com.importre.crayon.brightCyan
import utils.RuntimeStats

fun main(args: Array<String>) {

    // bubble sort
    with(RuntimeStats()) {
        println("bubble_sort".brightCyan())
        val unsortedList = mutableListOf("a", "b", "d", "c")
        val stats = RuntimeStats()
        bubble_sort(unsortedList, stats)
        print("sorted list=$unsortedList")
        println(", $stats")
    }

    // insertion sort
    with(RuntimeStats()) {
        println("insertion_sort".brightCyan())
        val unsortedList = mutableListOf("abc", "xyz", "def", "nop", "ghi", "lmk")
        val stats = RuntimeStats()
        insertion_sort(unsortedList, stats)
        print("sorted list=$unsortedList")
        println(", $stats")
    }

}

/** O(n^2) */
fun insertion_sort(list: MutableList<String>, stats: RuntimeStats) {
    val size = list.size
    var sortedUpToIndex = 0
    for (cursor1 in 0 until size) {
        stats.operations++
        for (cursor2 in 0 until sortedUpToIndex) {
            stats.operations++
            val lhs = list[cursor1]
            val rhs = list[cursor2]
            // CAS
            stats.comparisons++
            if (rhs < lhs) {
                stats.swaps++
                list[cursor1] = rhs
                list[cursor2] = lhs
            }
        }
        sortedUpToIndex++
    }
}

/** O(n^2) */
fun bubble_sort(list: MutableList<String>, stats: RuntimeStats) {
    val size = list.size

    for (x in 0 until size) {
        for (y in x + 1 until size) {
            println("\tx=$x, y=$y")
            with(list) {
                stats.comparisons++
                if (get(y) < get(x)) {
                    stats.swaps++
                    val larger = get(y) // save larger value
                    val smaller = get(x) // save smaller value
                    set(x, larger)
                    set(y, smaller)
                }
            }
        }
    }
}