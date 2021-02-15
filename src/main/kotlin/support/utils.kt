/*
 * Copyright 2021 Nazmul Idris. All rights reserved.
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
 *
 */

package support

import com.importre.crayon.*
import kotlin.jvm.internal.Reflection
import kotlin.reflect.full.declaredMemberFunctions

/** Contains run time stats for measuring algorithm performance and holding return values */
data class RuntimeStats(var comparisons: Int = 0,
                        var operations: Int = 0,
                        var swaps: Int = 0,
                        var insertions: Int = 0,
                        var dupes: Int = 0,
                        val dupeMap: MutableMap<String, Int> = mutableMapOf()
) {
  override fun toString(): String = StringBuffer().also {
    it.append("RuntimeStats(".brightCyan())

    val fields = listOf(::comparisons,
                        ::operations,
                        ::swaps,
                        ::insertions,
                        ::dupes)

    val stringList = mutableListOf<String>()

    for (field in fields) {
      with(field) {
        if (this.get() > 0)
          stringList.add("#${this.name}=${this.get()}".brightYellow())
      }
    }

    if (dupeMap.isNotEmpty())
      stringList.add("${::dupeMap.name}=$dupeMap".brightGreen())

    it.append(stringList.joinToString(separator = ", ",
                                      postfix = " }",
                                      prefix = "{ "))

    it.append(")".brightCyan())
  }.toString()
}

fun String.heading() = this.brightBlue().bgBrightBlack()

fun String.log() = System.out.println(this)

/**
 * References:
 * - [Tutorial 1](https://www.programmersought.com/article/5544327798/)
 * - [Tutorial 2](https://chercher.tech/kotlin/reflection-kotlin#companion-objects)
 * - [kotlin in Action book](https://livebook.manning.com/book/kotlin-in-action/chapter-10/126)
 */
object KotlinReflectHelper {
  fun invokeMethodForObjectBy(className: String, methodName: String, argv: Array<*>): Any? {
    val myClass = Class.forName(className)
    val myKotlinClass = Reflection.createKotlinClass(myClass)
    myKotlinClass.declaredMemberFunctions.forEach {
      if (it.name == methodName) return it.call(myKotlinClass.objectInstance, argv)
    }
    throw Exception("No method named : $methodName found for the $className object")
  }
}