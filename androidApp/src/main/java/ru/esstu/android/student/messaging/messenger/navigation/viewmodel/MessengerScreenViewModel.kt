package ru.esstu.android.student.messaging.messenger.navigation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsDbRepository
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModuleNew
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog


data class MessengerScreenState(
    val selectedDialog: List<Dialog> = emptyList(),
    val isEditing: Boolean = false,
    val showAlertDialog: Boolean = false,
)
infix fun String.toNormalView(countChat: Int): String{
    return when (countChat){
        2,3,4 -> "$this  $countChat чата"
        in 5..20 -> "$this $countChat чатов"
        else -> ""
    }
}

class MessengerScreenViewModel(
   private val dialogDB: IDialogsDbRepository = ESSTUSdk.dialogsModuleNew.repoDialogs,
): ViewModel() {
    var dialogState by mutableStateOf(MessengerScreenState())
        private set

    fun showAlertDialog(){
        dialogState = dialogState.copy(showAlertDialog = true)
    }

    fun dismissDialog(){
        dialogState = dialogState.copy(showAlertDialog = false)
    }

    fun deleteDialogs(){
        dismissDialog()
        viewModelScope.launch {
            dialogState.selectedDialog.forEach {
                dialogDB.deleteDialog(it.id)
            }
        }
        closeEditingMode()
    }

    fun addDialog(dialog: Dialog){
        if(!dialogState.isEditing){
            dialogState = dialogState.copy(isEditing = true)
        }

        val l = mutableSetOf(*dialogState.selectedDialog.toTypedArray())
        if (!l.add(dialog)){
            l.remove(dialog)
        }

        dialogState = dialogState.copy(selectedDialog = l.toList())
    }

    fun closeEditingMode(){
        dialogState = dialogState.copy(
            selectedDialog = emptyList(),
            isEditing = false,
        )
    }
}