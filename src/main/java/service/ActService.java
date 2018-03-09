package service;

import model.Credentials;
import model.Filter;
import model.IAct;
import service.bitbucket.BitbucketService;
import service.calendar.CalendarService;
import service.jira.JiraService;
import spark.utils.StringUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ActService {

	public List<IAct> getAct(Filter filter,
							 Credentials credentials) {

		List<IAct> actList = new ArrayList<>();
		final ZonedDateTime date = filter.getDate();

		final CalendarService calendarService = new CalendarService();
		final List<IAct> eventList = calendarService.getEvents(credentials.getCalendarAccessToken(), date);

		if (!eventList.isEmpty()){
			actList.addAll(eventList);
		}

		if (filter.getRepoList() != null && !filter.getRepoList().isEmpty()) {
			final String bitbucketAccessToken = credentials.getBitbucketAccessToken();
			final BitbucketService bitbucketService = new BitbucketService(bitbucketAccessToken);
			final List<IAct> commitList = bitbucketService.getCommits(filter.getRepoList(), bitbucketAccessToken, date);
			actList.addAll(commitList);
			final List<IAct> pullRequestList = bitbucketService.getPullRequestActivities(filter.getRepoList(), bitbucketAccessToken, date);
			actList.addAll(pullRequestList);
		}

		if (StringUtils.isNotBlank(filter.getUser())) {
			final List<IAct> jiraList = new JiraService().getActivities(filter.getUser(), date);
			actList.addAll(jiraList);
		}

		actList = actList
					.stream()
					.filter(act -> act.getDate().getDayOfMonth() == date.getDayOfMonth())
					.filter(act -> act.getDate().getMonth() == date.getMonth())
					.filter(act -> act.getDate().getYear() == date.getYear())
					.filter(act -> !(act.getSummary().contains("logged '")))
					.collect(Collectors.toList());

		actList.sort(Comparator.comparing((IAct act) -> act.getDate().toLocalDateTime()));

		return actList;
	}

}
