package apps.savvisingh.githubrepoissues;

import java.util.List;

import apps.savvisingh.githubrepoissues.DataModel.DataModel;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by SavviSingh on 19/05/17.
 */

public class ViewModel {

    private final PublishSubject<String> publishSubject = PublishSubject.create();

    private DataModel dataModel;

    public ViewModel(DataModel dataModel){
        this.dataModel = dataModel;
    }

    public Observable<List<Issue>> getResults(){
        return publishSubject
                .concatMap(dataModel::getIssues);
    }

    public void searchQuery(String queryText){
        publishSubject.onNext(queryText);
    }
}
