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

    run {
        println("o(1)")
        val list = listOf(null, "abc", "def")
        println("is first elem of list $list null? = ${isFirstElementNull(list)}")
    }

    run {
        println("o(n)")
        val value = "efg"
        val list = listOf("abc", "def", "123", "xyz")
        println("is '$value` in list $list = ${containsValue(list, value)}")
    }

    run {
        println("o(n^2)")
        val list = listOf("abc", "def", "123", "abc", "123", "567")
        println("list $list contains dupes = ${containsDupes(list)}")
    }
}

/**
 * The [containsDupes] function is O(N^2). For an input size of 6, 6x6 comparisons are made due to
 * two inner loops iterating over the size of the input list.
 * [More info in wiki](https://github.com/nazmulidris/algorithms-in-kotlin/wiki/Big-O-Notation).
 */
fun containsDupes(list: List<String>) = RuntimeStats().apply {
    with(list) {
        for (cursor1 in 0 until size) {
            for (cursor2 in 0 until size) {
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

/** Contains run time stats for measuring algorithm performance and holding return values */
data class RuntimeStats(var numberOfComparisons: Int = 0,
                        var numberOfDupes: Int = 0,
                        val dupeMap: MutableMap<String, Int> = mutableMapOf(),
                        var returnValue: Boolean = false)

/** O(1) */
fun isFirstElementNull(list: List<String?>) = list[0] == null

/** O(n) */
fun containsValue(list: List<String>, value: String): RuntimeStats =
        RuntimeStats().apply {
            list.forEach { it ->
                numberOfComparisons++
                if (it == value) {
                    returnValue = true
                    return@apply
                }
            }
            returnValue = false
            return@apply
        }
