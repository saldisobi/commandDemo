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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.template.data.MyModelRepository
import android.template.ui.mymodel.MyModelUiState.Error
import android.template.ui.mymodel.MyModelUiState.Loading
import android.template.ui.mymodel.MyModelUiState.Success
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class MyModelViewModel @Inject constructor(
    private val myModelRepository: MyModelRepository
) : ViewModel(), CommandReceiver {

    val uiState: StateFlow<MyModelUiState> = myModelRepository
        .myModels.map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun processCommand(command: Command) {
        command.execute(this)
    }

    override fun onAddClicked() {
        viewModelScope.launch {
            Log.v(TAG, "add command")
        }
    }

    override fun onTextUpdate(newText: String) {
        viewModelScope.launch {
            Log.v(TAG, "edit command")
        }
    }

    override fun onDeleteClicked() {
        viewModelScope.launch {
            Log.v(TAG, "delete command")
        }
    }

    override fun onListClicked() {
        viewModelScope.launch {
            Log.v(TAG, "list command")
        }
    }

    override fun onSaveClicked(text: String) {
        viewModelScope.launch {
            myModelRepository.add(text)
        }
    }

    companion object {
        const val TAG = "MyTag"
    }
}

sealed interface MyModelUiState {
    object Loading : MyModelUiState
    data class Error(val throwable: Throwable) : MyModelUiState
    data class Success(val data: List<String>) : MyModelUiState
}
