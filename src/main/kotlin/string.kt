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
    println("substring".heading())

    val arg1 = "Hello world".toCharArray()
    val arg2 = "o".toCharArray()

    with(RuntimeStats()) {
        print("substring(\n\t${arg1.joinToString("・")}, " +
                      "\n\t${arg2.joinToString("・")}" +
                      "\n) = ${substring(arg1, arg2, this)}")
        println(", $this")
    }

    with(RuntimeStats()) {
        print("substring_optimized(\n\t${arg1.joinToString("・")}, " +
                      "\n\t${arg2.joinToString("・")}" +
                      "\n) = ${substring_optimized(arg1, arg2, this)}")
        println(", $this")
    }

}

/**
 * O(m * n), where m = str.size, and n = substr.size.
 *
 * This is an inefficient brute force algorithm which has quadratic complexity O(n^2).
 */
fun substring(str: CharArray, substr: CharArray, stats: RuntimeStats): Any {
    // substr can't be longer than str.
    if (substr.size > str.size) return "not found"

    // Iterate str using cursor1 and for each index look ahead to see if matches exist for substr.
    var occurrences = 0
    for (cursor1 in 0 until str.size) {
        stats.operations++
        var matchCount = 0
        for (cursor2 in 0 until substr.size) {
            stats.operations++
            if (str[cursor1 + cursor2] == substr[cursor2]) matchCount++
            stats.comparisons++
        }
        // Found a match.
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

/**
 * O(m + n), where m = str.size, and n = substr.size.
 *
 * This function uses a deterministic finite automation (DFA) method which entails the use of a
 * state machine to keep track of progress in a game.
 */
fun substring_optimized(str: CharArray, substr: CharArray, stats: RuntimeStats): Any {

    class StateMachine(val pattern: CharArray) {
        var cursor = 0
        fun add(character: Char) {
            stats.comparisons++
            if (pattern[cursor] == character) cursor++
            else cursor = 0
        }

        fun isMatch() = cursor == pattern.size
        fun reset() {
            cursor = 0
        }
    }

    val stateMachine = StateMachine(substr)
    var numberOfOccurrences = 0

    for (cursor in 0 until str.size) {
        stats.operations++
        stateMachine.add(str[cursor])
        if (stateMachine.isMatch()) {
            stateMachine.reset()
            numberOfOccurrences++
        }
    }

    return object {
        val occurrences = numberOfOccurrences
        val matchFound = numberOfOccurrences > 0
        override fun toString(): String = StringBuilder().apply {
            append("{occurrences = $occurrences")
            append(", matchFound = $matchFound}")
        }.toString().brightBlue()
    }

}