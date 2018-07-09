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

package queue

import com.importre.crayon.*
import utils.heading

fun main(args: Array<String>) {
    println("circular queue".heading())
    val queue = RingBuffer<String>(4)
    println("add 1, ${queue.enqueue("1")}")
    println("add 2, ${queue.enqueue("2")}")
    println("add 3, ${queue.enqueue("3")}")
    println("add 4, ${queue.enqueue("4")}")
    println("dequeue = ${queue.dequeue()}, $queue")
    println("dequeue = ${queue.dequeue()}, $queue")
    println("add 5, ${queue.enqueue("5")}")
    println("add 6, ${queue.enqueue("6")}")
    try {
        println("add 7, ${queue.enqueue("7")}")
    } catch (e: Exception) {
        println(e.toString().red())
    }
    println("dequeue = ${queue.dequeue()}, $queue")
    println("dequeue = ${queue.dequeue()}, $queue")
    println("dequeue = ${queue.dequeue()}, $queue")
    println("dequeue = ${queue.dequeue()}, $queue")
    try {
        println("dequeue = ${queue.dequeue()}, $queue")
    } catch (e: Exception) {
        println(e.toString().red())
    }
    println("add 1, ${queue.enqueue("1")}")
    println("add 2, ${queue.enqueue("2")}")
    println("add 3, ${queue.enqueue("3")}")
}

/**
 * RingBuffer uses a fixed length array to implement a queue, where,
 * - [tail] Items are added to the tail
 * - [head] Items are removed from the head
 * - [capacity] Keeps track of how many items are currently in the queue
 *
 * References
 * - [More info - tutorial](http://tutorials.jenkov.com/java-performance/ring-buffer.html)
 * - [More info - video](https://www.youtube.com/watch?v=ia__kyuwGag)
 */
class RingBuffer<T>(val maxSize: Int = 10) {
    val array = mutableListOf<T?>().apply {
        for (index in 0 until maxSize) {
            add(null)
        }
    }

    // Head - remove from the head (read index)
    var head = 0

    // Tail - add to the tail (write index)
    var tail = 0

    // How many items are currently in the queue
    var capacity = 0

    fun clear() {
        head = 0
        tail = 0
    }

    fun enqueue(item: T): RingBuffer<T> {
        // Check if there's space before attempting to add the item
        if (capacity == maxSize) throw OverflowException("Can't add $item, queue is full")

        array[tail] = item
        // Loop around to the start of the array if there's a need for it
        tail = (tail + 1) % maxSize
        capacity++

        return this
    }

    fun dequeue(): T? {
        // Check if queue is empty before attempting to remove the item
        if (capacity == 0) throw UnderflowException("Queue is empty, can't dequeue()")

        val result = array[head]
        // Loop around to the start of the array if there's a need for it
        head = (head + 1) % maxSize
        capacity--

        return result
    }

    fun peek(): T? = array[head]

    /**
     * - Ordinarily, T > H ([isNormal]).
     * - However, when the queue loops over, then T < H ([isFlipped]).
     */
    fun isNormal(): Boolean {
        return tail > head
    }

    fun isFlipped(): Boolean {
        return tail < head
    }

    override fun toString(): String = StringBuffer().apply {
        var itemCount = capacity
        val items = mutableListOf<T?>()
        var readIndex = head
        while (itemCount > 0) {
            items.add(array[readIndex])
            readIndex = (readIndex + 1) % maxSize
            itemCount--
        }
        append(items.joinToString(", ",
                                  "{",
                                  "}").blue()).append(" [capacity = $capacity, H = $head, T = $tail]".yellow())
    }.toString()

}

class CircularQueueNaive<T>(val maxSize: Int = 10) {
    val array = mutableListOf<T?>().apply {
        for (index in 0 until maxSize) {
            add(null)
        }
    }

    // Head - remove from the head (read index)
    var head = 0

    // Tail - add to the tail (write index)
    var tail = 0

    // How many items are currently in the queue
    var size = 0

    fun enqueue(item: T): CircularQueueNaive<T> {
        // Check if there's space before attempting to add the item
        if (size == maxSize) throw OverflowException("Can't add $item, queue is full")

        // Loop around to the start of the array if there's a need for it
        if (tail == maxSize) {
            println("\tLooping tail to the start of the array".red())
            tail = 0
        }

        array[tail] = item
        tail++
        size++

        return this
    }

    fun dequeue(): T? {
        // Check if queue is empty before attempting to remove the item
        if (size == 0) throw UnderflowException("Queue is empty, can't dequeue()")

        // Loop around to the start of the array if there's a need for it
        if (head == maxSize) {
            println("\tLooping head to the start of the array".red())
            head = 0
        }

        val result = array[head]
        head++
        size--

        return result
    }

    fun peek(): T? = array[head]

    fun isNormal(): Boolean {
        return tail > head
    }

    /**
     * Ordinarily, T > H. However, when the queue loops over, then T < H.
     */
    override fun toString(): String = StringBuffer().apply {
        if (size == 0) append("{}")
        else
            with(mutableListOf<T?>()) {
                // Normal: T > H
                if (isNormal()) {
                    append("normal: ".yellow())
                    for (index in head until tail) {
                        add(array[index])
                    }
                }
                // Queue is looped over, H < T
                else {
                    append("looped: ".bold().yellow())
                    for (index in head until maxSize) {
                        add(array[index])
                    }
                    for (index in 0 until tail) {
                        add(array[index])
                    }
                }
                append(joinToString(", ", prefix = "{", postfix = "}").blue())
                append(" -> H=$head, T=$tail".green())
            }
    }.toString()

}

class OverflowException(msg: String) : RuntimeException(msg)
class UnderflowException(msg: String) : RuntimeException(msg)