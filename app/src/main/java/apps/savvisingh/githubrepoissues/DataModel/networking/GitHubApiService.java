package apps.savvisingh.githubrepoissues.DataModel.networking;

import java.util.List;

import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by SavviSingh on 19/05/17.
 */

public class GitHubApiService {


    private GitHubApiInterface gitHubApiInterface;

    public GitHubApiService(Retrofit retrofit){

        this.gitHubApiInterface = retrofit.create(GitHubApiInterface.class);
    }

    public Observable<List<Issue>> getIssues(String user, String repo, String type){

        return gitHubApiInterface.getIssues(user, repo, type);

    }
}
