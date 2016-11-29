package com.bajaj.agam.simply;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Agam on 11/27/2016.
 */
public class RecylcerAdaptor extends RecyclerView.Adapter<RecylcerAdaptor.MyViewHolder> {

    private List<ComplaintSchema> complaintList;

    public RecylcerAdaptor(List<ComplaintSchema> complaintList)
    {
        this.complaintList=complaintList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, date, body, cID, status;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.cr_title);
            date = (TextView)view.findViewById(R.id.cr_date);
            body = (TextView)view.findViewById(R.id.cr_body);
            cID = (TextView)view.findViewById(R.id.cr_cid);
            status = (TextView)view.findViewById(R.id.cr_status);
        }
    }

    @Override
    public RecylcerAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaint_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecylcerAdaptor.MyViewHolder holder, int position) {
        ComplaintSchema complaint = complaintList.get(position);
        holder.title.setText(complaint.getTitle());
        holder.body.setText(complaint.getBody());
        holder.date.setText(complaint.getDate());
        holder.status.setText(complaint.getStatus());
        holder.cID.setText(complaint.getuID());
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }
}
