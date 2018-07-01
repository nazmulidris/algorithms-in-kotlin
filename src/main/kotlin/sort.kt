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

import utils.RuntimeStats
import utils.heading

fun main(args: Array<String>) {

    // bubble sort
    with(RuntimeStats()) {
        println("bubble_sort O(n^2)".heading())
        val unsortedList = mutableListOf("a", "b", "d", "c")
        val stats = RuntimeStats()
        bubble_sort(unsortedList, stats)
        print("sorted list=$unsortedList")
        println(", $stats")
    }

    // insertion sort
    with(RuntimeStats()) {
        println("insertion_sort O(n^2)".heading())
        val unsortedList = mutableListOf("abc", "xyz", "def", "nop", "ghi", "lmk")
        val stats = RuntimeStats()
        insertion_sort(unsortedList, stats)
        print("sorted list=$unsortedList")
        println(", $stats")
    }

    // merge sort
    with(RuntimeStats()) {
        println("merge_sort O(n * log(n))".heading())
        val unsortedList = mutableListOf("123", "989", "000", "981", "778", "996", "993", "781")
        val stats = RuntimeStats()
        val sortedList = merge_sort(unsortedList, stats)
        print("sorted list=$sortedList")
        println(", $stats")
    }

}

/** O(n * log(n)) */
fun merge_sort(list: MutableList<String>, stats: RuntimeStats): MutableList<String> {
    stats.operations++
    // Can't split lists anymore, so stop recursion
    val length = list.size
    if (length <= 1) return list

    // Split the list into two and recurse (divide)
    val middleIndex = length / 2
    val leftList = merge_sort(list.subList(0, middleIndex), stats)
    val rightList = merge_sort(list.subList(middleIndex, length), stats)

    // Merge the left and right lists (conquer)
    return merge(leftList, rightList, stats)
}

fun merge(leftList: MutableList<String>, rightList: MutableList<String>, stats: RuntimeStats): MutableList<String> {
    val result = mutableListOf<String>()
    var leftIndex = 0
    var rightIndex = 0

    while (leftIndex < leftList.size && rightIndex < rightList.size) {
        stats.comparisons++
        val lhs = leftList[leftIndex]
        val rhs = rightList[rightIndex]
        if (lhs < rhs) {
            stats.insertions++
            result.add(lhs)
            leftIndex++
        } else {
            stats.insertions++
            result.add(rhs)
            rightIndex++
        }
    }

    // Copy remaining elements of leftList (if any) into the result
    while (leftIndex < leftList.size) {
        stats.insertions++
        result.add(leftList[leftIndex])
        leftIndex++
    }

    // Copy remaining elements of rightList (if any) into the result
    while (rightIndex < rightList.size) {
        stats.insertions++
        result.add(rightList[rightIndex])
        rightIndex++
    }

    return result
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
            stats.comparisons++
            // CAS
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