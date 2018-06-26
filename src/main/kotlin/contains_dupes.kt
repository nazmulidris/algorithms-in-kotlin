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

/**
 * O(N2) represents an algorithm whose performance is directly proportional to the square of the size
 * of the input data set. This is common with algorithms that involve nested iterations over the data
 * set. Deeper nested iterations will result in O(N3), O(N4) etc.
 *
 * The [containsDupes] function is O(N2). For an input size of 6, 6x6 comparisons are made.
 */
fun containsDupes(list: List<String>) = RuntimeStats().apply {
    with(list) {
        for (cursor1 in 0 until list.size) {
            for (cursor2 in 0 until list.size) {
                numberOfComparisons++
                if (cursor1 != cursor2) {
                    if (get(cursor1) == get(cursor2)) {
                        numberOfDupes++
                        get(cursor1).let {
                            val count = dupeMap[it] ?: 0
                            dupeMap[it] = count + 1
                        }
                    }
                }
            }
        }
    }
}

data class RuntimeStats(var numberOfComparisons: Int = 0,
                        var numberOfDupes: Int = 0,
                        val dupeMap: MutableMap<String, Int> = mutableMapOf())

fun main(args: Array<String>) {
    val list = listOf("abc", "def", "123", "abc", "123", "567")
    println("list $list contains dupes = ${containsDupes(list)}")
}