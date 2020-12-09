package redux

import androidx.compose.runtime.*

@Composable
private inline fun <A> state(
    policy: SnapshotMutationPolicy<A> = structuralEqualityPolicy(),
    init: @ComposableContract(preventCapture = true) () -> A,
): MutableState<A> {
    return remember { mutableStateOf(init(), policy) }
}

@Composable
fun <State, Action> rememberReducer(
    initial: State,
    policy: SnapshotMutationPolicy<State> = structuralEqualityPolicy(),
    f: Reducer<State, Action>,
): Store<State, Action> {
    val (state, setState) = remember { mutableStateOf(initial, policy) }
    return SnapshotStore(
        state = state,
        dispatch = { setState(f.reduce(state, it)) }
    )
}

/**
 * A simple implementation of a [Store].
 */
private data class SnapshotStore<State, Action>(
    override val state: State,
    override val dispatch: (Action) -> Unit,
) : Store<State, Action>
