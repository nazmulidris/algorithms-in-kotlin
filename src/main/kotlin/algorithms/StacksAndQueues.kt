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

package algorithms

import color_console_log.ColorConsoleContext.Companion.colorConsole
import color_console_log.Colors
import support.Main
import support.printHeading
import java.util.*

object StacksAndQueues : Main {

  override fun main(args: Array<String>) {
    "Stacks & Queues".printHeading()
    val root = makeSampleFolders()
    val found = dfs("jdk11", root)
    colorConsole {
      printLine(spanSeparator = "", prefixWithTimestamp = false) {
        span(Colors.Green, "\njdk11 found: $found")
      }
    }
  }

  /*
  Create a tree of folders that need to be searched.

      root
        + opt
          + chrome
        + apps
          + idea
          + androidstudio
        + dev
          + java
            + jdk8
            + jdk11
  */
  fun makeSampleFolders(): Folder {
    val root = Folder("root")

    val opt = Folder("opt", root)
    val apps = Folder("apps", root)
    val dev = Folder("dev", root)

    Folder("idea", apps)
    Folder("androidstudio", apps)

    Folder("chrome", opt)

    Folder("java", dev).apply {
      Folder("jdk8", this)
      Folder("jdk11", this)
    }

    return root
  }


  fun dfs(name: String, root: Folder): Boolean {
    val stack = ArrayDeque<Folder>()
    stack.push(root)
    var found = false
    var count = 0
    while (stack.isNotEmpty()) {
      colorConsole {
        printLine(spanSeparator = "", prefixWithTimestamp = false) {
          span(Colors.Cyan, "...while loop iteration #${++count} ➡ ")
          span(Colors.Yellow, "stack=$stack\n")
          val currentFolder = stack.pop()
          span(Colors.Red, "\n👆️️ popped: " + currentFolder.toDetailedString() + "\n")
          if (currentFolder.isNamed(name)) {
            found = true
            span(Colors.Green, "\tfound a matching folder\n")
          }
          for (f in currentFolder.subFolders) {
            stack.push(f)
            span(Colors.Green, "👇 ️push: " + f.toDetailedString() + "\n")
          }
        }
      }
    }
    return found

  }

  class Folder {
    val name: String

    private var _subFolders: MutableList<Folder> = mutableListOf()
    val subFolders: MutableList<Folder>
      get() = Collections.unmodifiableList(_subFolders)

    fun toDetailedString(): String {
      return "{name: $name, subFolders: ${subFolders.size}}"
    }

    override fun toString(): String {
      return name
    }

    fun isNamed(nameArg: String): Boolean {
      return name == nameArg
    }

    constructor(name: String) {
      this.name = name
    }

    constructor(name: String, root: Folder) {
      this.name = name
      root.addSubfolder(this)
    }

    fun addSubfolders(vararg folders: Folder) {
      folders.forEach { addSubfolder(it) }
    }

    fun addSubfolder(f: Folder) {
      if (!_subFolders.contains(f)) {
        _subFolders.add(f)
      }
    }

    fun hasSubfolders(): Boolean {
      return !_subFolders.isEmpty()
    }

  }
}