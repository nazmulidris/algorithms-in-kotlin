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

package string

import com.importre.crayon.brightBlue
import utils.RuntimeStats
import utils.heading

fun main(args: Array<String>) {
    with(RuntimeStats()) {
        println("substring".heading())
        val arg1 = "Hello world".toCharArray()
        var arg2 = "o".toCharArray()
        println("substring(\n\t${arg1.joinToString("・")}, " +
                        "\n\t${arg2.joinToString("・")}" +
                        "\n) = ${substring(arg1, arg2)}")
        println("substring_optimized(\n\t${arg1.joinToString("・")}, " +
                        "\n\t${arg2.joinToString("・")}" +
                        "\n) = ${substring_optimized(arg1, arg2)}")
    }
}

/**
 * O(str.size * substr.size)
 *
 * This is an inefficient brute force algorithm which has quadratic complexity.
 */
fun substring(str: CharArray, substr: CharArray): Any {
    // substr can't be longer than str
    if (substr.size > str.size) return false

    // Iterate str using cursor1 and for each index look ahead
    // to see if matches exist for substr
    var occurrences = 0
    for (cursor1 in 0 until str.size) {
        var matchCount = 0
        for (cursor2 in 0 until substr.size) {
            val index = cursor1 + cursor2
            // If index exceeds the size of str that means substr wasn't found
            if (index > str.size - 1) break
            // If there's a match at index between the str and substr then remember it
            if (str[index] == substr[cursor2]) matchCount++
        }
        // Found a match
        if (matchCount == substr.size) occurrences++
    }

    return object {
        val numberOfMatches = occurrences
        val matchFound = occurrences > 0
        override fun toString(): String = StringBuilder().apply {
            append("{match found = $matchFound")
            append(", # matches = $numberOfMatches}")
        }.toString().brightBlue()
    }
}

/* O(n) */
fun substring_optimized(str: CharArray, substr: CharArray): Boolean {
    val stateMachine = StateMachine(substr)
    for (cursor in 0 until str.size) {
        stateMachine.add(str[cursor])
        if (stateMachine.isMatch()) break
    }
    return stateMachine.isMatch()
}

class StateMachine(val pattern: CharArray) {
    var cursor = 0

    fun add(character: Char) {
        if (pattern[cursor] == character) cursor++
        else cursor = 0
    }

    fun isMatch() = cursor == pattern.size
}