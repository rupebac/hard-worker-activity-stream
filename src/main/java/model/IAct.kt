package model

import java.time.ZonedDateTime

interface IAct {
    val id: String?
    val type: String?
    val link: String?
    val date: ZonedDateTime?
    val summary: String?
    val description: String?
    val userName: String?
}
