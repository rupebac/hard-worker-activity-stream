package model

import java.time.ZonedDateTime

abstract class Act : IAct {
    override var id: String? = null
    override var type: String? = null
    override var link: String? = null
    override var date: ZonedDateTime? = null
    override var summary: String? = null
    override var userName: String? = null
}
