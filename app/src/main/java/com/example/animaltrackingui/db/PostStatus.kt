package com.example.animaltrackingui.db

enum class PostStatus {
    NONE, INVESTIGATING, ANIMAL_NOT_FOUND, ANIMAL_FOUND_BY_OFFICIALS
}

object PostStatusMap{
    val mapToString: Map<PostStatus,String> = mapOf(Pair(PostStatus.NONE,"None"),Pair(PostStatus.INVESTIGATING,"Investigating"),Pair(PostStatus.ANIMAL_NOT_FOUND,"Animal Not Found"),Pair(PostStatus.ANIMAL_FOUND_BY_OFFICIALS,"Animal Found And Relocated"))
    val mapToPostStatus: Map<String, PostStatus> = mapToString.entries.associate{(k,v)-> v to k}
}