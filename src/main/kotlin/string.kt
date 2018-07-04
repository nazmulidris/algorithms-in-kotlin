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

import utils.RuntimeStats
import utils.heading

fun main(args: Array<String>) {
    with(RuntimeStats()) {
        println("substring".heading())
        val arg1 = "Hello world".toCharArray()
        var arg2 = "o w".toCharArray()
        println("substring(\n\t${arg1.joinToString("・")}, " +
                        "\n\t${arg2.joinToString("・")}" +
                        "\n) = ${substring(arg1, arg2)}")
    }
}

fun substring(str: CharArray, substr: CharArray): Boolean {
    // substr can't be longer than str
    if (substr.size > str.size) return false

    // Iterate str using cursor1 and for each index look ahead
    // to see if matches exist for substr
    for (cursor1 in 0 until str.size) {
        var matchCount = 0
        for (cursor2 in 0 until substr.size) {
            val index = cursor1 + cursor2
            // If index exceeds the size of str that means substr wasn't found
            if (index > str.size - 1) return false
            // If there's a match at index between the str and substr then remember it
            if (str[index] == substr[cursor2]) matchCount++
        }
        if (matchCount == substr.size) return true
    }

    return false
}