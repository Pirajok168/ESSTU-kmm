package ru.esstu.android.authorized.messaging.messanger

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import ru.esstu.features.messanger.dialogs.di.dialogsDi
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog


data class MessengerScreenState(
    val selectedPreviewDialog: List<PreviewDialog> = emptyList(),
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

class MessengerScreenViewModel : ViewModel() {

    private val di: DI by lazy { dialogsDi() }

    var dialogState by mutableStateOf(MessengerScreenState())
        private set

    fun showAlertDialog() {
        dialogState = dialogState.copy(showAlertDialog = true)
    }

    fun dismissDialog() {
        dialogState = dialogState.copy(showAlertDialog = false)
    }

    fun deleteDialogs(){
        dismissDialog()
        viewModelScope.launch {
            dialogState.selectedPreviewDialog.forEach {
                //  dialogDB.deleteDialog(it.id)
            }
        }
        closeEditingMode()
    }

    fun addDialog(previewDialog: PreviewDialog){
        if(!dialogState.isEditing){
            dialogState = dialogState.copy(isEditing = true)
        }

        val l = mutableSetOf(*dialogState.selectedPreviewDialog.toTypedArray())
        if (!l.add(previewDialog)){
            l.remove(previewDialog)
        }

        dialogState = dialogState.copy(selectedPreviewDialog = l.toList())
    }

    fun closeEditingMode(){
        dialogState = dialogState.copy(
            selectedPreviewDialog = emptyList(),
            isEditing = false,
        )
    }
}