package apps.savvisingh.githubrepoissues.DataModel;


import android.support.v4.util.ArrayMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.savvisingh.githubrepoissues.DataModel.networking.GitHubApiService;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Comment;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

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

    @Override
    public Observable<Comment> postIssue(String user, String repo, String issueNumber, String comment){

        HashMap<String, String> map = new HashMap<>();
        //put something inside the map, could be null
        map.put("body", comment);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(map)).toString());


        return gitHubApiService.postComment(user, repo, issueNumber, map).subscribeOn(Schedulers.io());
    }
}
