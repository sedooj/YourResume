package com.sedooj.app_ui.pages.resume.create.components.skills

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sedooj.app_ui.navigation.config.SlideScreenTransitions
import com.sedooj.app_ui.pages.resume.create.components.skills.data.SkillsListPageContent
import com.sedooj.arch.Routes
import com.sedooj.ui_kit.R
import com.sedooj.ui_kit.screens.Screen

@Destination<RootGraph>(
    start = false,
    route = Routes.CREATE_RESUME_SKILLS,
    style = SlideScreenTransitions::class
)
@Composable
fun SkillsComponentPage(
    navigator: DestinationsNavigator,
) {
    BackHandler {}
    Screen(
        title = stringResource(id = R.string.skills),
        modifier = Modifier
            .fillMaxSize(),
        alignment = Alignment.Top,
        paddingValues = 0.dp,
        hasBackButton = true,
        onBack = {
            navigator.navigateUp()
        },
    ) {
        SkillsListPageContent(onSelect = {
            navigator.navigate(it) {
                launchSingleTop = true
            }
        })
    }

}