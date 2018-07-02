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

package recursion

import utils.RuntimeStats
import utils.heading

fun main(args: Array<String>) {
    with(RuntimeStats()) {
        println("calculate factorial w/ recursion".heading())
        val arg = 5
        println("${::fact.name}($arg)=${fact(arg)}")
    }
}

fun fact(n: Int): Int = if (n == 0) 1 else n * fact(n - 1)