package com.example.proyectofinal.viewModel

import androidx.lifecycle.ViewModel
import com.example.proyectofinal.model.Role
import com.example.proyectofinal.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsersViewModel: ViewModel() {
    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    init{
        loadUsers()
    }

    fun loadUsers(){
        _users.value = listOf(
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
                nombre = "carlos",
                username = "carlos123",
                role = Role.USER,
                email = "carlos@gmail.com",
                password = "12345678",
                city = "bogota"
            )
        )
    }

    fun create( user: User){
        _users.value = _users.value + user
    }

    fun findById(id: String): User?{
        return _users.value.find { it.id == id }

    }

    fun findByEmail(email: String): User?{
        return _users.value.find { it.email == email }
    }

    fun login(email: String, password: String): User?{
        return _users.value.find { it.email == email && it.password == password }

    }
}