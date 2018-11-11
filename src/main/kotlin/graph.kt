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
    print(graph.toString())

    println("breadth first search traversal".heading())

    print("bfs_traversal(graph, '0', 5) = ")
    println(breadthFirstTraversal(graph, "0", 5))

    print("bfs_traversal(graph, '0', 1) = ")
    println(breadthFirstTraversal(graph, "0", 1))

    println("depth first search traversal".heading())
    println(depthFirstTraversal(graph, "0"))
}

/**
 * [More info](https://www.geeksforgeeks.org/graph-and-its-representations/).
 */
class Graph<T> {
    val adjacencyMap: MutableMap<T, MutableSet<T>> = mutableMapOf()

    fun addEdge(sourceVertex: T, destinationVertex: T) {
        // Add edge to source vertex.
        adjacencyMap.computeIfAbsent(sourceVertex) { mutableSetOf() }.add(destinationVertex)
        // Add edge to destination vertex.
        adjacencyMap.computeIfAbsent(destinationVertex) { mutableSetOf() }.add(sourceVertex)
    }

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }.toString()
}

/**
 * Breadth first traversal leverages a [Queue] (FIFO).
 */
fun <T> breadthFirstTraversal(graph: Graph<T>, startNode: T, maxDepth: Int): String {
    // Mark all the vertices / nodes as not visited.
    val visitedMap = mutableMapOf<T, Boolean>().apply {
        graph.adjacencyMap.keys.forEach { node -> put(node, false) }
    }
    // Keep track of the depth of each node, so that more than maxDepth nodes aren't visited.
    val depthMap = mutableMapOf<T, Int>().apply {
        graph.adjacencyMap.keys.forEach { node -> put(node, Int.MAX_VALUE) }
    }

    // Create a queue for BFS.
    val queue: Deque<T> = LinkedList()
    fun T.addToQueue(depth: Int) {
        // Add to the tail of the queue.
        queue.add(this)
        // Record the depth of this node.
        depthMap[this] = depth
    }

    // Initial step -> add the startNode to the queue.
    startNode.addToQueue(0)

    // Store the sequence in which nodes are visited, for return value.
    val traversalList = mutableListOf<T>()

    // Traverse the graph
    while (queue.isNotEmpty()) {
        // Peek and remove the item at the head of the queue.
        val currentNode = queue.remove()
        val depth = depthMap[currentNode]!!

        if (depth <= maxDepth) {

            if (!visitedMap[currentNode]!!) {

                // Mark the current node visited and add to traversal list.
                visitedMap[currentNode] = true
                traversalList.add(currentNode)

                // Add nodes in the adjacency map
                graph.adjacencyMap[currentNode]?.forEach { it.addToQueue(depth + 1) }
            }

        }

    }

    return traversalList.joinToString()
}

/**
 * Depth first traversal leverages a [Stack] (LIFO).
 *
 * It's possible to use recursion instead of using this iterative implementation using a [Stack].
 * Also, this algorithm is almost the same above, except for [Stack] is LIFO and [Queue] is FIFO.
 *
 * [More info](https://stackoverflow.com/a/35031174/2085356).
 */
fun <T> depthFirstTraversal(graph: Graph<T>, startNode: T): String {
    // Mark all the vertices / nodes as not visited
    val visitedMap = mutableMapOf<T, Boolean>().apply {
        graph.adjacencyMap.keys.forEach { node -> put(node, false) }
    }

    // Create a stack for DFS
    val stack: Deque<T> = LinkedList()

    // Initial step -> add the startNode to the stack
    startNode.also { node ->
        stack.push(node)
    }

    // Store the sequence in which nodes are visited, for return value
    val traversalList = mutableListOf<T>()

    // Traverse the graph
    while (stack.isNotEmpty()) {
        // Pop the node off the top of the stack
        val currentNode = stack.pop()

        if (!visitedMap[currentNode]!!) {

            // Store this for the result
            traversalList.add(currentNode)

            // Mark the current node visited and add to the traversal list
            visitedMap[currentNode] = true

            // Add nodes in the adjacency map
            graph.adjacencyMap[currentNode]?.forEach { node ->
                stack.push(node)
            }

        }

    }

    return traversalList.joinToString()
}