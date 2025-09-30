package com.example.proyectofinal.ui.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.proyectofinal.ui.components.InputText
import com.example.proyectofinal.ui.components.ToggleButtonGroup
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryLight

@Composable
fun LoginForm(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
){
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (typeUser, setTypeUser) = rememberSaveable { mutableStateOf("") }
    var (isEmailError, setIsEmailError) = rememberSaveable { mutableStateOf(false) }
    var (password, setPassword) = rememberSaveable { mutableStateOf("") }
    var (isPasswordError, setIsPasswordError) = rememberSaveable { mutableStateOf(false) }
    var context = LocalContext.current

    Surface {
        Column (
            modifier = Modifier
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 80.dp),
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
                                    style = MaterialTheme.typography.titleLarge,
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
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                    content={
                        ToggleButtonGroup(
                            onButtonSelected = { selectedButtonType ->
                                setTypeUser(selectedButtonType)
                            },

                        )
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Button(
//                                onClick = {
//
//                                },
//                                colors = ButtonDefaults.buttonColors(Primary),
//                                shape = RoundedCornerShape(8.dp),
//                                modifier = Modifier
//                                    .width(150.dp)
//                                    .height(50.dp),
//                                content={
//                                        Icon(
//                                            imageVector = Icons.Outlined.LocationOn,
//                                            contentDescription = stringResource(R.string.icon_content_description_location),
//                                            modifier = Modifier
//                                                .size(25.dp)
//                                        )
//                                    Spacer(
//                                        modifier = Modifier.width(8.dp)
//                                    )
//                                        Text(
//                                            text = stringResource(R.string.login_text_user),
//                                            fontSize = 18.sp,
//                                            fontWeight = FontWeight.Bold
//                                        )
//
//
//                                }
//                            )
//                            Button(
//                                onClick = {
//
//                                },
//                                content={
//                                    Icon(
//                                        imageVector = Icons.Outlined.LocationOn,
//                                        contentDescription = stringResource(R.string.icon_content_description_location),
//                                        modifier = Modifier
//                                            .size(30.dp),
//                                    )
//                                    Text(
//                                        text = stringResource(R.string.login_text_user)
//                                    )
//
//
//                                }
//                            )
//                        }
                        InputText(
                            value = email,
                            setValue = setEmail,
                            text = stringResource(R.string.login_label_email),
                            place = stringResource(R.string.login_placeholder_email),
                            textError = stringResource(R.string.login_error_message_email),
                            isError = isEmailError,
                            setError = setIsEmailError,
                            onValidate = {
                                email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
                                password.length < 8 || password.isBlank()
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
        )
    }
}


@Composable
fun IconInContainer() {
    val borderRadius = 20.dp

    Box(
        modifier = Modifier
            .size(120.dp)
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