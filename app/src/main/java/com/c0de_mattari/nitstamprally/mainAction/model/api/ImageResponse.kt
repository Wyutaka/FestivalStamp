package com.c0de_mattari.nitstamprally.mainAction.model.api

data class ImageResponse(
        val messageId: String,
        val isSend: Boolean,
        val quizCode: Int,
        val imageURL: String
)
