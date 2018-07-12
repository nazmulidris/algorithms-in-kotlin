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

package binarytree

import com.importre.crayon.blue
import com.importre.crayon.green
import com.importre.crayon.yellow
import utils.heading

fun main(args: Array<String>) {
    println("binary trees".heading())
    val rootNode: Node<Char> = buildTree()
    println(rootNode.toString())
}

/**
 * [Image of the generated tree](http://bit.ly/2ufLBWq)
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

data class Node<T>(val value: T, var leftNode: Node<T>?, var rightNode: Node<T>?) {
    fun link(left: Node<T>?, right: Node<T>?) = this.apply { linkLeft(left).linkRight(right) }

    fun linkLeft(left: Node<T>?) = this.apply { leftNode = left }

    fun linkRight(right: Node<T>?) = this.apply { rightNode = right }

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