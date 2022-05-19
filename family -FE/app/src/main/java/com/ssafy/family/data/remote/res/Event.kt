package com.ssafy.family.data.remote.res

import java.time.LocalDate

data class Event(
    val id: String,
    val text: String,
    val date: LocalDate
)
