package com.example.proyectofinal.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.model.Role
import com.example.proyectofinal.model.User
import com.example.proyectofinal.utils.RequestResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsersViewModel: ViewModel() {
    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    private val _userResult = MutableStateFlow<RequestResult?>(null)
    val userResult: StateFlow<RequestResult?> = _userResult.asStateFlow()
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    val db = Firebase.firestore
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    init{
        auth.currentUser?.let { firebaseUser ->
            // Si hay una sesión, busca sus datos en Firestore
            fetchCurrentUserData(firebaseUser.uid)
        }
    }

    fun create(user: User){
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value =  runCatching {
                createFireBase(user)
            }
                .fold(
                    onSuccess = { RequestResult.Success("Usuario creado correctamente") },
                    onFailure = { RequestResult.Failure(it.message ?: "Error al crear el usuario")}
                )
        }
    }

    private suspend fun createFireBase( user: User){
        val newUser = auth.createUserWithEmailAndPassword(user.email, user.password).await()
        val uId = newUser.user?.uid ?: throw Exception("Error al obtener el UUID del usuario")


        val userCopy = user.copy(
            id = uId,
            password = ""
        )

        db.collection("users")
            .document(uId)
            .set(userCopy)
            .await()
    }

    private fun fetchCurrentUserData(userId: String){
        viewModelScope.launch {
            try {
                val snapshot = db.collection("users").document(userId).get().await()
                val user = snapshot.toObject(User::class.java)?.apply {
                    this.id = snapshot.id
                }
                _currentUser.value = user
                println("ViewModel: Usuario cargado -> $user")
            } catch (e: Exception) {
                println("ViewModel: Error al cargar usuario -> ${e.message}")

                logOut()
            }
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value =  runCatching {
                loginFirebase(email, password)
            }
                .fold(
                    onSuccess = { RequestResult.Success("Login exitoso") },
                    onFailure = { RequestResult.Failure("Error al iniciar sesión, credenciales incorrectas")}
                )
        }
    }

    fun logOut(){
        auth.signOut()
        _currentUser.value = null
    }

    private suspend fun loginFirebase(email: String, password: String){
        val responseUser = auth.signInWithEmailAndPassword(email, password).await()
        val uId = responseUser.user?.uid ?: throw Exception("Usuario no encontrado")

        fetchCurrentUserData(uId)
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _userResult.value = RequestResult.Failure("Por favor, introduce un correo electrónico válido.")
                return@launch
            }

            try {
                auth.sendPasswordResetEmail(email).await()
                _userResult.value = RequestResult.Success("Se ha enviado un enlace de recuperación a tu correo.")
            } catch (e: Exception) {
                _userResult.value = RequestResult.Failure(e.message ?: "Error al enviar el correo de recuperación.")
            }
        }
    }

    fun resetOperationResult(){
        _userResult.value = null
    }

    fun loadUsers(){
        /*_users.value = listOf(
            User(
                id = "1",
                nombre = "marlon",
                username = "itzMarlon13",
                role = Role.ADMIN,
                email = "marlonamorteguicampo@gmail.com",
                password = "12345678",
                city = "bogota"
            ),
            User(
                id = "2",
                nombre = "test",
                username = "test123",
                role = Role.USER,
                email = "test@gmail.com",
                password = "12345678",
                city = "bogota"
            )
        )*/
    }

}