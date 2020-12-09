package redux

/**
 * An interface that describes a [Reducer]. A [Reducer] takes a [State], and transforms it given
 * an [Action].
 *
 * It's implemented as a functional interface.
 */
fun interface Reducer<State, Action> {
    fun reduce(state: State, action: Action): State
}

fun <S1, S2, A> combine(
    a: Reducer<S1, A>,
    b: Reducer<S2, A>,
) = Reducer<Pair<S1, S2>, A> { (s1, s2), action ->
    Pair(
        a.reduce(s1, action),
        b.reduce(s2, action),
    )
}

fun <S1, S2, S3, A> combine(
    a: Reducer<S1, A>,
    b: Reducer<S2, A>,
    c: Reducer<S3, A>,
) = Reducer<Triple<S1, S2, S3>, A> { (s1, s2, s3), action ->
    Triple(
        a.reduce(s1, action),
        b.reduce(s2, action),
        c.reduce(s3, action),
    )
}

fun <S, A> combine(
    vararg reducer: Reducer<S, A>,
) = Reducer<Iterable<S>, A> { s, action ->
    s.mapIndexed { pos, item ->
        reducer[pos].reduce(item, action)
    }
}
