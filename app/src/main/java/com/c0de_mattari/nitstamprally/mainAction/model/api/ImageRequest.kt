package com.c0de_mattari.nitstamprally.mainAction.model.api

data class ImageRequest(
        val quizCode: Int,
        val majors: Collection<Int>
)