package com.sce.findjobproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsRecViewAdapter extends RecyclerView.Adapter<ContactsRecViewAdapter.ViewHolder>{

    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<String> CompanyReasons=new ArrayList<>();
    private final Context context;
    public ContactsRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item,parent,false);
        return new ViewHolder(view);
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.txtCompanyName.setText(contacts.get(position).getCompanyName());
        holder.SendCV.setOnClickListener(view -> {
            ((Home) context).SendCvFunc(position);
            holder.SendCV.setText(R.string.Cv_Send);
            holder.SendCV.setEnabled(false);

        });




        final float scale1 = context.getResources().getDisplayMetrics().density;
        final float scale2 = context.getResources().getDisplayMetrics().density;
        int px2 = (int) (350 * scale2 + 0.5f);  // replace 100 with your dimensions
        int px1 = (int) (850 * scale1 + 0.5f);  // replace 100 with your dimensions



        if(Home.CheckWhichUserForRecView()==3){
            holder.reportBtn.setVisibility(View.GONE);
            holder.SendCV.setVisibility(View.GONE);
            holder.txtReport.setText(context.getString(R.string.reason_of_report) + CompanyReasons.get(position));
        }

        holder.btnTrash.setOnClickListener(view -> {

            if(contacts !=null && !(contacts.isEmpty())){
                int actualPosition = holder.getBindingAdapterPosition();

                if (Home.CheckWhichUserForRecView()==1 || Home.CheckWhichUserForRecView()==2){
                    contacts.remove(actualPosition);
                    notifyItemRemoved(actualPosition);
                    notifyItemRangeChanged(actualPosition, contacts.size());
                }


                if(Home.CheckWhichUserForRecView()==3) {
                    ( (Home) context).DeletePostAdmin(actualPosition);
                    contacts.remove(actualPosition);
                    notifyItemRemoved(actualPosition);
                    CompanyReasons.remove(actualPosition);
                    notifyItemRemoved(actualPosition);
                    notifyItemRangeChanged(actualPosition, contacts.size());
                    notifyItemRangeChanged(actualPosition, CompanyReasons.size());


                }




            }

        });

        holder.reportBtn.setOnClickListener(view -> {
            //  int actualPosition = holder.getBindingAdapterPosition();
            ( (Home) context).ReportButton(position);

        });

        holder.shareBtn.setOnClickListener(view -> {
            int actualPosition = holder.getBindingAdapterPosition();
            ( (Home) context).ShareButton(actualPosition);

        });

        holder.txtDate.setText(contacts.get(position).getDate());
        holder.txtJobType2.setText(contacts.get(position).getJobType());
        holder.txtJobDescription.setText(contacts.get(position).getJobDescription());
        holder.txtJobLocation2.setText(contacts.get(position).getJobLocation());
        holder.parent.setOnClickListener(view -> {
            if(!contacts.get(position).isExpanded()){
                holder.btnDown.setImageResource(R.drawable.uparrows);
                holder.RelativeLayoutSize.getLayoutParams().height=px1;
                contacts.get(position).setExpanded(true);

            }
            else{
                holder.btnDown.setImageResource(R.drawable.downarrow);
                holder.RelativeLayoutSize.getLayoutParams().height=px2;
                contacts.get(position).setExpanded(false);
            }
            notifyDataSetChanged();

        });



    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCompanyReasons(ArrayList<String> CompanyReasons) {
        this.CompanyReasons = CompanyReasons;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtCompanyName;
        private final TextView txtReport;
        private final TextView txtJobType2;
        private final TextView txtJobLocation2;
        private final TextView txtDate;
        private final CardView parent;
        private final ImageView btnDown;
        private final ImageView btnTrash;
        private final ImageButton shareBtn,reportBtn;
        private final Button SendCV;
        private final RelativeLayout RelativeLayoutSize;
        private final TextView txtJobDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCompanyName = itemView.findViewById(R.id.txtCompanyName);
            txtDate= itemView.findViewById(R.id.txtDate);
            txtReport=itemView.findViewById(R.id.txtReport);
            shareBtn= itemView.findViewById(R.id.shareBtn);
            reportBtn= itemView.findViewById(R.id.reportBtn);
            parent=itemView.findViewById(R.id.parent);
            txtJobType2 =itemView.findViewById(R.id.txtJobType2);
            txtJobLocation2 =itemView.findViewById(R.id.txtJobLocation2);
            SendCV=itemView.findViewById(R.id.SendCV);
            btnDown=itemView.findViewById(R.id.btnDown);
            btnTrash=itemView.findViewById(R.id.btnTrash);
            RelativeLayoutSize=itemView.findViewById(R.id.RelativeLayoutSize);
            txtJobDescription =itemView.findViewById(R.id.txtJobDescription);
        }
    }
}
