package com.example.nakatsuka.newgit.mainAction.model.api

data class ImageRequest(
        val quizCode: Int,
        val majors: Collection<Int>
)