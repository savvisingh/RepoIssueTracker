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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;

    private RecyclerView mRecyclerView;

    private DataAdapter mAdapter;

    private ArrayList<Issue> mIssuesList;

    private EditText editText;

    private CompositeDisposable composite;

    private Disposable searchDisposable;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        composite = new CompositeDisposable();

        editText = (EditText) findViewById(R.id.searchQuery);
        button  =(Button) findViewById(R.id.search);

        retrofit = ApiClient.getClient();

        final ApiInterface GitHubAPI = retrofit.create(ApiInterface.class);

        button.setOnClickListener(view -> {
            String s = editText.getText().toString();
            String[] ss = s.split("/");


            if(searchDisposable!=null){
                searchDisposable.dispose();
            }
            searchDisposable = GitHubAPI.getIssues(ss[0], ss[1], "open")
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(searchResponse ->
                                loadData(searchResponse)
                                , throwable -> onError(throwable));



        });
    }



    public void loadData(List<Issue> issues){
            mIssuesList.clear();
            mIssuesList.addAll(issues);
            mAdapter.notifyDataSetChanged();
    }

    public void onError(Throwable e) {
        Log.d("Error", " " + e.getLocalizedMessage());
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.coordinatorLayout), "No such User and Repo", Snackbar.LENGTH_LONG);
        snackbar.show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(searchDisposable !=null && !searchDisposable.isDisposed())
            searchDisposable.dispose();
    }
}
