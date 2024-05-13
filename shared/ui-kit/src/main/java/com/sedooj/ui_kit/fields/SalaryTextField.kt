package com.sedooj.ui_kit.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedooj.ui_kit.R

@Composable
fun SalaryTextField(
    label: Int,
    onValueChange: (String) -> Unit,
    value: String,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = if (value.isNotBlank()) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        onValueChange = {
            val newChar = it.removePrefix(value)
            if (newChar == "\b") {
                if (it != "") {
                    onValueChange(it)
                } else {
                    onValueChange("0")
                }
            }
            if (newChar != "-" && newChar != " " && newChar != ".") {
                if (newChar == ",") {
                    if (value.isBlank()) {
                        onValueChange(it.replaceBefore(it, "0"))
                    } else if ("," !in value) {
                        onValueChange(it)
                    }
                } else {
                    if (value.startsWith("0") && value.length > 1) {
                        onValueChange(it)
                    } else if (newChar == "0" && value.isBlank()) {
                        onValueChange(it)
                    } else {
                        onValueChange(it.removePrefix("0"))
                    }
                }
            }
        },
        label = {
            Text(text = stringResource(id = label))
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ruble),
                contentDescription = stringResource(
                    id = R.string.ruble
                ),
                modifier = Modifier.size(25.dp)
            )
        },
        supportingText = {
            if (value.isBlank())
                Text(
                    text = stringResource(id = R.string.field_must_not_be_empty),
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
        },
        singleLine = true,
        maxLines = 1,
        shape = RoundedCornerShape(10.dp),
        isError = (value.isBlank()),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = keyboardActions
    )
}