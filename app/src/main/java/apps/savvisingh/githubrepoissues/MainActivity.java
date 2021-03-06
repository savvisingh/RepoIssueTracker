package apps.savvisingh.githubrepoissues;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.savvisingh.githubrepoissues.DataModel.DataModel;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Comment;
import apps.savvisingh.githubrepoissues.adapter.DataAdapter;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;

    private RecyclerView mRecyclerView;

    private DataAdapter mAdapter;

    private ArrayList<Issue> mIssuesList;

    private EditText editText;

    private Disposable searchDisposable;

    private Disposable postCommentDisposable;

    private Button button;

    private TextView noIssueText, noSearchText;

    private ProgressBar mProgressDialog;

    private DataModel dataModel;
    private ViewModel viewModel;

   // private final PublishSubject<String> publishSubject = PublishSubject.create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = (EditText) findViewById(R.id.searchQuery);
        button  =(Button) findViewById(R.id.search);
        noIssueText = (TextView) findViewById(R.id.no_issue_text);
        noSearchText = (TextView) findViewById(R.id.no_search_Query);
        mProgressDialog = (ProgressBar) findViewById(R.id.progressbar_loading);

        initRecyclerView();


        dataModel = new DataModel();
        viewModel = new ViewModel(dataModel);

        button.setOnClickListener(view -> {

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            viewModel.searchQuery(editText.getText().toString());

//            publishSubject.onNext(editText.getText().toString());

            mRecyclerView.setVisibility(View.INVISIBLE);
            noSearchText.setVisibility(View.INVISIBLE);
            noIssueText.setVisibility(View.INVISIBLE);
            mProgressDialog.setVisibility(View.VISIBLE);



//            if(searchDisposable!=null){
//                searchDisposable.dispose();
//            }
//            searchDisposable = GitHubAPI.getIssues(ss[0], ss[1], "open")
//                                .subscribeOn(Schedulers.computation())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(this::loadData
//                                , this::onError);



        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchDisposable = viewModel.getResults()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::loadData
                                    , this::onError);

//        publishSubject.
//                concatMap(dataModel::getIssues)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::loadData, this::onError);

    }

    public void showSomething(List<Issue> data){
        Toast.makeText(this, data.size() + " ", Toast.LENGTH_SHORT).show();
    }

    public void loadData(List<Issue> issues){

        mProgressDialog.setVisibility(View.INVISIBLE);

        if(issues.size()==0){
            noIssueText.setVisibility(View.VISIBLE);
        }else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mIssuesList.clear();
            mIssuesList.addAll(issues);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void onError(Throwable e) {
        mProgressDialog.setVisibility(View.INVISIBLE);
        noIssueText.setVisibility(View.VISIBLE);
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

        mRecyclerView.setVisibility(View.INVISIBLE);
        noIssueText.setVisibility(View.INVISIBLE);
        noSearchText.setVisibility(View.VISIBLE);
        //mProgressDialog.setVisibility(View.INVISIBLE);

        mAdapter.setItemClickListener((v, position) -> {

            Log.d("ItemClick ", "here");

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_dialog, null, false);
            dialogBuilder.setView(dialogView);

            final EditText commenttext = (EditText) dialogView.findViewById(R.id.comment);

            dialogBuilder.setTitle("Post Comment");
            dialogBuilder.setMessage("");
            dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
                //do something with edt.getText().toString();
                Log.d("Comment", commenttext.getText().toString());

                String[] search = this.editText.getText().toString().split("/");

                postCommentDisposable = dataModel.postIssue(search[0], search[1],
                        String.valueOf(mIssuesList.get(position).getNumber()), commenttext.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::showSuccess
                                , this::onError);

            });
            dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
                //pass
            });
            AlertDialog b = dialogBuilder.create();
            b.show();

        });


    }

    private void showSuccess(Comment comment) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.coordinatorLayout), "Comment Added", Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(searchDisposable !=null && !searchDisposable.isDisposed())
            searchDisposable.dispose();
    }
}
