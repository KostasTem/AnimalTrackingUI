package com.example.animaltrackingui.db

data class EncounteredPost(
    var id: Long?,
    var username: String,
    var animalType: String,
    var animalImage: String,
    var comment: String,
    var latitude: Double,
    var longitude: Double,
    var location: List<String>,
    var countryCode: String,
    var timestamp: Long?,
    var archived: Boolean,
    var hostile: Boolean,
    var postStatus: PostStatus
)
{
    constructor(): this(-1,"","","","",0.0,0.0,listOf(),"",System.currentTimeMillis(),false, false, PostStatus.NONE)
}
