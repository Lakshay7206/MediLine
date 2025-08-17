package com.example.mediline.User

sealed class UiEvent {
    class Navigation(val route:String): UiEvent()
}