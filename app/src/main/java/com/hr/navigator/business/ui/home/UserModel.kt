package com.hr.navigator.business.ui.home

import com.hr.navigator.business.ui.profile.CompanyModel

data class UserModel(
    val firstName :String="",
    val lastName :String="",
    val designation :String="",
    val email :String="",
    val phone :String="",
    val latitude :String="",
    val longitude :String="",
    val addressLine :String="",
    val type: String = "",
    val companyModel: CompanyModel? = null
) {
    // Default no-argument constructor required by Firebase
    constructor() : this("", "", "", "", "", "", "", "","" ,null)
}