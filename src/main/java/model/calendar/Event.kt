package model.calendar

import com.google.gson.internal.LinkedTreeMap
import model.Act

import java.time.ZonedDateTime

class Event(event: LinkedTreeMap<*, *>) : Act() {

    init {
        type = "event"
        id = event["id"].toString()
        link = event["htmlLink"].toString()
        summary = event["summary"].toString()
        date = ZonedDateTime.parse((event["start"] as LinkedTreeMap<*, *>)["dateTime"].toString())
        userName = (event["organizer"] as LinkedTreeMap<*, *>)["displayName"].toString()
    }
}
