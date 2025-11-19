package com.example.proyectofinal.ui.components

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.proyectofinal.model.AlertType
import com.example.proyectofinal.utils.RequestResult
import kotlinx.coroutines.delay

@Composable
fun OperationResultHandler(
    result: RequestResult?,
    onSuccess: suspend () -> Unit,
    onFailure: suspend () -> Unit,
){

    when (result){
        is RequestResult.Loading ->{
            LinearProgressIndicator()
        }
        is RequestResult.Success ->{
            AlertMessage(type = AlertType.SUCCESS, message = result.message)
        }
        is RequestResult.Failure ->{
            AlertMessage(type = AlertType.ERROR, message = result.errorMessage)
        }
        else ->{}
    }

    LaunchedEffect(result) {
        when (result) {
            is RequestResult.Success->{
                delay(2000)
                onSuccess()
            }
            is RequestResult.Failure -> {
                delay(4000)
                onFailure()
            }
            else -> {}

        }
    }

}