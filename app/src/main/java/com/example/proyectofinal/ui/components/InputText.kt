package com.example.proyectofinal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.theme.Primary

@Composable
fun InputText(
    value: String,
    setValue: (String) -> Unit,
    text: String,
    place: String,
    textError: String,
    isError: Boolean,
    setError: (Boolean) -> Unit,
    onValidate: (String) -> Boolean,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
){
    Column (
        verticalArrangement = Arrangement.spacedBy(space = 6.dp),
        content = {
            Text(
                text=text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(

                value = value,
                placeholder = {
                    Text(text = place, color = Color.Gray)

                },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text(text = textError)
                    }
                },
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                onValueChange = {
                    setValue(it)
                    setError(onValidate(it))
                }
                ,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                    disabledContainerColor = Color.LightGray.copy(alpha = 0.4f),
                    focusedBorderColor = Primary.copy(alpha = 0.4f),
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }

    )
}