package redux

interface Store<State, Action> {
    val state: State
    val dispatch: (Action) -> Unit

    operator fun component1() = state
    operator fun component2() = dispatch
}
