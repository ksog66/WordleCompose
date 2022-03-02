package com.example.wordle_compose.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wordle_compose.R
import com.google.android.material.color.MaterialColors

@Composable
fun AppDrawer(
    allScreens: List<WordleDrawerTab>,
    onTabSelected: (WordleDrawerTab) -> Unit,
    currentScreen: WordleDrawerTab,
    closeDrawer: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.wordle_logo),
            contentDescription = stringResource(
                id = R.string.app_name
            )
        )

        Divider(color = MaterialTheme.colors.surface.copy(alpha = .2f))

        allScreens.forEach { screen ->
            DrawerButton(
                icon = screen.icon,
                label = stringResource(id = screen.title),
                isSelected = screen == currentScreen,
                action = {
                    onTabSelected(screen)
                    closeDrawer()
                })
        }
    }

}

@Composable
fun DrawerButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors

    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(0.6f)
    }

    val backgroundColor = if (isSelected) {
        colors.primary.copy(.12f)
    } else {
        Color.Transparent
    }

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()

    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = stringResource(id = R.string.navigation_icon),
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(textIconColor),
                    alpha = if (isSelected) 1f else .6f
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}