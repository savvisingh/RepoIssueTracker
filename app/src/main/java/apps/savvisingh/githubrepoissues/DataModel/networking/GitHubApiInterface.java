package apps.savvisingh.githubrepoissues.DataModel.networking;

import java.util.HashMap;
import java.util.List;


import apps.savvisingh.githubrepoissues.DataModel.networking.model.Comment;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by SavviSingh on 15/05/17.
 */

public interface GitHubApiInterface {
    @GET("repos/{user}/{repo}/issues")
    Observable<List<Issue>> getIssues(@Path("user") String user, @Path("repo") String repo, @Query("state") String state);


    @POST("repos/{owner}/{repo}/issues/{number}/comments")
    Observable<Comment> postComment(@Path("owner") String owner, @Path("repo") String repo,
                                    @Path("number") String number,
                                    @Body HashMap<String, String> params);
}
