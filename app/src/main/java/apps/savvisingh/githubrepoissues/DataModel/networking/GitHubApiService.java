package apps.savvisingh.githubrepoissues.DataModel.networking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.savvisingh.githubrepoissues.DataModel.networking.model.Comment;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.Observable;
import okhttp3.RequestBody;
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

    public Observable<Comment> postComment(String user, String repo, String issue, HashMap<String, String> comment){
        return  gitHubApiInterface.postComment(user, repo, issue, comment);
    }
}
