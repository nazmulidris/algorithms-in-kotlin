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

fun main(args: Array<String>) {

    // bubble sort
    with(RuntimeStats()) {
        println("bubble_sort")
        val unsortedList = mutableListOf("a", "b", "d", "c")
        val stats = RuntimeStats()
        bubble_sort(unsortedList, stats)
        print("sorted list=$unsortedList")
        print(", + $stats")
    }

    // insertion sort
    with(RuntimeStats()) {
        println("\ninsertion_sort")
        val unsortedList = mutableListOf("abc", "xyz", "def", "nop", "ghi", "lmk")
        val stats = RuntimeStats()
        insertion_sort(unsortedList, stats)
        print("sorted list=$unsortedList")
        print(", + $stats")
    }

}

fun insertion_sort(unsortedList: MutableList<String>, stats: RuntimeStats) {

}

fun bubble_sort(unsortedList: MutableList<String>, stats: RuntimeStats) {
    val size = unsortedList.size

    for (x in 0 until size) {
        for (y in x + 1 until size) {
            println("x=$x, y=$y")
            with(unsortedList) {
                stats.numberOfComparisons++
                if (get(y) < get(x)) {
                    stats.numberOfSwaps++
                    val larger = get(y) // save larger value
                    val smaller = get(x) // save smaller value
                    set(x, larger)
                    set(y, smaller)
                }
            }
        }
    }
}