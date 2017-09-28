package service.calendar;

import api.calendar.CalendarClient;
import model.IAct;
import model.calendar.Event;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarService {

	public List<IAct> getEvents(String accessToken, ZonedDateTime date) {
		return  new CalendarClient()
				.getEvents(date, accessToken)
				.stream()
				.map(Event::new)
				.collect(Collectors.toList());
	}
}
