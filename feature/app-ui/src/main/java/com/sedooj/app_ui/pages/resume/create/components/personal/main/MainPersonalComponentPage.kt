package com.sedooj.app_ui.pages.resume.create.components.personal.main

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sedooj.api.domain.data.resume.usecase.CreateResumeUseCase
import com.sedooj.api.domain.data.types.GenderType
import com.sedooj.api.domain.data.types.MaritalStatus
import com.sedooj.app_ui.navigation.config.ScreensTransitions
import com.sedooj.app_ui.pages.resume.create.components.GenderConvertibleContainer
import com.sedooj.app_ui.pages.resume.create.components.MainComponent
import com.sedooj.app_ui.pages.resume.create.components.MaritalConvertibleContainer
import com.sedooj.arch.Routes
import com.sedooj.arch.viewmodel.auth.resume.CreateResumeViewModel
import com.sedooj.ui_kit.R
import com.sedooj.ui_kit.Screen

enum class PageFields(
    @StringRes
    val fieldName: Int,
    val defaultValue: FieldValue,
    val suggestions: List<CustomValue<ConvertibleValue>> = emptyList(),
) {
    FIRST_NAME(fieldName = R.string.firstname, defaultValue = TextValue("")),
    SECOND_NAME(fieldName = R.string.secondname, defaultValue = TextValue("")),
    GENDER(
        fieldName = R.string.gender_picker,
        defaultValue = CustomValue(GenderConvertibleContainer(GenderType.NOT_SELECTED)),
        suggestions = listOf(
            CustomValue(GenderConvertibleContainer(GenderType.NOT_SELECTED)),
            CustomValue(GenderConvertibleContainer(GenderType.MALE)),
            CustomValue(GenderConvertibleContainer(GenderType.FEMALE))
        ),
    ),
    MARITAL(
        fieldName = R.string.marital_picker,
        defaultValue = CustomValue(
            MaritalConvertibleContainer(
                MaritalStatus.NOT_SELECTED
            )
        ),
        suggestions = listOf(
            CustomValue(MaritalConvertibleContainer(MaritalStatus.NOT_SELECTED)),
            CustomValue(MaritalConvertibleContainer(MaritalStatus.NOT_MARRIED)),
            CustomValue(MaritalConvertibleContainer(MaritalStatus.MARRIED)),
            CustomValue(MaritalConvertibleContainer(MaritalStatus.FEMALE_NOT_MARRIED)),
            CustomValue(MaritalConvertibleContainer(MaritalStatus.FEMALE_MARRIED))
        )
    )
}

@Composable
fun rememberDataMap(initInfo: CreateResumeUseCase.PersonalInformation?): SnapshotStateMap<PageFields, FieldValue> {
    return remember {
        mutableStateMapOf(
            PageFields.FIRST_NAME to if (initInfo?.firstName != null) TextValue(initInfo.firstName) else TextValue(
                ""
            ),
            PageFields.SECOND_NAME to if (initInfo?.secondName != null) TextValue(initInfo.secondName) else TextValue(
                ""
            ),
            PageFields.GENDER to if (initInfo?.genderType != null) CustomValue(
                GenderConvertibleContainer(initInfo.genderType)
            ) else CustomValue(GenderConvertibleContainer(GenderType.NOT_SELECTED)),
            PageFields.MARITAL to if (initInfo?.maritalStatus != null) CustomValue(
                MaritalConvertibleContainer(initInfo.maritalStatus)
            ) else CustomValue(
                MaritalConvertibleContainer(MaritalStatus.NOT_SELECTED)
            )
        )
    }
}

@Destination<RootGraph>(
    start = false,
    route = Routes.CREATE_RESUME_PERSONAL_MAIN,
    style = ScreensTransitions::class
)
@Composable
fun MainPersonalComponentPage(
    destinationsNavigator: DestinationsNavigator,
    createResumeViewModel: CreateResumeViewModel,
) {
    var isAlertDialogVisible by remember { mutableStateOf(false) }
    BackHandler {
//        if (!isChangesSaved)
        isAlertDialogVisible = true
//        else
//            destinationsNavigator.navigateUp()
    }
    val personal = createResumeViewModel.uiState.collectAsState().value.personalInformation
    val data = rememberDataMap(personal)

    Screen(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        title = stringResource(id = R.string.vacancy),
        navigationButton = {
            IconButton(onClick = {
//                if (!isChangesSaved)
                isAlertDialogVisible = true
//                else
//                    destinationsNavigator.navigateUp()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = stringResource(
                        id = R.string.go_back
                    ),
                    Modifier.size(15.dp)
                )
            }
        },
        alignment = Alignment.Top,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
//            AnimatedVisibility(
//                visible = !isChangesSaved,
//                enter = scaleIn(tween(200)),
//                exit = scaleOut(tween(200))
//            ) {
//                FloatingActionButton(onClick = {
//
//                        isChangesSaved = true
//
//                }) {
//                    Icon(
//                        imageVector = Icons.Filled.Done,
//                        contentDescription = stringResource(id = R.string.save)
//                    )
//                }
//            }
        }
    ) {
        MainComponent(
            data = data,
            onValueChange = { field, value ->
                data[field] = value
            },
            modifier = Modifier.fillMaxSize(),
        )
        //TODO()
    }
}