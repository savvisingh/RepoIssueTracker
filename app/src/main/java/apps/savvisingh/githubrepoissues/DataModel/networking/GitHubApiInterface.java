package apps.savvisingh.githubrepoissues.DataModel.networking;

import java.util.List;


import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by SavviSingh on 15/05/17.
 */

public interface GitHubApiInterface {
    @GET("repos/{user}/{repo}/issues")
    Observable<List<Issue>> getIssues(@Path("user") String user, @Path("repo") String repo, @Query("state") String state);
}
