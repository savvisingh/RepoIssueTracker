package apps.savvisingh.githubrepoissues.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import apps.savvisingh.githubrepoissues.R;
import apps.savvisingh.githubrepoissues.DataModel.networking.model.Issue;

/**
 * Created by SavviSingh on 15/05/17.
 */

public class DataAdapter  extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<Issue> mIssueList;

    public DataAdapter(ArrayList<Issue> androidList) {
        mIssueList = androidList;
    }

    public ItemClickListener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mIssueTitle.setText(mIssueList.get(position).getTitle());

        holder.mIssueId.setText("#" + mIssueList.get(position).getId());
        holder.mIssueOpenedBy.setText(mIssueList.get(position).getUser().getLogin());


    }

    public void setItemClickListener(ItemClickListener listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mIssueList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mIssueTitle;
        private TextView mIssueId;
        private TextView mIssueOpenedBy;
        public ViewHolder(View view) {
            super(view);

            mIssueTitle = (TextView)view.findViewById(R.id.issue_title);
            mIssueId = (TextView)view.findViewById(R.id.issue_id);
            mIssueOpenedBy = (TextView)view.findViewById(R.id.issue_opened_by_username);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(mListener!= null){
                mListener.onClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
         void onClick(View v, int position);
    }
}