package com.hbworld.onehub.dtos

sealed class Results {

    data class success(val data: List<Device>) : Results()
    data class error(val message: String) : Results()
    object loading : Results()
}