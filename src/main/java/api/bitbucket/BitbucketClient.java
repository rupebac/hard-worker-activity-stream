package api.bitbucket;

import api.ClientApi;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import model.bitbucket.PullRequestActivity;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static model.bitbucket.PullRequestActivity.getValueDate;

public class BitbucketClient extends ClientApi {

	private static final String BITBUCKET_API = "https://bitbucket.org/api/2.0";
	private static final String AUTH_TYPE = "Bearer";
	private static final String VALUES = "values";
	private Gson gson = new Gson();

	public List<LinkedTreeMap> requestCommits(String repo,
								 ZonedDateTime minDate,
	                                   String accessToken) {
		StringBuilder url = new StringBuilder();
		url.append(BITBUCKET_API)
				.append("/repositories/")
				.append(repo)
				.append("/commits")
				.append("?fields=")
				.append("-*,")
				.append("-values.*,")
				.append("-values.repository.*,")
				.append("-values.author.*,")
				.append("-values.links.*,")
				.append("%2Bvalues.author.user,")
				.append("-values.author.user.*,")
				.append("%2Bvalues.author.user.username,")
				.append("%2Bvalues.hash,")
				.append("%2Bvalues.repository.full_name,")
				.append("%2Bvalues.date,")
				.append("%2Bvalues.message,")
				.append("%2Bvalues.links.html,")
				.append("%2Bnext");

		List<String> resp = new ArrayList<>();

		String resAsString = performRequest(AUTH_TYPE,accessToken, url.toString());

		if(!isValidResponse(resAsString)){
			return Collections.emptyList();
		}

		resp.add(resAsString);

		ZonedDateTime lastCommit = getLastCommitDate(resAsString);

		while(lastCommit != null && lastCommit.isAfter(minDate)){
			String nextPage = getNextPage(resAsString);
			resAsString = performRequest(AUTH_TYPE, accessToken, nextPage);

			lastCommit = getLastCommitDate(resAsString);
			resp.add(resAsString);
		}

		List list = resp.stream().map(s -> gson.fromJson(s, Map.class)).collect(Collectors.toList());

		List<LinkedTreeMap> resultList = new ArrayList<>();

		list.forEach(m -> resultList.addAll(((Map<String, List<LinkedTreeMap>>)m).get(VALUES)));

		return resultList;
	}

	public List<LinkedTreeMap> getPullRequestActivities(String repo,
										   ZonedDateTime minDate,
										   String accessToken) {
		StringBuilder url = new StringBuilder();
		url.append(BITBUCKET_API)
				.append("/repositories/")
				.append(repo)
				.append("/pullrequests")
				.append("/activity")
				.append("?fields=")
				.append("-values.pull_request")
				.append("&pagelen=50");

		List<String> resp = new ArrayList<>();

		String resAsString = performRequest(AUTH_TYPE, accessToken, url.toString());

		if(!isValidResponse(resAsString)){
			return Collections.emptyList();
		}

		resp.add(resAsString);

		ZonedDateTime lastPrActivity = getLastPRDate(resAsString);

		while(lastPrActivity != null && lastPrActivity.isAfter(minDate)){
			String nextPage = getNextPage(resAsString);
			resAsString = performRequest(AUTH_TYPE, accessToken, nextPage);

			lastPrActivity = getLastPRDate(resAsString);
			resp.add(resAsString);
		}

		List list = resp.stream().map(s -> gson.fromJson(s, Map.class)).collect(Collectors.toList());

		List<LinkedTreeMap> resultList = new ArrayList<>();

		list.forEach(m -> resultList.addAll(((Map<String, List<LinkedTreeMap>>)m).get(VALUES)));

		return resultList;
	}

	public String getUserName(String accessToken) {
		String resAsJson = performRequest(AUTH_TYPE, accessToken, BITBUCKET_API + "/user");
		Map resAsMap = new Gson().fromJson(resAsJson, Map.class);
		return resAsMap.get("username").toString();
	}

	private String getNextPage(String resAsString){
		Map resMap = new Gson().fromJson(resAsString, Map.class);
		return (String) resMap.get("next");
	}


	private ZonedDateTime getLastCommitDate(String resAsString){
		Map<String,List<LinkedTreeMap>> resMap = gson.fromJson(resAsString, Map.class);

		List<LinkedTreeMap> values = resMap.get(VALUES);

		if(values != null){
			values.sort(Comparator.comparing(v -> ZonedDateTime.parse(v.get("date").toString())));
			LinkedTreeMap lastValue = values.get(values.size() - 1);
			return ZonedDateTime.parse(lastValue.get("date").toString());
		}

		return null;
	}

	private ZonedDateTime getLastPRDate(String resAsString){
		Map<String,List<LinkedTreeMap>> resMap = gson.fromJson(resAsString, Map.class);

		List<LinkedTreeMap> values = resMap.get(VALUES);

		if(values != null){
			values.sort(Comparator.comparing(PullRequestActivity::getValueDate));
			LinkedTreeMap lastValue = values.get(values.size() - 1);
			return getValueDate(lastValue);
		}
		return null;
	}

	private Boolean isValidResponse(String resp) {
		if(!resp.contains("values")){
			return false;
		}
		try {
			Map linkedTreeMap = gson.fromJson(resp, Map.class);
			return !linkedTreeMap.isEmpty();
		} catch (Exception e) {
			return false;
		}
	}
}
