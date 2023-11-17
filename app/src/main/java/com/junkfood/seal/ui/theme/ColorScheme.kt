package com.junkfood.seal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.junkfood.seal.ui.common.LocalDarkTheme
import com.kyant.monet.TonalPalettes
import com.kyant.monet.a1
import com.kyant.monet.a2
import com.kyant.monet.a3
import io.material.hct.Hct

@Composable
fun Number.autoDark(isDarkTheme: Boolean = LocalDarkTheme.current.isDarkTheme()): Double =
    if (!isDarkTheme) this.toDouble()
    else when (this.toDouble()) {
        6.0 -> 98.0
        10.0 -> 99.0
        20.0 -> 95.0
        25.0 -> 90.0
        30.0 -> 90.0
        40.0 -> 80.0
        50.0 -> 60.0
        60.0 -> 50.0
        70.0 -> 40.0
        80.0 -> 40.0
        90.0 -> 30.0
        95.0 -> 20.0
        98.0 -> 10.0
        99.0 -> 10.0
        100.0 -> 20.0
        else -> this.toDouble()
    }

object FixedAccentColors {
    val primaryFixed: Color
        @Composable get() = 90.a1
    val primaryFixedDim: Color
        @Composable get() = 80.a1
    val onPrimaryFixed: Color
        @Composable get() = 10.a1
    val onPrimaryFixedVariant: Color
        @Composable get() = 30.a1
    val secondaryFixed: Color
        @Composable get() = 90.a2
    val secondaryFixedDim: Color
        @Composable get() = 80.a2
    val onSecondaryFixed: Color
        @Composable get() = 10.a2
    val onSecondaryFixedVariant: Color
        @Composable get() = 30.a2
    val tertiaryFixed: Color
        @Composable get() = 90.a3
    val tertiaryFixedDim: Color
        @Composable get() = 80.a3
    val onTertiaryFixed: Color
        @Composable get() = 10.a3
    val onTertiaryFixedVariant: Color
        @Composable get() = 30.a3
}

const val DEFAULT_SEED_COLOR = 0xa3d48d

/**
 * @receiver Seed number used for generating color
 * @return a [Color] generated using [Hct] algorithm, harmonized with `primary` color
 */
@Composable
fun Int.generateLabelColor(): Color =
    Color(
        Hct.from(
            hue = (this % 360).toDouble(),
            chroma = 36.0,
            tone = 80.0
        ).toInt()
    ).harmonizeWithPrimary()


@Composable
fun TonalPalettes.toDynamicColorScheme(isLight: Boolean = !isSystemInDarkTheme()): ColorScheme {
    return if (isLight) {
        lightColorScheme(
            background = this neutral1 98.0,
            inverseOnSurface = this neutral1 95.0,
            inversePrimary = this accent1 80.0,
            inverseSurface = this neutral1 20.0,
            onBackground = this neutral1 10.0,
            onPrimary = this accent1 100.0,
            onPrimaryContainer = this accent1 10.0,
            onSecondary = this accent2 100.0,
            onSecondaryContainer = this accent2 10.0,
            onSurface = this neutral1 10.0,
            onSurfaceVariant = this neutral2 30.0,
            onTertiary = this accent3 100.0,
            onTertiaryContainer = this accent3 10.0,
            outline = this neutral2 50.0,
            outlineVariant = this neutral2 80.0,
            primary = this accent1 40.0,
            primaryContainer = this accent1 90.0,
//            scrim = this neutral1 0.0,
            secondary = this accent2 40.0,
            secondaryContainer = this accent2 90.0,
            surface = this neutral1 98.0,
            surfaceVariant = this neutral2 90.0,
            tertiary = this accent3 40.0,
            tertiaryContainer = this accent3 90.0,
            surfaceBright = this neutral1 98.0,
            surfaceDim = this neutral1 87.0,
            surfaceContainerLowest = this neutral1 100.0,
            surfaceContainerLow = this neutral1 96.0,
            surfaceContainer = this neutral1 94.0,
            surfaceContainerHigh = this neutral1 92.0,
            surfaceContainerHighest = this neutral1 90.0,
            surfaceTint = this accent1 40.0,
        )
    } else {
        darkColorScheme(
            background = this neutral1 6.0,
            inverseOnSurface = this neutral1 20.0,
            inversePrimary = this accent1 40.0,
            inverseSurface = this neutral1 90.0,
            onBackground = this neutral1 90.0,
            onPrimary = this accent1 20.0,
            onPrimaryContainer = this accent1 90.0,
            onSecondary = this accent2 20.0,
            onSecondaryContainer = this accent2 90.0,
            onSurface = this neutral1 90.0,
            onSurfaceVariant = this neutral2 80.0,
            onTertiary = this accent3 20.0,
            onTertiaryContainer = this accent3 90.0,
            outline = this neutral2 60.0,
            outlineVariant = this neutral2 30.0,
            primary = this accent1 80.0,
            primaryContainer = this accent1 30.0,
//            scrim = this neutral1 0.0,
            secondary = this accent2 80.0,
            secondaryContainer = this accent2 30.0,
            surface = this neutral1 6.0,
            surfaceVariant = this neutral2 30.0,
            tertiary = this accent3 80.0,
            tertiaryContainer = this accent3 30.0,
            surfaceBright = this neutral1 24.0,
            surfaceDim = this neutral1 6.0,
            surfaceContainerLowest = this neutral1 4.0,
            surfaceContainerLow = this neutral1 10.0,
            surfaceContainer = this neutral1 12.0,
            surfaceContainerHigh = this neutral1 17.0,
            surfaceContainerHighest = this neutral1 22.0,
            surfaceTint = this accent1 40.0,
        )
    }
}