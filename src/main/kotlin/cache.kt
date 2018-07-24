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

package cache

import com.importre.crayon.yellow
import utils.heading

enum class Type { LRU, MRU }

fun main(args: Array<String>) {
    run {
        println("cache LRU".heading())
        val cacheLRU = Cache<String>(Type.LRU, 4)
        println("cacheLRU.put(A), evicted=${cacheLRU.put("A")}, $cacheLRU")
        println("cacheLRU.put(B), evicted=${cacheLRU.put("B")}, $cacheLRU")
        println("cacheLRU.put(C), evicted=${cacheLRU.put("C")}, $cacheLRU")
        println("cacheLRU.put(D), evicted=${cacheLRU.put("D")}, $cacheLRU")
        println("cacheLRU.put(E), evicted=${cacheLRU.put("E")}, $cacheLRU")
        println("cacheLRU.put(D), evicted=${cacheLRU.put("D")}, $cacheLRU")
        println("cacheLRU.put(F), evicted=${cacheLRU.put("F")}, $cacheLRU")
    }

    run {
        println("cache MRU".heading())
        val cacheMRU = Cache<String>(Type.MRU, 4)
        println("cacheMRU.put(A), evicted=${cacheMRU.put("A")}, $cacheMRU")
        println("cacheMRU.put(B), evicted=${cacheMRU.put("B")}, $cacheMRU")
        println("cacheMRU.put(C), evicted=${cacheMRU.put("C")}, $cacheMRU")
        println("cacheMRU.put(D), evicted=${cacheMRU.put("D")}, $cacheMRU")
        println("cacheMRU.put(E), evicted=${cacheMRU.put("E")}, $cacheMRU")
        println("cacheMRU.put(C), evicted=${cacheMRU.put("C")}, $cacheMRU")
        println("cacheMRU.put(D), evicted=${cacheMRU.put("D")}, $cacheMRU")
        println("cacheMRU.put(B), evicted=${cacheMRU.put("B")}, $cacheMRU")
    }

}

class Cache<T>(val type: Type, val size: Int) {
    val map = mutableMapOf<T, Int>()
    var rank = 0

    fun put(value: T): T? {
        var evictedKey: T? = null

        when {
            map.containsKey(value) -> {
                // Increase rank of existing value
                map[value] = rank++
            }
            map.size == size -> {
                // Remove the lowest rank item in the map
                evictedKey = findKeyToEvict()
                map.remove(evictedKey)
                map.put(value, rank++)
            }
            else -> {
                // Add the new item
                map.put(value, rank++)
            }
        }

        return evictedKey
    }

    /**
     * LRU means evict the item in the map w/ the lowest rank.
     * MRU means evict the item in the map w/ the highest rank.
     */
    fun findKeyToEvict(): T {
        var rankToEvict = map.values.first()
        var keyToEvict = map.keys.first()

        when (type) {
            Type.MRU -> {
                // Find the highest rank item
                for (entry in map) {
                    if (entry.value > rankToEvict) {
                        rankToEvict = entry.value
                        keyToEvict = entry.key
                    }
                }
            }
            Type.LRU -> {
                // Find the lowest rank item
                for (entry in map) {
                    if (entry.value < rankToEvict) {
                        rankToEvict = entry.value
                        keyToEvict = entry.key
                    }
                }
            }
        }

        return keyToEvict
    }

    override fun toString(): String = StringBuilder().apply {
        val list = mutableListOf<String>().apply {
            for (entry in map) add("'${entry.key}'->rank=${entry.value}".yellow())
        }
        append(list.joinToString(", ", "{", "}"))
    }.toString()
}