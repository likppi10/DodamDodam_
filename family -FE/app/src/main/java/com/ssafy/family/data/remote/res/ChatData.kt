package com.ssafy.family.data.remote.res

data class ChatData(
    var id: Long? = -1,
    var name: String? = "",
    var message: String? = "",
    var profileImage: String? = "",
    var time: String? = ""
)