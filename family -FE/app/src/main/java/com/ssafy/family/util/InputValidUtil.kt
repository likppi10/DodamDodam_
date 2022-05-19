package com.ssafy.family.util

import java.lang.StringBuilder
import java.util.*

private const val TAG = "InputValidUtil_strait"
object InputValidUtil {
    val nameRegex = "^[가-힣a-zA-Z]{2,10}$".toRegex()
    val nickNameRegex = "^[가-힣a-zA-Zㄱ-ㅎㅏ-ㅞ\\s]{2,10}$".toRegex()
    val idRegex = "^[0-9a-z]{4,20}".toRegex()
    //영문, 숫자
    val passRegex1 = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$".toRegex()
    //영문, 특문
    //val passRegex2 = "^(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,14}$".toRegex()
    //특문, 숫자
    //val passRegex3 = "^(?=.*[^a-zA-Z0-9])(?=.*[0-9]).{8,14}$".toRegex()
    val passRegexes = listOf(passRegex1)

    val birthDayRegex = "([0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[1,2][0-9]|3[0,1]))".toRegex()
    val phoneRegex = "^\\d{3}\\d{3,4}\\d{4}$".toRegex()

    val familyCodeRegex = "^[0-9a-zA-Z]{15}$".toRegex()
    val wishRegex = "^[가-힣a-zA-Z0-9`~!?@#$%^&*()_=+\\s]{1,20}$".toRegex()

    fun isValidId(Id: String): Boolean {
        val result = Id.matches(idRegex)
        return result
    }

    fun isValidName(name: String): Boolean {
        val result = name.matches(nameRegex)
        return result
    }

    fun isValidNickName(name: String): Boolean {
        val result = name.matches(nickNameRegex)
        return result
    }

    fun isValidPassword(password: String): Boolean {
        for(i in passRegexes) {
            if(password.matches(i))
                return true
        }
        return false
    }


    // todo: 날짜 계산
    fun isValidBirthDay(birthDay: String): Boolean {
        if(birthDay.matches(birthDayRegex)) {
            val year = birthDay.substring(0, 4).toInt()
            val month = birthDay.substring(4, 6).toInt()
            val day = birthDay.substring(6, 8).toInt()

            return isValidDay(year, month, day)
        }
        return false
    }
    fun makeDay(birthDay: String):String{
        val sb = StringBuilder()
        sb.append(birthDay.substring(0, 4))
        sb.append("-")
        sb.append(birthDay.substring(4, 6))
        sb.append("-")
        sb.append(birthDay.substring(6, 8))
        return sb.toString()
    }
    fun isValidDay(year: Int, month: Int, day: Int): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        return if (1900 > year || year > currentYear) {
            false
        } else if (month < 1 || month > 12) {
            false
        } else if (day < 1 || day > 31) {
            false
        } else if ((month==4 || month==6 || month==9 || month==11) && day==31) {
            false
        } else if (month == 2) {
            val isLeap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
            !(day > 29 || (day == 29 && !isLeap))
        } else {
            true
        }
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches(phoneRegex)
    }

    fun isValidFamilyCode(code: String): Boolean {
        return code.matches(familyCodeRegex)
    }

    fun isValidWish(content: String): Boolean {
        return content.matches(wishRegex)
    }
}