package apps.savvisingh.githubrepoissues.DataModel;

import java.util.List;

import apps.savvisingh.githubrepoissues.DataModel.networking.model.Comment;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by SavviSingh on 19/05/17.
 */

public interface DataModelInterface {

    Observable<List<Issue>> getIssues(String queryText);

    Observable<Comment> postIssue(String user, String repo, String issueNumber, String comment);

}
