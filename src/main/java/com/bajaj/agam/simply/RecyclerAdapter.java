package com.bajaj.agam.simply;


        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import org.w3c.dom.Text;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

        import static com.bajaj.agam.simply.R.id.view;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private List<HashMap<String,String>> list;
    private LayoutInflater inflater;
    private CallBack callBack;
    public interface CallBack{
        void onClick(int i);
        void onSeciconClick(int i);
    }

//    public void onCallBack(final CallBack callBack1)
//    {
//        this.callBack=callBack1;
//    }
//
//    public void setList(ArrayList<ComplaintSchema> exerciseList) {
//        this.list.clear();
//        this.list.addAll(exerciseList);
//    }


    public RecyclerAdapter(List<HashMap<String,String>> list, Context context)
    {
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.complaint_row,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        HashMap<String,String> item = list.get(position);
        holder.title.setText(item.get("title"));
        holder.body.setText(item.get("body"));
        holder.date.setText("");
        holder.status.setText(String.valueOf(item.get("status")));
        holder.cID.setText(item.get("u_id"));




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        public TextView title, date, body, cID, status;

        public Holder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.cr_title);
            date = (TextView)itemView.findViewById(R.id.cr_date);
            body = (TextView)itemView.findViewById(R.id.cr_body);
            cID = (TextView)itemView.findViewById(R.id.cr_cid);
            status = (TextView)itemView.findViewById(R.id.cr_status);




        }


    }
}