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

import support.Main
import support.log
import support.printHeading
import support.yellow
import java.util.*

object Cache : Main {

  enum class Type { LRU, MRU }

  override fun main(args: Array<String>) {
    run {
      "cache LRU".printHeading()
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
      "cache MRU".printHeading()
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

    run {
      "low cost insertion cache".printHeading()
      val cacheLRU = LowCostLRUCache<String, String>(3)
      cacheLRU.put("A", "A")
        .also { "cacheLRU.put(A, A), evicted: $it, $cacheLRU".log() }
      cacheLRU.put("B", "B")
        .also { "cacheLRU.put(B, B), evicted: $it, $cacheLRU".log() }
      cacheLRU.put("C", "C")
        .also { "cacheLRU.put(C, C), evicted: $it, $cacheLRU".log() }
      cacheLRU.put("D", "D")
        .also { "cacheLRU.put(D, D), evicted: $it, $cacheLRU".log() }
      cacheLRU.put("E", "E")
        .also { "cacheLRU.put(E, E), evicted: $it, $cacheLRU".log() }
    }

  }

  /**
   * This is a LRU cache that has no performance impact for cache insertions
   * once the capacity of the cache has been reached. For cache hit,
   * performance is O(1) and for cache eviction, it is O(1).
   */
  class LowCostLRUCache<K, V>(private val capacity: Int = 5) {
    private val cache = HashMap<K, V>()
    private val insertionOrder = LinkedList<K>()

    /**
     * [HashMap] put and remove is O(1).
     * More info: https://stackoverflow.com/a/4578039/2085356
     */
    fun put(key: K, value: V): K? {
      var evictedKey: K? = null
      if (cache.size >= capacity) {
        evictedKey = getKeyToEvict()
        cache.remove(evictedKey)
      }
      cache[key] = value
      insertionOrder.addLast(key)
      return evictedKey
    }

    /**
     * [HashMap] get is O(1).
     * More info: https://stackoverflow.com/a/4578039/2085356
     */
    fun get(key: K): V? = cache[key]

    /**
     * The head of the [insertionOrder] is removed, which is O(1), since this
     * is a linked list, and it's inexpensive to remove an item from head.
     * More info: https://stackoverflow.com/a/42849573/2085356
     */
    private fun getKeyToEvict(): K? = insertionOrder.removeFirst()

    override fun toString() = cache.toString()
  }

  class Cache<T>(val type: Type, val size: Int) {
    val map = mutableMapOf<T, Int>()
    var rank = 0

    fun put(value: T): T? {
      var evictedKey: T? = null

      when {
        map.containsKey(value) -> {
          // Increase rank of existing value.
          map[value] = rank++
        }
        map.size == size -> {
          // Remove the highest or lowest rank item in the map (depending on Type).
          evictedKey = findKeyToEvict()
          map.remove(evictedKey)
          map.put(value, rank++)
        }
        else -> {
          // Add the new item.
          map.put(value, rank++)
        }
      }

      return evictedKey
    }

    /**
     * LRU means evict the item in the map w/ the lowest rank.
     * MRU means evict the item in the map w/ the highest rank.
     */
    fun findKeyToEvict(): T? {
      val rankToEvict = when (type) {
        Type.MRU -> Collections.max(map.values)
        Type.LRU -> Collections.min(map.values)
      }
      val keyToEvict = map.entries.find { it.value == rankToEvict }?.key
      return keyToEvict
    }

    override fun toString(): String = StringBuilder().apply {
      val list = mutableListOf<String>().apply {
        for (entry in map) add("'${entry.key}'->rank=${entry.value}".yellow())
      }
      append(list.joinToString(", ", "{", "}"))
    }.toString()
  }
}