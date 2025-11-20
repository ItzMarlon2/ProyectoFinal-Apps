package com.example.proyectofinal.ui.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.R
import com.example.proyectofinal.model.City
import com.example.proyectofinal.model.PlaceType
import com.example.proyectofinal.model.Role
import com.example.proyectofinal.model.User
import com.example.proyectofinal.ui.components.DropDownMenu
import com.example.proyectofinal.ui.components.InputText
import com.example.proyectofinal.ui.components.OperationResultHandler
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.utils.RequestResult
import com.example.proyectofinal.viewModel.UsersViewModel
import java.util.UUID

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit
){
    val usersViewModel = localMainViewModel.current.usersViewModel
    val userResult by usersViewModel.userResult.collectAsState()



    Scaffold (
        modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBarRegister(onNavigateBack = onNavigateBack)
                }
            ){ padding ->
                ScrollContent(padding=padding, onNavigateToLogin, usersViewModel, userResult)
            }
}


@Composable
fun ScrollContent(padding: PaddingValues, onNavigateToLogin: () -> Unit, usersViewModel: UsersViewModel, userResult: RequestResult?){
    val (name, setName) = rememberSaveable { mutableStateOf("") }
    val (isErrorName, setIsErrorName) = rememberSaveable { mutableStateOf(false) }
    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (isErrorUsername, setIsErrorUsername) = rememberSaveable { mutableStateOf(false) }
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    var (isEmailError, setIsEmailError) = rememberSaveable { mutableStateOf(false) }
    var (password, setPassword) = rememberSaveable { mutableStateOf("") }
    var (isPasswordError, setIsPasswordError) = rememberSaveable { mutableStateOf(false) }
    var (confirmPassword, setConfirmPassword) = rememberSaveable { mutableStateOf("") }
    var (isConfirmPasswordError, setIsConfirmPasswordError) = rememberSaveable { mutableStateOf(false) }
    var selectedCity by rememberSaveable { mutableStateOf<City?>(null) }
    val cityOptions = remember { City.entries.map {it.displayName} }
    var context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(space = 40.dp),

        content = {
            Row (
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 20.dp),

                // Contenido del Row
                content={
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.text_arrow),
                        modifier = Modifier
                            .size(30.dp),
                    )
                    Column (
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 2.dp),
                        content={
                            Text(
                                text = stringResource(R.string.register_text_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.register_text_header),
                                fontSize = 18.sp

                            )
                        }
                    )
                }

            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 10.dp),
                modifier = Modifier
                    .padding(horizontal = 40.dp),

                content={
                    InputText(
                        value = name,
                        setValue = setName,
                        text = stringResource(R.string.register_label_name),
                        place = stringResource(R.string.register_label_name_placeholder),
                        textError = stringResource(R.string.register_error_message_name),
                        isError = isErrorName,
                        setError = setIsErrorName,
                        onValidate = {
                            name.isBlank()
                        }
                    )
                    InputText(
                        value = username,
                        setValue = setUsername,
                        text = stringResource(R.string.register_label_username),
                        place = stringResource(R.string.register_label_username_placeholder),
                        textError = stringResource(R.string.register_error_message_username),
                        isError = isErrorUsername,
                        setError = setIsErrorUsername,
                        onValidate = {
                            username.isBlank()
                        }
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
                            email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        }
                    )

                    DropDownMenu(
                        items = cityOptions,
                        selectedItem = selectedCity?.displayName ?: "Selecciona una ciudad",
                        text = stringResource(R.string.register_label_city2),
                        onItemSelected = { newCity ->
                            selectedCity = City.entries.find { it.displayName == newCity }
                        },
                    )
                    InputText(
                        value = password,
                        setValue = setPassword,
                        isPassword = true,
                        text = stringResource(R.string.login_label_password),
                        place = stringResource(R.string.login_placeholder_password),
                        textError = stringResource(R.string.login_error_message_password),
                        isError = isPasswordError,
                        setError = setIsPasswordError,
                        onValidate = {
                            it.length < 8 || it.isBlank()
                        }
                    )
                    InputText(
                        value = confirmPassword,
                        setValue = setConfirmPassword,
                        isPassword = true,
                        text = stringResource(R.string.register_label_confirm_password),
                        place = stringResource(R.string.login_placeholder_password),
                        textError = stringResource(R.string.register_error_confirm_password),
                        isError = isConfirmPasswordError,
                        setError = setIsConfirmPasswordError,
                        onValidate = {
                            it != password
                        }
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                        content={
                            Button(
                                onClick = {
                                    val user= User(
                                        nombre = name,
                                        username = username,
                                        email = email,
                                        city = selectedCity,
                                        password = password,
                                        role = Role.USER

                                    )
                                    usersViewModel.create(user)
                                },
                                colors = ButtonDefaults.buttonColors(Primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(50.dp),
                                content={
                                    Text(
                                        text = stringResource(R.string.register_text_btn)
                                    )
                                }
                            )

                            OperationResultHandler(
                                result = userResult,
                                onSuccess = {
                                    onNavigateToLogin()
                                    usersViewModel.resetOperationResult()
                                },
                                onFailure = {
                                    usersViewModel.resetOperationResult()
                                }
                            )

                            Row {
                                Text(
                                    text = stringResource(R.string.register_redirect_login)
                                )
                                Text(
                                    text = stringResource(R.string.register_redirect_login2),
                                    color = Primary,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .clickable {
                                            onNavigateToLogin()
                                        },
                                )
                            }
                        }
                    )

                }
            )
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarRegister(onNavigateBack: () -> Unit = {}){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    TopAppBar(
        title={
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 2.dp),
                content={
                    Text(
                        text = stringResource(R.string.register_text_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.register_text_header),
                        fontSize = 18.sp

                    )
                }
            )
        },
        navigationIcon = {
            IconButton (onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}