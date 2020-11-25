package tupperdate.android.ui.layout

import androidx.compose.animation.animate
import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.AnimationClockObservable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin

/**
 * Possible values of [SwipeStackState].
 */
enum class SwipeStackValue {
    /**
     * The state of the swipe stack when it is not swiped.
     */
    NotSwiped,

    /**
     * The state of the swipe stack when it's swiped at its start.
     */
    SwipedStart,

    /**
     * The state of the swipe stack when it's swiped at its end.
     */
    SwipedEnd,
}

/**
 * State of the [SwipeStack] composable.
 *
 * @param initialValue the initial value of the state.
 * @param clock the animation clock that will be used to drive the animations.
 * @param confirmStateChange optional callback invoked to confirm or veto a pending state change.
 */
@ExperimentalMaterialApi
@Stable
class SwipeStackState(
    initialValue: SwipeStackValue,
    clock: AnimationClockObservable,
    confirmStateChange: (SwipeStackValue) -> Boolean = { true },
) : SwipeableState<SwipeStackValue>(
    initialValue = initialValue,
    clock = clock,
    animationSpec = AnimationSpec,
    confirmStateChange = confirmStateChange,
) {
    /**
     * Whether the swipe stack is swiped.
     */
    val isSwiped: Boolean
        get() = value != SwipeStackValue.NotSwiped

    /**
     * Swipe the stack to the end with an animation.
     */
    fun swipeEnd() {
        animateTo(SwipeStackValue.SwipedEnd)
    }

    /**
     * Swipe the stack to the start with an animation.
     */
    fun swipeStart() {
        animateTo(SwipeStackValue.SwipedStart)
    }
}

@ExperimentalMaterialApi
private fun SwipeStackState.angle(): Float {
    val progress = progress
    val angle: (SwipeStackValue) -> Float = {
        when (it) {
            SwipeStackValue.NotSwiped -> 0f
            SwipeStackValue.SwipedEnd -> SwipeStackMaxAngle
            SwipeStackValue.SwipedStart -> -SwipeStackMaxAngle
        }
    }
    val delta = angle(progress.to) - angle(progress.from)
    return angle(progress.from) + (delta * progress.fraction)
}

/**
 * Create and remember a [SwipeStackState] with the default animation clock.
 *
 * @param initialValue the initial value of the state.
 * @param confirmStateChange optional callback invoked to confirm or veto a pending state change.
 */
@ExperimentalMaterialApi
@Composable
fun rememberSwipeStackState(
    initialValue: SwipeStackValue = SwipeStackValue.NotSwiped,
    confirmStateChange: (SwipeStackValue) -> Boolean = { true },
): SwipeStackState {
    val clock = AnimationClockAmbient.current.asDisposableClock()
    // TODO : Remember this across configuration changes, with rememberSavedInstanceState.
    return remember {
        SwipeStackState(initialValue, clock, confirmStateChange)
    }
}

/**
 * A swipe stack displays multiple swipeable items, such that they can be dismissed in both start
 * and end directions.
 *
 * @param items the items that are displayed in the stack. These items are keyed, and you must
 *              therefore make sure that they are all unique (for instance with a unique id).
 * @param modifier the modifier for this composable.
 * @param padding the amount of padding between the stacked items.
 * @param peekCount the count of items that are peeking behind the front item.
 * @param swipeStackState the [SwipeStackState] that is used for this composable.
 * @param content a lambda that generates the body to display for each item.
 */
@ExperimentalMaterialApi
@Composable
fun <T> SwipeStack(
    items: List<T>,
    modifier: Modifier = Modifier,
    padding: Dp = SwipeStackDefaultPadding,
    peekCount: Int = SwipeStackDefaultOverlapCount,
    swipeStackState: SwipeStackState = rememberSwipeStackState(),
    content: @Composable (T) -> Unit,
) {
    WithConstraints(modifier.padding(bottom = padding * peekCount)) {

        // Consider the maximum angle that is applied to stack elements as they are swiped in the
        // swipe distance.
        val angleInducedOffset =
            sin(SwipeStackMaxAngle * PI / 180) * constraints.maxHeight.toFloat()
        val startValue = -constraints.maxWidth.toFloat() - angleInducedOffset.toFloat()
        val restValue = 0f
        val endValue = constraints.maxWidth.toFloat() + angleInducedOffset.toFloat()

        val anchors = mapOf(
            startValue to SwipeStackValue.SwipedStart,
            restValue to SwipeStackValue.NotSwiped,
            endValue to SwipeStackValue.SwipedEnd,
        )
        Box(
            Modifier.swipeable(
                state = swipeStackState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal,
                resistance = null,
            )
        ) {
            for (index in items.indices.reversed()) {
                val item = items[index]
                key(item) {
                    val paddingOffset = animate(padding * (peekCount - min(index, peekCount)))
                    val offset = if (index == 0) swipeStackState.offset.value else 0f
                    val angle = if (index == 0) swipeStackState.angle() else 0f
                    Box(
                        Modifier
                            .drawLayer(translationX = offset, rotationZ = angle)
                            .offset(y = paddingOffset)
                    ) {
                        content(item)
                    }
                }
            }
        }
    }
}

private val AnimationSpec = SpringSpec<Float>(
    stiffness = Spring.StiffnessMedium,
    dampingRatio = Spring.DampingRatioNoBouncy,
)

private const val SwipeStackMaxAngle = 5f
private val SwipeStackDefaultPadding = 8.dp
private const val SwipeStackDefaultOverlapCount = 3

// PREVIEWS

@Composable
@Preview(showBackground = true)
@ExperimentalMaterialApi
private fun SwipeStackPreview() {
    val colors = arrayOf(
        Color(0xFFD4F1EA),
        Color(0xFFFFFFD1),
        Color(0xFFFFDDF8),
        Color(0xFFFFE2D1),
        Color(0xFFEBFFD1)
    )

    // Items and generator for sample items.
    var items by remember { mutableStateOf(listOf(0, 1, 2, 3, 4)) }
    var next by remember { mutableStateOf(5) }

    // Remember the swipe stack state.
    val state = rememberSwipeStackState()

    // TODO (alex) : I don't like doing it this way, but I'm not sure that there's a different way
    //               to change this state (at least with the SwipeableState<T> API).
    if (state.isSwiped) {
        items = items.drop(1).plus(next)
        next += 1
        state.snapTo(SwipeStackValue.NotSwiped)
    }

    MaterialTheme {
        SwipeStack(items, swipeStackState = state) { number ->
            Card(
                Modifier.padding(32.dp),
                elevation = 1.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = colors[number % colors.size],
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable(onClick = {
                            if (items[0] % 2 == 0) {
                                state.swipeEnd()
                            } else {
                                state.swipeStart()
                            }
                        }, indication = null),
                    Alignment.Center,
                ) {
                    Text("$number", style = MaterialTheme.typography.h5)
                }
            }
        }
    }
}
