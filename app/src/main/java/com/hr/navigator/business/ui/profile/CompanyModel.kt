package com.hr.navigator.business.ui.profile

data class CompanyModel(
    val companyName :String="",
    val email :String="",
    val phone :String="",
    val address :String="",
    val latitude :String="",
    val longitude :String="",
    val addressLine :String="",
    val companyId :String="",
    val type : String = ""
) {
    // Default no-argument constructor required by Firebase
    constructor() : this("", "", "", "", "", "", "", "", "")
}