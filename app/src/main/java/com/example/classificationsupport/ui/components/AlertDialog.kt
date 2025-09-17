package com.example.classificationsupport.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.w3c.dom.Text

object CustomAlertDialogModel {
    var isAlertDialog = mutableStateOf(false)
    var title by mutableStateOf("")
    var text by mutableStateOf("")

    private var onConfirm: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    fun showDialog(
        title: String,
        text: String,
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = null
    ) {
        this.title = title
        this.text = text
        this.onConfirm = onConfirm
        this.onCancel = onCancel
        isAlertDialog.value = true
    }

    fun confirm() {
        onConfirm?.invoke()
        isAlertDialog.value = false
    }

    fun cancel() {
        onCancel?.invoke()
        isAlertDialog.value = false
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAlertDialog() {
    if (CustomAlertDialogModel.isAlertDialog.value) {
        BasicAlertDialog(
            onDismissRequest = {}
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                Text(CustomAlertDialogModel.title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Text(CustomAlertDialogModel.text, color = Color.White)
                Spacer(Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomAlertDialogModel.run {
                        if (onCancel != null) {
                            TextButton(onClick = { cancel() }) {
                                Text("취소", color = Color.Red)
                            }
                            Spacer(Modifier.width(8.dp))
                        }
                    }

                    TextButton(onClick = { CustomAlertDialogModel.confirm() }) {
                        Text("확인")
                    }
                }
            }
        }
    }
}