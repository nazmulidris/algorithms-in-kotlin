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

package graphs

import utils.RuntimeStats
import utils.heading
import java.util.*

/**
 * [Image of the graph](https://cdncontribute.geeksforgeeks.org/wp-content/uploads/undirectedgraph.png).
 */
fun main(args: Array<String>) {
    with(RuntimeStats()) {
        println("graphs".heading())
        val graph = Graph<String>().apply {
            // Node / vertex "0"
            addEdge("0", "1")
            addEdge("0", "4")
            // Node / vertex "1"
            addEdge("1", "2")
            addEdge("1", "3")
            addEdge("1", "4")
            // Node / vertex "2"
            addEdge("2", "3")
            // Node / vertex "3"
            addEdge("3", "4")
        }
        println(graph.toString())
    }
}

/**
 * [More info](https://www.geeksforgeeks.org/graph-and-its-representations/).
 */
class Graph<T> {

    val adjacencyList: MutableMap<T, LinkedList<T>> = mutableMapOf()

    fun addEdge(src: T, dest: T) {
        adjacencyList[src] = adjacencyList[src] ?: LinkedList()
        adjacencyList[src]?.add(dest)
        adjacencyList[dest] = adjacencyList[dest] ?: LinkedList()
        adjacencyList[dest]?.add(src)
    }

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyList.keys) {
            append("$key -> ")
            adjacencyList[key]?.forEach { append("$it, ") }
            append("\n")
        }
    }.toString()

}

// BFT
// DFT