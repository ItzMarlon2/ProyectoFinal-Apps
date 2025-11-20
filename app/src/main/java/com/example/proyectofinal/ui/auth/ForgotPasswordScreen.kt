package com.example.proyectofinal.ui.auth

import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.utils.RequestResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit
) {

    val usersViewModel = localMainViewModel.current.usersViewModel
    val operationResult by usersViewModel.userResult.collectAsState()

    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(operationResult) {
        when (val result = operationResult) {
            is RequestResult.Loading -> {
                isLoading = true
            }
            is RequestResult.Success -> {
                isLoading = false
                scope.launch {
                    delay(2000L)
                    onNavigateBack()
                }
            }
            is RequestResult.Failure -> {
                isLoading = false
            }
            null -> {
                isLoading = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            usersViewModel.resetOperationResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Introduce tu correo electrónico",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Te enviaremos un enlace para que puedas restablecer tu contraseña.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                    disabledContainerColor = Color.LightGray.copy(alpha = 0.4f),
                    focusedBorderColor = Primary.copy(alpha = 0.4f),
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    usersViewModel.sendPasswordResetEmail(email)
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(Primary, Color.White, Color.Gray, Color.White)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Primary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Enviar Enlace", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            when (val result = operationResult) {
                is RequestResult.Success -> {
                    Text(result.message, color = Color(0xFF008000))
                }
                is RequestResult.Failure -> {
                    Text(result.errorMessage, color = MaterialTheme.colorScheme.error)
                }
                else -> { /* No mostrar nada */ }
            }
        }
    }
}
