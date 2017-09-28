package model

class Credentials(cookies: Map<String, String>) {
    val bitbucketAccessToken: String = cookies["bitbucketAccessToken"].toString()
    val calendarAccessToken: String = cookies["calendarAccessToken"].toString()
}
