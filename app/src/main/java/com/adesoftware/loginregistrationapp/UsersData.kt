package com.adesoftware.loginregistrationapp

import android.media.Image

data class UsersData(
    var userId: String,
    var username: String,
    var email: String,
    var password: String,
    var gender: String,
    var mobile: String,
    var imageUrl: Image
)
