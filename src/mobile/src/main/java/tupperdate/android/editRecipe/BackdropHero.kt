package tupperdate.android.editRecipe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.enforce
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import androidx.ui.tooling.preview.Preview
import tupperdate.android.editRecipe.BackdropSection.Content
import tupperdate.android.editRecipe.BackdropSection.Hero
import kotlin.RequiresOptIn.Level.ERROR
import kotlin.math.max

@RequiresOptIn("This layout is not fully working yet.", level = ERROR)
annotation class ExperimentalBackdropHero

/**
 * A composable that displays some [content] that overlaps a [hero] backdrop. This pattern is
 * similar to the [Material Design Backdrop](https://material.io/components/backdrop) composable,
 * except that it's tailored for modal popups.
 *
 * @param hero the composable to be displayed as hero content.
 * @param content the composable to be displayed as content.
 * @param modifier the [Modifier] for this composable.
 * @param overlap the amount of overlap between the hero and the content.
 */
@ExperimentalBackdropHero
@Composable
fun BackdropHero(
    hero: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlap: Dp = 16.dp,
) {
    SubcomposeLayout<BackdropSection>(modifier) { constraints ->

        // Constrain and measure the Hero of the backdrop.
        // TODO : Check unbounded constraints handling.
        val heroMaxHeight = HeroMaxHeight + overlap
        val heroConstraints = Constraints(maxHeight = heroMaxHeight.toIntPx())
        val heroCoerced = constraints.enforce(heroConstraints)
        val heroPlaceables = subcompose(Hero, hero).fastMap { it.measure(heroCoerced) }
        val heroWidth = heroPlaceables.fastMaxBy { it.width }?.width ?: errorNoHero()
        val heroHeight = heroPlaceables.fastMaxBy { it.height }?.height ?: errorNoHero()

        // Constrain and measure the Content of the backdrop.
        // TODO : Add some scrolling support.
        // TODO : Handle unbounded constraints.
        val contentMaxHeightUnbounded = constraints.maxHeight - heroHeight + overlap.toIntPx()
        val contentMaxHeight = max(constraints.minHeight, contentMaxHeightUnbounded)
        val contentConstraints = Constraints(maxHeight = contentMaxHeight)
        val contentCoerced = constraints.enforce(contentConstraints)
        val contentPlaceables = subcompose(Content, content).fastMap { it.measure(contentCoerced) }
        val contentWidth = contentPlaceables.fastMaxBy { it.width }?.width ?: errorNoContent()

        // Layout the contents.
        layout(
            width = max(heroWidth, contentWidth),
            height = constraints.maxHeight,
        ) {
            heroPlaceables.fastForEach { it.placeRelative(0, 0) }
            contentPlaceables.fastForEach { it.placeRelative(0, heroHeight - overlap.toIntPx()) }
        }
    }
}

private enum class BackdropSection {
    Hero,
    Content,
}

private val HeroMaxHeight = 282.dp

private fun errorNoHero(): Nothing = error("No Hero @Composable provided.")
private fun errorNoContent(): Nothing = error("No Content @Composable provided.")

// Previews.

@ExperimentalBackdropHero
@Preview
@Composable
private fun BackdropHeroPreview() {
    BackdropHero(
        hero = {
            Spacer(
                Modifier
                    .border(4.dp, Color.White)
                    .background(Color.Green)
                    .fillMaxHeight()
            )
        },
        content = {
            Spacer(
                Modifier
                    .clip(RoundedCornerShape(topLeft = 48.dp, topRight = 48.dp))
                    .border(4.dp, Color.Yellow)
                    .background(Color.Red)
                    .fillMaxHeight()
            )
        },
        Modifier.fillMaxSize(),
    )
}
