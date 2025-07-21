package com.vibejournal.app.model

data class GoogleDoc(
    val id: String,
    val title: String,
    val createdTime: String? = null,
    val modifiedTime: String? = null
)
