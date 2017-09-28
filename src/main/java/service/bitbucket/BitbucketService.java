package service.bitbucket;

import api.bitbucket.BitbucketClient;
import com.google.gson.internal.LinkedTreeMap;
import model.IAct;
import model.bitbucket.Commit;
import model.bitbucket.PullRequestActivity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BitbucketService {
	private final BitbucketClient bitbucketClient = new BitbucketClient();
	private final String userName;

	public BitbucketService(String accessToken) {
		userName = bitbucketClient.getUserName(accessToken);
	}

	public List<IAct> getCommits(List<String> repoList, String accessToken, ZonedDateTime date) {
		List<LinkedTreeMap> commitList = new ArrayList<>();

		repoList.forEach(repo -> commitList.addAll(bitbucketClient.requestCommits(repo, date, accessToken)));

		return commitList
				.stream()
				.map(Commit::new)
				.filter(commit -> userName.equals(commit.getUserName()))
				.filter(commit -> commit.getDate().isAfter(date))
				.filter(commit -> !(commit.getSummary().contains("Merged in")))
				.filter(commit -> !(commit.getSummary().contains("Merge remote-tracking branch")))
				.filter(commit -> !(commit.getSummary().contains("Merge branch")))
				.collect(Collectors.toList());
	}


	public List<IAct> getPullRequestActivities(List<String> repoList, String accessToken, ZonedDateTime date) {
		List<LinkedTreeMap> pullRequestList = new ArrayList<>();

		repoList.forEach(repo -> pullRequestList.addAll(bitbucketClient.getPullRequestActivities(repo, date, accessToken)));

		return pullRequestList
				.stream()
				.map(PullRequestActivity::new)
				.filter(pr-> userName.equals(pr.getUserName()))
				.collect(Collectors.toList());
	}

}
