package com.junkfood.seal.ui.page.settings

import android.os.Build
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.junkfood.seal.R
import com.junkfood.seal.ui.component.LargeTopAppBar
import com.junkfood.seal.ui.component.PreferenceSwitch
import com.junkfood.seal.util.PreferenceUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearancePreferences(navController: NavController) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val context = LocalContext.current
    var dynamicColorSwitch by remember { mutableStateOf(Build.VERSION.SDK_INT >= 31) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(id = R.string.appearance),
                    )
                }, navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 8.dp),
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }, scrollBehavior = scrollBehavior
            )
        }, content = {
            Column(Modifier.padding(it)) {
                PreferenceSwitch(
                    title = stringResource(id = R.string.dynamic_color),
                    description = stringResource(id = R.string.dynamic_color_desc),
                    icon = Icons.Outlined.Palette,
                    onClick = {
                        dynamicColorSwitch = !dynamicColorSwitch
                        PreferenceUtil.updateValue(
                            PreferenceUtil.DYNAMIC_COLORS,
                            dynamicColorSwitch
                        )
                    }, enabled = false,
                    isChecked = dynamicColorSwitch
                )
            }
        })
}