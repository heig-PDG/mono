package tupperdate.android.ui.theme

import androidx.compose.foundation.layout.PaddingValues

operator fun PaddingValues.plus(other: PaddingValues) = PaddingValues(
    start + other.start,
    top + other.top,
    end + other.end,
    bottom + other.bottom
)
