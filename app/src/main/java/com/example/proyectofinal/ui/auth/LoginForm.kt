package com.example.proyectofinal.ui.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Role
import com.example.proyectofinal.ui.components.InputText
import com.example.proyectofinal.ui.components.ToggleButtonGroup
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.ui.theme.BackgroundColorFromHSL
import com.example.proyectofinal.ui.theme.BorderBoxes
import com.example.proyectofinal.ui.theme.MutedColorFromHSL
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryLight
import com.example.proyectofinal.viewModel.UsersViewModel

@Composable
fun LoginForm(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onNavigateToHome: (String, Role) -> Unit,

    ){
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (typeUser, setTypeUser) = rememberSaveable { mutableStateOf(Role.USER) }
    val (isEmailError, setIsEmailError) = rememberSaveable { mutableStateOf(false) }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (isPasswordError, setIsPasswordError) = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val usersViewModel = localMainViewModel.current.usersViewModel
    val gradientColors = listOf(
        BackgroundColorFromHSL,
        MutedColorFromHSL.copy(alpha = 0.3f),
        Primary.copy(alpha = 0.3f)
    )

    Surface (
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 40.dp),
            content={
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                    content = {
                        IconInContainer()

                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(space = 4.dp),

                            content = {
                                Text(
                                    text = stringResource(R.string.login_app_title),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.Black,
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.Bold,

                                    )
                                Text(
                                    text = stringResource(R.string.login_welcome_message),
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        )
                    }
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(20.dp),

                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                        content={
                            ToggleButtonGroup(
                                onButtonSelected = { selectedButtonType ->
                                    setTypeUser(selectedButtonType)
                                },

                                )
                            InputText(
                                value = email,
                                setValue = setEmail,
                                text = stringResource(R.string.login_label_email),
                                place = stringResource(R.string.login_placeholder_email),
                                textError = stringResource(R.string.login_error_message_email),
                                isError = isEmailError,
                                setError = setIsEmailError,
                                onValidate = {
                                    it.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                }
                            )
                            InputText(
                                value = password,
                                setValue = setPassword,
                                text = stringResource(R.string.login_label_password),
                                place = stringResource(R.string.login_placeholder_password),
                                textError = stringResource(R.string.login_error_message_password),
                                isError = isPasswordError,
                                setError = setIsPasswordError,
                                onValidate = {
                                    it.length < 8 || it.isBlank()
                                },
                                isPassword = true
                            )
                        }
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                        content={
                            Button(
                                onClick = {
                                    val userLogged = usersViewModel.login(email, password)
                                    if(userLogged != null){
                                        onNavigateToHome(userLogged.id, userLogged.role)
                                        Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(
                                            context,
                                            "Credenciales incorrectas",
                                            Toast.LENGTH_SHORT

                                        ).show()
                                    }
                                },
                                enabled = !isEmailError && !isPasswordError,
                                colors = ButtonDefaults.buttonColors(Primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(50.dp),
                                content={
                                    Text(
                                        text = stringResource(R.string.login_button_text)
                                    )
                                }
                            )
                            Text(
                                text = stringResource(R.string.login_text_forgot_password),
                                color = Primary,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    textDecoration = TextDecoration.Underline // Subrayado para que parezca un enlace
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                                    .clickable {
                                        onNavigateToForgotPassword()
                                    },
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                            Row {
                                Text(
                                    text = stringResource(R.string.login_text_signuphere)
                                )
                                Text(
                                    text = stringResource(R.string.login_text_signuphere_navigate),
                                    color = Primary,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textDecoration = TextDecoration.Underline // Subrayado para que parezca un enlace
                                    ),
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .clickable {
                                            onNavigateToRegister()
                                        },
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}


@Composable
fun IconInContainer() {
    val borderRadius = 20.dp

    Box(
        modifier = Modifier
            .size(120.dp)
            .shadow(
                elevation = 8.dp, // Elige la elevación que prefieras
                shape = RoundedCornerShape(borderRadius) // Usa la misma forma que el contenedor
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Primary, PrimaryLight)
                ),
                shape = RoundedCornerShape(borderRadius)
            )
            .clip(RoundedCornerShape(borderRadius))

            ,

        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.location),
            contentDescription = stringResource(R.string.icon_content_description_location),
            modifier = Modifier
                .size(50.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}