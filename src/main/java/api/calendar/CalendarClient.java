package api.calendar;

import api.ClientApi;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CalendarClient  extends ClientApi {

	public static final String URL = "https://content.googleapis.com/calendar/v3/calendars";

	public List<LinkedTreeMap> getEvents(ZonedDateTime date, String accessToken) {

		StringBuilder url = new StringBuilder();
		url.append(URL)
				.append("/primary")
				.append("/events?")
				.append("maxAttendees=1")
				.append("&orderBy=startTime")
				.append("&showDeleted=false")
				.append("&showHiddenInvitations=false")
				.append("&singleEvents=true")
				.append("&timeMax=")
				.append(date.toLocalDate())
				.append("T18%3A00%3A00-03%3A00")
				.append("&timeMin=")
				.append(date.toLocalDate())
				.append("T08%3A00%3A00-03%3A00");

		try {
			String events = performRequest("Bearer ", accessToken, url.toString());
			Map<String, List<LinkedTreeMap>> eventsMap = new Gson().fromJson(events, Map.class);
			return eventsMap.get("items");
		} catch (Exception e) {
			return Collections.emptyList();
		}

	}
}
