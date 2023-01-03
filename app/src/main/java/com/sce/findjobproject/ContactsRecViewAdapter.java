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
// Adapter for displaying job postings in a RecyclerView
public class ContactsRecViewAdapter extends RecyclerView.Adapter<ContactsRecViewAdapter.ViewHolder>{
    // List of job postings
    private ArrayList<Contact> contacts = new ArrayList<>();
    // List of reasons for job postings being reported
    private ArrayList<String> CompanyReasons=new ArrayList<>();
    // Context of the calling activity
    private final Context context;
    // Constructs a new ContactsRecViewAdapter with the given context
    public ContactsRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each job posting
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item,parent,false);
        return new ViewHolder(view);
    }

    // Binds the data for a job posting to the views in the layout
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Set the company name for the job posting
        holder.txtCompanyName.setText(contacts.get(position).getCompanyName());
        // Set a click listener for the Send CV button
        holder.SendCV.setOnClickListener(view -> {
            // Call the SendCvFunc method in the Home activity, passing the position of the job posting
            ((Home) context).SendCvFunc(position);
            // Update the text and state of the Send CV button
            holder.SendCV.setText(R.string.Cv_Send);
            holder.SendCV.setEnabled(false);

        });



        // Calculate the dimensions for the expanded and collapsed states of the job posting layout
        final float scale1 = context.getResources().getDisplayMetrics().density;
        final float scale2 = context.getResources().getDisplayMetrics().density;
        int px2 = (int) (350 * scale2 + 0.5f);  // replace 100 with your dimensions
        int px1 = (int) (850 * scale1 + 0.5f);  // replace 100 with your dimensions


        // If the user is an admin, hide the report and Send CV buttons and show the report reason
        if(Home.CheckWhichUserForRecView()==3){
            holder.reportBtn.setVisibility(View.GONE);
            holder.SendCV.setVisibility(View.GONE);
            holder.txtReport.setText(context.getString(R.string.reason_of_report) + CompanyReasons.get(position));
        }

        // If the user is an admin, hide the report and Send CV buttons and show the report reason

        holder.btnTrash.setOnClickListener(view -> {
            // If the list of job postings is not empty
            if(contacts !=null && !(contacts.isEmpty())){
                // Get the current position of the job posting in the RecyclerView
                int actualPosition = holder.getBindingAdapterPosition();

                // If the user is a company or job seeker
                if (Home.CheckWhichUserForRecView()==1 || Home.CheckWhichUserForRecView()==2){
                    contacts.remove(actualPosition);
                    notifyItemRemoved(actualPosition);
                    notifyItemRangeChanged(actualPosition, contacts.size());
                }


                if(Home.CheckWhichUserForRecView()==3) {
                    // Check if user is an admin
                    // Call the DeletePostAdmin method in the Home activity, passing in the position of the item
                    ( (Home) context).DeletePostAdmin(actualPosition);
                    // Remove the item from the contacts and CompanyReasons lists
                    contacts.remove(actualPosition);
                    notifyItemRemoved(actualPosition);
                    CompanyReasons.remove(actualPosition);
                    // Update the RecyclerView to reflect the changes
                    notifyItemRemoved(actualPosition);
                    notifyItemRangeChanged(actualPosition, contacts.size());
                    notifyItemRangeChanged(actualPosition, CompanyReasons.size());


                }




            }

        });

        holder.reportBtn.setOnClickListener(view -> {
            // Call the 'ReportButton' method on the 'Home' context, passing in the current position

            ( (Home) context).ReportButton(position);

        });

        holder.shareBtn.setOnClickListener(view -> {
            // Get the actual position of the item within the RecyclerView
            int actualPosition = holder.getBindingAdapterPosition();
            // Call the 'ShareButton' method on the 'Home' context, passing in the actual position
            ( (Home) context).ShareButton(actualPosition);

        });
        // Set the text of the 'txtDate' TextView to the date value of the current item in the list
        holder.txtDate.setText(contacts.get(position).getDate());
        // Set the text of the 'txtJobType2' TextView to the job type value of the current item in the list
        holder.txtJobType2.setText(contacts.get(position).getJobType());
        // Set the text of the 'txtJobDescription' TextView to the job description value of the current item in the list
        holder.txtJobDescription.setText(contacts.get(position).getJobDescription());
        // Set the text of the 'txtJobLocation2' TextView to the job location value of the current item in the list
        holder.txtJobLocation2.setText(contacts.get(position).getJobLocation());
        holder.parent.setOnClickListener(view -> {
            if(!contacts.get(position).isExpanded()){
                // If the item is not expanded, set the image of the 'btnDown' ImageView to the 'uparrows' drawable and set the height of the 'RelativeLayoutSize' to the value of 'px1'
                holder.btnDown.setImageResource(R.drawable.uparrows);
                holder.RelativeLayoutSize.getLayoutParams().height=px1;
                // Set the 'expanded' property of the current item in the list to true
                contacts.get(position).setExpanded(true);

            }
            else{
                // If the item is expanded, set the image of the 'btnDown' ImageView to the 'downarrow' drawable and set the height of the 'RelativeLayoutSize' to the value of 'px2'
                holder.btnDown.setImageResource(R.drawable.downarrow);
                holder.RelativeLayoutSize.getLayoutParams().height=px2;
                // Set the 'expanded' property of the current item in the list to false
                contacts.get(position).setExpanded(false);
            }
            // Notify the adapter that the data has changed, causing the UI to update
            notifyDataSetChanged();

        });



    }
    // Return the number of items in the list
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCompanyReasons(ArrayList<String> CompanyReasons) {
        // Set the 'CompanyReasons' field to the passed in value and notify the adapter that the data has changed, causing the UI to update
        this.CompanyReasons = CompanyReasons;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setContacts(ArrayList<Contact> contacts) {
        // Set the 'contacts' field to the passed in value and notify the adapter that the data has changed, causing the UI to update
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Declare variables for the views that will be used in the RecyclerView
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
            // Initialize the views by finding them by their ID
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
