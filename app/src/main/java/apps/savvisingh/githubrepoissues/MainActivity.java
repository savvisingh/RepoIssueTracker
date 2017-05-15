package apps.savvisingh.githubrepoissues;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.savvisingh.githubrepoissues.DataManager.ApiClient;
import apps.savvisingh.githubrepoissues.DataManager.ApiInterface;
import apps.savvisingh.githubrepoissues.adapter.DataAdapter;
import apps.savvisingh.githubrepoissues.model.Issue;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;

    private RecyclerView mRecyclerView;

    private DataAdapter mAdapter;

    private ArrayList<Issue> mIssuesList;

    private EditText editText;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        editText = (EditText) findViewById(R.id.searchQuery);
        button  =(Button) findViewById(R.id.search);

        retrofit = ApiClient.getClient();

        final ApiInterface GitHubAPI = retrofit.create(ApiInterface.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString();
                String[] ss = s.split("/");
                Observable<List<Issue>> issueTracker = GitHubAPI.getIssues(ss[0], ss[1], "open");

                issueTracker.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Issue>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<Issue> value) {
                                mIssuesList.clear();
                                mIssuesList.addAll(value);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("Error", " " + e.getLocalizedMessage());
                                Snackbar snackbar = Snackbar
                                        .make(findViewById(R.id.coordinatorLayout), "No such User and Repo", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });

//        PublishSubject<String> publishSubject  = PublishSubject.create();
//
//        publishSubject.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Function<String, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<?> apply(String s) throws Exception {
//
//                return GitHubAPI.getIssues("square", "retrofit", "open");;
//            }
//        }).subscribe(new Observer<Object>() {
//        });



    }

    private void initRecyclerView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mIssuesList = new ArrayList<>();
        mAdapter = new DataAdapter(mIssuesList);
        mRecyclerView.setAdapter(mAdapter);

    }
}
