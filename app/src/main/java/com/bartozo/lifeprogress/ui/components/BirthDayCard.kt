package com.bartozo.lifeprogress.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.util.rangeOfYearsFromNowTo
import com.google.android.material.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDayCard(
    modifier: Modifier = Modifier,
    birthDay: LocalDate,
    onBirthDaySelect: (LocalDate) -> Unit
) {
    val dialogState = rememberMaterialDialogState()

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.mtrl_picker_confirm),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            negativeButton(
                text = stringResource(id = R.string.mtrl_picker_cancel),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        },
        shape = RoundedCornerShape(28.dp),
        onCloseRequest =  { dialogState.hide() },
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        datepicker(
            initialDate = birthDay,
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.surface,
                headerTextColor = MaterialTheme.colorScheme.onSurface,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.surface,
                dateInactiveTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            yearRange = rangeOfYearsFromNowTo(150),
            allowedDateValidator = {
                it.isBefore(LocalDate.now()) || it.isEqual(LocalDate.now())
            },
        ) { date ->
            onBirthDaySelect(date)
        }
    }

    ListItem(
        modifier = modifier.clickable { dialogState.show() },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Cake,
                contentDescription = "Cake Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        headlineContent = {
            Text(
                text = stringResource(id = com.bartozo.lifeprogress.R.string.your_birthday),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = birthDay.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        },
    )
}

@Preview
@Composable
private fun BirthDayCardPreview() {
    LifeProgressTheme {
        BirthDayCard(
            birthDay = LocalDate.now(),
            onBirthDaySelect = {}
        )
    }
}