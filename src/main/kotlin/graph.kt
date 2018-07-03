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

import utils.heading
import java.util.*

/**
 * [Image of the graph](https://cdncontribute.geeksforgeeks.org/wp-content/uploads/undirectedgraph.png).
 */
fun main(args: Array<String>) {
    println("graphs".heading())

    val graph = Graph<String>()
    graph.apply {
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

    println("breadth first search traversal".heading())
    println(bfs_traversal(graph, "0"))

    println("depth first search traversal".heading())
    println(dfs_traversal(graph, "0"))
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
            append(adjacencyList[key]?.joinToString(prefix = "[", postfix = "]"))
            append("\n")
        }
    }.toString()

}

/** Breadth first traversal leverages a Queue */
fun <T> bfs_traversal(graph: Graph<T>, startNode: T): String {
    // Mark all the vertices / nodes as not visited
    val visitedNodeMap = mutableMapOf<T, Boolean>().apply {
        graph.adjacencyList.keys.forEach { node -> put(node, false) }
    }

    // Create a queue for BFS
    val queue: Queue<T> = LinkedList()

    // Mark the current node as visited and enqueue it
    startNode.also { node ->
        queue.add(node)
        visitedNodeMap[node] = true
    }

    // Store the sequence in which nodes are visited, for return value
    val result = mutableListOf<T>()

    // Traverse the graph
    while (queue.isNotEmpty()) {
        // Get the head of the queue
        val currentNode = queue.poll()

        // Get all the adjacent vertices of the node. For each of them:
        // - If an adjacent has not been visited then mark it visited
        // - Add it to the queue
        val adjacencyList = graph.adjacencyList[currentNode]
        adjacencyList?.forEach { node ->
            val currentNodeHasBeenVisited = visitedNodeMap[node]!!
            if (!currentNodeHasBeenVisited) {
                visitedNodeMap[node] = true
                queue.add(node)
            }
        }

        // Store this for the result
        result.add(currentNode)
    }

    return result.joinToString()
}

/** Depth first traversal leverages a Stack */
fun <T> dfs_traversal(graph: Graph<T>, startNode: T): String {
    // Mark all the vertices / nodes as not visited
    val visitedNodeMap = mutableMapOf<T, Boolean>().apply {
        graph.adjacencyList.keys.forEach { node -> put(node, false) }
    }

    // Create a queue for DFS
    val stack: Stack<T> = Stack()

    // Mark the current node as visited and enqueue it
    startNode.also { node ->
        stack.push(node)
        visitedNodeMap[node] = true
    }

    // Store the sequence in which nodes are visited, for return value
    val result = mutableListOf<T>()

    // Traverse the graph
    while (stack.isNotEmpty()) {
        // Get the head of the queue
        val currentNode = stack.pop()

        // Get all the adjacent vertices of the node. For each of them:
        // - If an adjacent has not been visited then mark it visited
        // - Add it to the queue
        val adjacencyList = graph.adjacencyList[currentNode]
        adjacencyList?.forEach { node ->
            val currentNodeHasBeenVisited = visitedNodeMap[node]!!
            if (!currentNodeHasBeenVisited) {
                visitedNodeMap[node] = true
                stack.push(node)
            }
        }

        // Store this for the result
        result.add(currentNode)
    }

    return result.joinToString()
}