/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.template.ui.mymodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import android.template.ui.theme.MyApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.ui.text.input.DeleteAllCommand

@Composable
fun MyModelCommandScreen(
    modifier: Modifier = Modifier,
    viewModel: MyModelViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val items by produceState<MyModelUiState>(
        initialValue = MyModelUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }
    if (items is MyModelUiState.Success) {
        MyModelCommandScreen(
            items = (items as MyModelUiState.Success).data,
            modifier = modifier,
            commandProcessor = viewModel::processCommand
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyModelCommandScreen(
    items: List<String>,
    modifier: Modifier = Modifier,
    commandProcessor: (Command) -> Unit
) {
    Column(modifier) {
        var nameMyModel by remember { mutableStateOf("Compose") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = nameMyModel,
                onValueChange = { nameMyModel = it }
            )

            Button(
                modifier = Modifier.width(96.dp),
                onClick = { commandProcessor(SaveCommand(nameMyModel)) }) {
                Text("Save")
            }
        }
        items.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Saved item: $it")

                Icon(
                    modifier = Modifier.clickable { commandProcessor(AddCommand()) },
                    imageVector = Icons.Filled.Add,
                    contentDescription = "add"
                )

                Icon(
                    modifier = Modifier.clickable { commandProcessor(TextUpdateCommand(nameMyModel)) },
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "edit"
                )

                Icon(
                    modifier = Modifier.clickable { commandProcessor(DeleteProductCommand()) },
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "delete"
                )

                Icon(
                    modifier = Modifier.clickable { commandProcessor(ListCommand()) },
                    imageVector = Icons.Filled.List,
                    contentDescription = "list"
                )
            }

        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        MyModelScreen(
            listOf("Compose", "Room", "Kotlin"),
            onSave = {},
            onAdd = {},
            onList = {},
            onDelete = {},
            onUpdate = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        MyModelScreen(
            listOf("Compose", "Room", "Kotlin"),
            onSave = {},
            onAdd = {},
            onList = {},
            onDelete = {},
            onUpdate = {})
    }
}
