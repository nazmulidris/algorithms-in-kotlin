/*
 * Copyright 2019 Nazmul Idris. All rights reserved.
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

package orderenforcer

import utils.heading
import java.util.concurrent.Executors

fun main(args: Array<String>) {
  println("Order enforcer to schedule ordered tasks".heading())
  val enforcer = StateMachine<Stage>(Stage.A, Stage.B, Stage.C)

  enforcer.runWhen(Stage.A, Runnable { println("A1") })
  enforcer.runWhen(Stage.B, Runnable { println("B1") })
  enforcer.runWhen(Stage.C, Runnable { println("C1") })
  enforcer.runWhen(Stage.C, Runnable { println("C2") })

  enforcer.transition(Stage.A)
  enforcer.transition(Stage.C)
  enforcer.transition(Stage.B)

  enforcer.shutdown()
}

enum class Stage {
  A, B, C
}

class StateMachine<State>(vararg states: State) {
  private val pattern: Array<out State> = states
  private val runnableMap = HashMap<State, ArrayList<Runnable>>()
  private val executorService = Executors.newSingleThreadExecutor()
  fun runWhen(state: State, runnable: Runnable) =
      runnableMap.computeIfAbsent(state) { ArrayList() }.add(runnable)

  private fun runAllFor(state: State) =
      runnableMap[state]?.forEach { executorService.submit(it) }

  fun shutdown() = executorService.shutdown()

  // Record state transition.
  private var cursor = -1
  private var overflowTransitions = HashSet<State>()
  fun transition(newState: State) {
    if (pattern[cursor + 1] == newState) {
      // Successfully transitioned to the next sequential state.
      cursor++
      runAllFor(newState)
      // See if sequential states exist in overflowTransitions,
      // and if so run tasks UNTIL that state.
      checkOverflow()
    }
    else {
      // Skip a state (non sequential).
      overflowTransitions.add(newState)
    }
  }

  private fun checkOverflow() {
    val condition: () -> Boolean = {
      if (cursor + 1 >= pattern.size) false
      else overflowTransitions.contains(pattern[cursor + 1])
    }
    while (condition()) {
      val nextState = pattern[cursor + 1]
      overflowTransitions.remove(nextState)
      runAllFor(nextState)
      cursor++
    }
  }

}