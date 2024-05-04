package com.sedooj.app_ui.pages.resume.create.components.generic

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sedooj.api.domain.data.resume.usecase.CreateResumeUseCase.PersonalInformation.*
import com.sedooj.api.domain.data.types.GenderType
import com.sedooj.api.domain.data.types.MaritalStatus
import com.sedooj.ui_kit.R

sealed class FieldValue

class TextValue(val text: String) : FieldValue()

class CustomValue<T : ConvertibleValue>(val value: T) : FieldValue()

class ConvertibleStringResValue(
    private val value: Int
): ConvertibleValue {

    @Composable
    override fun asStringValue(): String {
        return stringResource(id = value)
    }
}

interface ConvertibleValue {

    @Composable
    fun asStringValue(): String

}

interface ConvertibleEducationValue {

    @Composable
    fun asEducationList(): List<Education>

}

@Composable
fun FieldValue.asStringValue(): String {
    return when (this) {
        is CustomValue<*> -> value.asStringValue()
        is TextValue -> text
    }
}

@JvmInline
value class GenderConvertibleContainer(val value: GenderType) : ConvertibleValue {
    @Composable
    override fun asStringValue(): String {
        return stringResource(id = value.title)
    }

}

@JvmInline
value class MaritalConvertibleContainer(val value: MaritalStatus) : ConvertibleValue {
    @Composable
    override fun asStringValue(): String {
        return stringResource(id = value.title)
    }

}

@JvmInline
value class EducationConvertibleContainer(val value: List<Education>) : ConvertibleValue, ConvertibleEducationValue {

    @Composable
    override fun asStringValue(): String {
        TODO("Not yet implemented")
    }
    @Composable
    override fun asEducationList(): List<Education> {
        return value.map {
            Education(
                educationStage = it.educationStage,
                title = it.title,
                locationCity = it.locationCity,
                enterDate = it.enterDate,
                graduatedDate = it.graduatedDate,
                faculty = it.faculty,
                speciality = it.speciality
            )
        }
    }
}

@JvmInline
value class HasChildConvertibleContainer(val value: Boolean) : ConvertibleValue {
    @Composable
    override fun asStringValue(): String {
        return if (value) stringResource(id = R.string.has_children_yes) else stringResource(id = R.string.has_children_no)
    }

}

@JvmInline
value class SocialMediaConvertibleContainer(val value: List<SocialMedia>) : ConvertibleValue {
    @Composable
    override fun asStringValue(): String {
        return "tododo" // TODO()
    }
}