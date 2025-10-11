package com.example.proyectofinal.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

class Schedule (
    val day: DayOfWeek,
    val open: LocalTime,
    val close: LocalTime

    )