package com.sedooj.app_ui.pages.resume.create.components.skills.components.programming.languages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.SkillsEditProgrammingLanguagesDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.onResult
import com.sedooj.api.domain.data.resume.usecase.CreateResumeUseCase
import com.sedooj.app_ui.navigation.config.SlideScreenTransitions
import com.sedooj.app_ui.pages.resume.create.components.skills.components.programming.languages.data.ProgrammingLanguageSkillsListPageContent
import com.sedooj.app_ui.pages.resume.create.components.skills.components.programming.languages.data.addProgrammingLanguageOrEdit
import com.sedooj.app_ui.pages.resume.create.components.skills.components.programming.languages.edit.data.ProgrammingLanguageSkill
import com.sedooj.arch.Routes
import com.sedooj.arch.viewmodel.auth.resume.CreateResumeViewModel
import com.sedooj.ui_kit.R
import com.sedooj.ui_kit.components.FloatingAddButton
import com.sedooj.ui_kit.screens.Screen

@Destination<RootGraph>(
    route = Routes.CREATE_RESUME_SKILLS_PROGRAMMING_LANGUAGE,
    style = SlideScreenTransitions::class
)
@Composable
fun ProgrammingLanguagesSkillsComponentPage(
    navigator: DestinationsNavigator,
    createResumeViewModel: CreateResumeViewModel,
    resultRecipient: ResultRecipient<SkillsEditProgrammingLanguagesDestination, ProgrammingLanguageSkill>,
) {
    BackHandler {}
    val programmingLanguageSkillList =
        createResumeViewModel.uiState.collectAsState().value.skillsInformation?.programmingLanguagesSkillsInformation
    resultRecipient.onResult {
        createResumeViewModel.saveProgrammingLanguageSkill(
            it.id, CreateResumeUseCase.SkillsInformation.ProgrammingLanguageSkillsInformation(
                languageName = it.languageName
            )
        )
    }
    Screen(
        title = stringResource(id = R.string.skills_programming_languages),
        modifier = Modifier.fillMaxSize(),
        alignment = if (programmingLanguageSkillList != null) Alignment.Top else Alignment.CenterVertically,
        hasBackButton = true,
        onBack = {
            navigator.navigateUp()
        },
        floatingActionButton = {
            FloatingAddButton(
                onClick = dropUnlessResumed {
                    addProgrammingLanguageOrEdit(
                        navigator = navigator,
                        id = programmingLanguageSkillList?.lastIndex?.plus(1) ?: 0
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        ProgrammingLanguageSkillsListPageContent(
            modifier = Modifier
                .fillMaxSize(),
            skillsList = programmingLanguageSkillList,
            onEdit = { i, language ->
                addProgrammingLanguageOrEdit(
                    navigator = navigator,
                    id = i,
                    skill = CreateResumeUseCase.SkillsInformation.ProgrammingLanguageSkillsInformation(
                        languageName = language.languageName
                    )
                )
            })
    }
}