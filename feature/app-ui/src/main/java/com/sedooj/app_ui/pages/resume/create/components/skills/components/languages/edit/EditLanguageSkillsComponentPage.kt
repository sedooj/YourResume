package com.sedooj.app_ui.pages.resume.create.components.skills.components.languages.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.sedooj.app_ui.navigation.config.SlideScreenTransitions
import com.sedooj.app_ui.pages.resume.create.components.skills.components.languages.edit.data.EditLanguageSkillPageContent
import com.sedooj.app_ui.pages.resume.create.components.skills.components.languages.edit.data.LanguageSkill
import com.sedooj.app_ui.pages.resume.create.components.skills.components.languages.edit.data.parseEditedLanguageData
import com.sedooj.app_ui.pages.resume.create.components.skills.components.languages.edit.data.rememberEditedLanguageDataMap
import com.sedooj.arch.Routes
import com.sedooj.ui_kit.R
import com.sedooj.ui_kit.components.FloatingSaveButton
import com.sedooj.ui_kit.components.LostDataAlert
import com.sedooj.ui_kit.screens.Screen

@Destination<RootGraph>(
    route = Routes.CREATE_RESUME_SKILLS_EDIT_LANGUAGE,
    style = SlideScreenTransitions::class
)
@Composable
fun EditLanguageSkillsComponentPage(
    languageSkill: LanguageSkill,
    resultNavigator: ResultBackNavigator<LanguageSkill>,
) {
    val data = rememberEditedLanguageDataMap(initInfo = languageSkill)
    var isDataSaved by remember { mutableStateOf(false) }
    var isDataEdited by remember { mutableStateOf(false) }
    var isLostDataAlertShow by remember { mutableStateOf(false) }
    BackHandler {}
    Screen(
        modifier = Modifier.fillMaxSize(),
        title = if (languageSkill.isEdit) languageSkill.languageName else stringResource(id = R.string.new_language),
        alignment = Alignment.Top,
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            val parsedData = parseEditedLanguageData(data = data, initInfo = languageSkill)
            FloatingSaveButton(
                onSave = {
                    resultNavigator.navigateBack(
                        result = parsedData
                    )
                    isDataSaved = true
                },
                isDataSaved = isDataSaved,
                isDataEdited = isDataEdited,
            )
        },
        hasBackButton = true,
        onBack = {
            if (isDataEdited)
                isLostDataAlertShow = true
            else
                resultNavigator.navigateBack()
        },
        alertDialog = {
            LostDataAlert(
                onDismiss = { isLostDataAlertShow = false },
                onConfirm = {
                    isLostDataAlertShow = false
                    resultNavigator.navigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        showAlert = isLostDataAlertShow
    ) {
        EditLanguageSkillPageContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            onEdit = { key, value ->
                data[key] = value
                isDataEdited = true
                isDataSaved = false
            },
            data = data
        )
    }
}