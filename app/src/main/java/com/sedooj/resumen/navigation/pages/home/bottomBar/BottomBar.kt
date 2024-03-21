package com.sedooj.resumen.navigation.pages.home.bottomBar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination =
        navController.currentDestinationAsState().value ?: NavGraphs.root.startDestination
    NavigationBar(
        Modifier.padding(10.dp).clip(shape = RoundedCornerShape(10.dp))
    ) {
        BottomBarDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    if (currentDestination != destination.direction)
                        navController.navigate(destination.direction) {
                            launchSingleTop = true
                        }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = stringResource(destination.label),
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = {
                    Text(text = stringResource(id = destination.label))
                }
            )

        }
    }
}