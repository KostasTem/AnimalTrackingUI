package com.example.animaltrackingui.db

import com.example.animaltrackingui.states.PostType

data class Report(
    val id:Long?,
    val postID: Long,
    var reason: String,
    val username: String?,
    var comment: String,
    val postType: PostType
){
    constructor(): this(null,0,"",null,"",PostType.NONE)
}