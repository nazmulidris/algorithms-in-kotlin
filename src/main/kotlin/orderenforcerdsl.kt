import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.LinkedHashMap

enum class State {
    A, B, C
}

fun main() {
    StateMachine.create<State> {
        on(State.A) { println("A1") }
        on(State.B) { println("B1") }
        on(State.C) { println("C1") }
        on(State.C) { println("C2") }
    }.start {
        fulfill(State.A)
        fulfill(State.C)
        fulfill(State.B)
    }
}

class StateMachine<TState> private constructor(
    private val orderedStates: List<TState>,
    private val callbackMap: Map<TState, List<Runnable>>,
    private val executor: ExecutorService,
    private var prematureTransitions: MutableSet<TState>
) {
    // Record state transition.
    private var cursor = 0

    // Ensures that the executor is always shutdown.
    fun start(callback: Manipulator<TState>.() -> Unit) {
        callback(Manipulator(this))
        executor.shutdown()
    }

    // Exposed via the Manipulator.
    private fun fulfill(nextState: TState) {
        if (nextState == orderedStates[cursor]) {
            // Successfully transitioned to the next sequential state.
            executeCallbacksFor(nextState)
            // See if sequential states exist in prematureTransitions, and if so, run tasks until that state.
            while (cursor < orderedStates.size && prematureTransitions.contains(orderedStates[cursor])) {
                executeCallbacksFor(orderedStates[cursor])
            }
        } else {
            // Add the next state to the set of out-of-order (prematurely hit) states.
            prematureTransitions.add(nextState)
        }
    }

    private fun executeCallbacksFor(state: TState) {
        callbackMap[state]?.forEach { executor.submit(it) }
        prematureTransitions.remove(state)
        ++cursor
    }

    companion object {
        fun <TState : Enum<TState>> create(builderFunction: Builder<TState>.() -> Unit) =
            Builder<TState>().also { builderFunction(it) }.build()
    }

    class Manipulator<TState>(private val machine: StateMachine<TState>) {
        fun fulfill(nextState: TState) = machine.fulfill(nextState)
    }

    class Builder<TState> {
        private val states: LinkedHashMap<TState, MutableList<Runnable>> = LinkedHashMap()

        fun on(nextState: TState, callback: () -> Unit) {
            states.computeIfAbsent(nextState) { mutableListOf() }.add(Runnable(callback))
        }

        fun build(): StateMachine<TState> =
            StateMachine(states.keys.toList(), states, Executors.newSingleThreadExecutor(), TreeSet())
    }
}
