package com.hkapps.hygienekleen.features.features_management.homescreen.closing.model

data class SendEmailClosingRequest(
    val projectCode : String,
    val date : String,
    val id : Int,
    val emailTo : List<EmailSendClosing>,
    val emailCc : List<EmailSendClosing>
)

data class EmailSendClosing(
    val name : String,
    val email : String
)
