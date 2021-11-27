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
import color_console_log.blue
import color_console_log.green
import color_console_log.yellow
import support.*
import java.util.*

/** Makes it easy to run just this file. */
fun main() {
  BinaryTree.main(args = emptyArray())
}

object BinaryTree : Main {

  private const val padLength = 25

  override fun main(args: Array<String>) {
    "binary trees".printHeading()

    colorConsole {
      val rootNode: Node<Char> = buildTree()
      printLine(prefixWithTimestamp = false, spanSeparator = "") {
        span(Colors.White, "$rootNode")
      }

      // Pre-order traversal (recursive)
      with(mutableListOf<Char>()) {
        traversalPreOrder(rootNode, this)
        printLine(prefixWithTimestamp = false, spanSeparator = "") {
          span(Colors.Purple, "\npre-order traversal".padEnd(padLength))
          span(Colors.Yellow, " ➡ ")
          span(Colors.Green, this@with.joinToString(" 👉 "))
        }
      }

      // In-order traversal (recursive)
      with(mutableListOf<Char>()) {
        traversalInOrder(rootNode, this)
        printLine(prefixWithTimestamp = false, spanSeparator = "") {
          span(Colors.Purple, "\nin-order traversal".padEnd(padLength))
          span(Colors.Yellow, " ➡ ")
          span(Colors.Green, this@with.joinToString(" 👉 "))
        }
      }

      // Post-order traversal (recursive)
      with(mutableListOf<Char>()) {
        traversalPostOrder(rootNode, this)
        printLine(prefixWithTimestamp = false, spanSeparator = "") {
          span(Colors.Purple, "\npost-order traversal".padEnd(padLength))
          span(Colors.Yellow, " ➡ ")
          span(Colors.Green, this@with.joinToString(" 👉 "))
        }
      }

      // BFS traversal (using queue)
      with(breadthFirstTraversal(rootNode)) {
        val text = this@with.joinToString(" 👉 ") { "${it.value}, ${it.depth}" }
        printLine(prefixWithTimestamp = false, spanSeparator = "") {
          span(Colors.Purple, "\nBFS traversal".padEnd(padLength))
          span(Colors.Yellow, " ➡ ")
          span(Colors.Green, text)
        }
      }

      // BFS traversal (using queue) pretty print
      with(printBFSTraversal(rootNode)) {
        printLine(prefixWithTimestamp = false, spanSeparator = "") {
          span(Colors.Purple, "\nBFS traversal (pretty)".padEnd(padLength))
          span(Colors.Yellow, " ➡ \n")
          span(Colors.Green, this@with)
        }
      }

      // DFS traversal (using stack)
      with(depthFirstTraversal(rootNode)) {
        val text = this.joinToString(" 👉 ") { "${it.value}, ${it.depth}" }
        printLine(prefixWithTimestamp = false, spanSeparator = "") {
          span(Colors.Purple, "DFS traversal".padEnd(padLength))
          span(Colors.Yellow, " ➡ ")
          span(Colors.Green, text)
        }
      }
    }
  }

  /**
   * A neat trick for pre-order traversals: starting from the root, go around the tree
   * counterclockwise. Print each node when you pass its left side.
   */
  fun <T> traversalPreOrder(node: Node<T>?, list: MutableList<T>) {
    if (node != null) {
      list.add(node.value)
      traversalPreOrder(node.leftNode, list)
      traversalPreOrder(node.rightNode, list)
    }
  }

  /**
   * A neat trick for in-order traversals: starting from the root, go around the tree
   * counterclockwise. Print each node when you pass its bottom side.
   */
  fun <T> traversalInOrder(node: Node<T>?, list: MutableList<T>) {
    if (node != null) {
      traversalInOrder(node.leftNode, list)
      list.add(node.value)
      traversalInOrder(node.rightNode, list)
    }
  }

  /**
   * A neat trick for post-order traversals: starting from the root, go around the tree
   * counterclockwise. Print each node when you pass its right side.
   */
  fun <T> traversalPostOrder(node: Node<T>?, list: MutableList<T>) {
    if (node != null) {
      traversalPostOrder(node.leftNode, list)
      traversalPostOrder(node.rightNode, list)
      list.add(node.value)
    }
  }

  fun <T> depthFirstTraversal(root: Node<T>): MutableList<Node<T>> {
    val stack = LinkedList<Node<T>>()
    val traversalList = mutableListOf<Node<T>>()

    // Add first node.
    stack.push(root)

    // Use stack to create breadth first traversal.
    while (stack.isNotEmpty()) {
      val currentNode = stack.pop()
      val depth = currentNode.depth

      // Push right child to stack FIRST (so this will be processed LAST).
      if (currentNode.rightNode != null)
        stack.push(currentNode.rightNode!!.depth(depth + 1))

      // Push left child to stack LAST (so this will be processed FIRST).
      if (currentNode.leftNode != null)
        stack.push(currentNode.leftNode!!.depth(depth + 1))

      // Add to traversal list.
      traversalList.add(currentNode)
    }

    return traversalList
  }

  /**
   * Traverses the binary tree nodes in a sorted order.
   */
  fun <T> breadthFirstTraversal(root: Node<T>): MutableList<Node<T>> {
    val queue = LinkedList<Node<T>>()
    val traversalList = mutableListOf<Node<T>>()

    // Add first node
    queue.add(root)

    // Use stack to create breadth first traversal
    while (queue.isNotEmpty()) {
      val currentNode = queue.poll()
      val depth = currentNode.depth

      // Add left node first
      if (currentNode.leftNode != null)
        queue.add(currentNode.leftNode!!.depth(depth + 1))

      // Add right node next
      if (currentNode.rightNode != null)
        queue.add(currentNode.rightNode!!.depth(depth + 1))

      // Add the node to the traversal list
      traversalList.add(currentNode)
    }

    return traversalList
  }

  /**
   * Traverses the binary tree nodes in a sorted order.
   */
  fun <T> printBFSTraversal(root: Node<T>): String {

    val queue = LinkedList<Node<T>>()
    // Add first node.
    queue.add(root)

    val mapVisitedDepth = mutableMapOf<Int, MutableList<T>>()
    // Use stack to create breadth first traversal.
    while (queue.isNotEmpty()) {
      val currentNode = queue.poll()
      val depth = currentNode.depth

      // Add left node first.
      if (currentNode.leftNode != null)
        queue.add(currentNode.leftNode!!.depth(depth + 1))

      // Add right node next.
      if (currentNode.rightNode != null)
        queue.add(currentNode.rightNode!!.depth(depth + 1))

      // Decide whether to print crlf or not.
      mapVisitedDepth
        .computeIfAbsent(depth) { mutableListOf() }
        .add(currentNode.value)
    }

    val outputString = StringBuilder()

    for (entry in mapVisitedDepth) {
      outputString.append(entry.value.joinToString(", ", postfix = "\n"))
    }

    return outputString.toString()
  }

  /**
   * [Image of the generated tree](https://github.com/nazmulidris/algorithms-in-kotlin/blob/master/docs/images/binarytree.png)
   *        [A]
   *       /   \
   *     [B]    [C]
   *     / \    /  \
   *  [D]  [E] [F] [G]
   *               / \
   *             [H] [I]
   */
  fun buildTree(): Node<Char> {
    val a = Node('a', null, null)
    val b = Node('b', null, null)
    val c = Node('c', null, null)
    val d = Node('d', null, null)
    val e = Node('e', null, null)
    val f = Node('f', null, null)
    val g = Node('g', null, null)
    val h = Node('h', null, null)
    val i = Node('i', null, null)

    a.link(b, c)
    b.link(d, e)
    c.link(f, g)
    g.link(h, i)

    return a
  }

  data class Node<T>(
    val value: T,
    var leftNode: Node<T>?,
    var rightNode: Node<T>?,
    var depth: Int = 0
  ) {
    fun link(left: Node<T>?, right: Node<T>?) =
      this.apply { linkLeft(left).linkRight(right) }

    fun linkLeft(left: Node<T>?) = this.apply { leftNode = left }

    fun linkRight(right: Node<T>?) = this.apply { rightNode = right }

    fun depth(value: Int) = this.apply { depth = value }

    /**
     * Nodes on the left are in yellow, and those on the right are blue.
     */
    override fun toString(): String {
      return StringBuffer().apply {
        append("{${value.toString().green()}")
        if (leftNode != null)
          append(", ${leftNode.toString().yellow()}")
        if (rightNode != null)
          append(", ${rightNode.toString().blue()}}")
      }.toString()
    }
  }
}