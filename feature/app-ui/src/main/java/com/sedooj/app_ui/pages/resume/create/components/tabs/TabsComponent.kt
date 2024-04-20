package com.sedooj.app_ui.pages.resume.create.components.tabs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sedooj.arch.viewmodel.auth.model.TabsModel

@Composable
fun SetupTabs(
    selectedTabId: Int,
    onSelectTab: (Int, TabsModel.Tabs) -> Unit,
) {
    TabRow(selectedTabIndex = selectedTabId) {
        TabsModel.Tabs.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabId == index,
                onClick = {
                    if (selectedTabId != index)
                        onSelectTab(index, tab)
                }, modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                Icon(
                    painter = painterResource(id = tab.icon),
                    contentDescription = stringResource(
                        id = tab.title
                    ),
                    modifier = Modifier
                        .size(45.dp)
                        .padding(10.dp)
                )
            }
        }
    }
}