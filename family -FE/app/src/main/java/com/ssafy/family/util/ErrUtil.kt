package com.ssafy.family.util

object ErrUtil {
    fun setErrorMsg(msg:String?):String{
        if(msg.isNullOrBlank()||msg.isNullOrEmpty()){
            return "서버에러"
        }
        return msg
    }
}