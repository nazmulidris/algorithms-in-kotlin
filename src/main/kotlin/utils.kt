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

package utils

import com.importre.crayon.*

/** Contains run time stats for measuring algorithm performance and holding return values */
data class RuntimeStats(var comparisons: Int = 0,
                        var operations: Int = 0,
                        var swaps: Int = 0,
                        var dupes: Int = 0,
                        val dupeMap: MutableMap<String, Int> = mutableMapOf()) {
    override fun toString(): String = StringBuffer().also {
        it.append("RuntimeStats".brightCyan())
        if (operations > 0) it.append(" [#${::operations.name}=$operations] "
                .brightRed()
        )
        if (comparisons > 0) it.append(" [#${::comparisons.name}=$comparisons] "
                .brightGreen()
        )
        if (swaps > 0) it.append(" [#${::swaps.name}=$swaps] "
                .brightYellow()
        )
        if (dupes > 0) it.append(" [#${::dupes.name}=$dupes] "
                .brightBlue()
        )
        if (dupeMap.isNotEmpty()) it.append(" [${::dupeMap.name}=$dupeMap] "
                .brightWhite()
        )
        it.append(")".brightCyan())
    }.toString()
}