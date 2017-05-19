package apps.savvisingh.githubrepoissues.DataModel;

import java.util.List;

import apps.savvisingh.githubrepoissues.DataModel.networking.GitHubApiService;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SavviSingh on 19/05/17.
 */

public class DataModel implements DataModelInterface {

    private GitHubApiService gitHubApiService;

    public DataModel(){
        gitHubApiService = new GitHubApiService(ApiClient.getClient());
    }

    @Override
    public Observable<List<Issue>> getIssues(String queryText) {

        String[] query_params = queryText.split("/");

        return gitHubApiService.getIssues(query_params[0], query_params[1], "open").subscribeOn(Schedulers.io());
    }
}
