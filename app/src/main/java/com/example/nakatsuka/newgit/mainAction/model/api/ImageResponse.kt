package com.example.nakatsuka.newgit.mainAction.model.api

data class ImageResponse(
        val messageId: String,
        val isSend: Boolean,
        val quizCode: Int,
        val imageURL: String
)
