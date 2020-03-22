package com.example.jepapp.Adapters.HR;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Fragments.HR.Page2;
import com.example.jepapp.GMailSender;
import com.example.jepapp.Models.HR.Requests;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HRAdapterRequests extends RecyclerView.Adapter<HRAdapterRequests.UserViewHolder> {

    private List<Requests> requestsList;
    private List<Requests> filteredrequestList;
    private List<UserCredentials> userList;
    private Context context;
    private static int currentPosition = -1;
    private String subject = "RE: Request for balance addition";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");

    public HRAdapterRequests(Context context, List<Requests> requestsList, List<UserCredentials> userList){
        this.context = context;
        this.requestsList = requestsList;
        this.userList = userList;
        filteredrequestList = new ArrayList<>(requestsList);

    }


    @NonNull
    @Override
    public HRAdapterRequests.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hr_requests_outline, parent,false);
        UserViewHolder holder = new UserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HRAdapterRequests.UserViewHolder holder, final int position) {

        final  Requests user = requestsList.get(position);
        holder.Username.setText("Username:\n" + user.getUsername());
        holder.Request_Amount.setText("Request amount:\n" + user.getamount());
        holder.Request_Date.setText("Date: \n" + user.getdate());
        holder.linearLayout.setVisibility(View.GONE);
        holder.Request_EmpID.setText("Employee ID:\n" + user.getUserID());

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
        }
        else if (currentPosition == -1){
            Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            holder.linearLayout.setVisibility(View.GONE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideUp);

        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //getting the position of the item to expand it
                if(currentPosition==position){
                    currentPosition = -1;

                }
                else if (currentPosition!=position){
                    currentPosition = position;
                }


                //reloading the list
                notifyDataSetChanged();
            }

        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               // building dialog
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.create();
                builder1.setTitle("Accept User Request");
                builder1.setMessage("Please note $"+ user.getamount()+ " will be added to "+ user.getUsername()+"'s"+" current balance");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //checking userlist for model that has same userID
                        for(int i=0; i<userList.size(); i++){
                            if(userList.get(i).getUserID().equals(requestsList.get(position).getUserID())){
                                final UserCredentials userinfo = userList.get(i);
                                //adding request amount to users current balance
                                int current_balance = Integer.parseInt(userinfo.getBalance());
                                int value = Integer.parseInt(user.getamount());
                                int new_balance = current_balance+value;
                                //update users balance in the database
                                doupdate(String.valueOf(new_balance),userinfo);
                                String state = "accepted";
                                String message = "Dear "+ userinfo.getUsername() +",\n"+"Your request of $"+ user.getamount()+" has been "+ state +"."  + " Please check your balance for updates";
                                //send user an email with the new status of their application
                                sendEmail(userinfo.getEmail(), message, subject);
                                // update the state of request in database
                                updateRequest(requestsList.get(position), state, databaseReference);

                            }
                        }

                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
            }
        });
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.create();
                builder1.setTitle("Decline User Request");
                builder1.setMessage("Please note if you decline this request the user's balance will not be updated");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get user data who sent the request
                        for(int i=0; i<userList.size(); i++){
                            if(userList.get(i).getUserID().equals(requestsList.get(position).getUserID())){
                                final UserCredentials userinfo = userList.get(i);
                                String state = "denied";
                                String message = "Dear "+ userinfo.getUsername() +",\n"+"We regret to inform you that your request of $"+ user.getamount()+" to be added to your account has been "+ state +"." + "\n Please contact Human Resources for further details or try again later";
                               //send email with status of application to user email
                                sendEmail(userinfo.getEmail(), message, subject);
                                //updates status of request in database
                                 updateRequest(requestsList.get(position), state, databaseReference);


                            }
                        }

                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
            }
        });

    }

    private void doupdate(final String value, UserCredentials user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        Query update_Balance= databaseReference.orderByChild("email").equalTo(user.getEmail());
        update_Balance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updateQuantity: dataSnapshot.getChildren()){
                    updateQuantity.getRef().child("balance").setValue(value);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendEmail(String email, String message, String subject) {

        //Creating SendMail object
        GMailSender sm = new GMailSender(context, email, message, subject);

        //Executing sendmail to send email
        sm.execute();
    }


    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    class UserViewHolder extends  RecyclerView.ViewHolder{
        TextView Username, Request_Amount, Request_Date, Request_EmpID;
        LinearLayout parent, linearLayout;
        Button accept, decline;

        public UserViewHolder(View itemview) {
            super(itemview);
            Username = itemview.findViewById(R.id.hr_username);
            Request_Amount = itemview.findViewById(R.id.hr_request_amount);
            Request_Date = itemview.findViewById(R.id.request_date);
            parent = itemview.findViewById(R.id.parent_layout2);
            linearLayout = itemview.findViewById(R.id.linearLayout);
            accept = itemview.findViewById(R.id.Accept);
            decline = itemview.findViewById(R.id.decline);
            Request_EmpID = itemview.findViewById(R.id.hr_request_empid);
        }
    }

    public void updateRequest(Requests request, final String state, DatabaseReference databaseReference){
        Query update_Status= databaseReference.orderByChild("key").equalTo(request.getKey());
        update_Status.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updateStatus: dataSnapshot.getChildren()){
                    updateStatus.getRef().child("status").setValue(state);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        Toast toast = Toast.makeText(context,
                "Request has been processed",
                Toast.LENGTH_SHORT);
        toast.show();
    }


    public void updateList(List<Requests> newList){
        requestsList = new ArrayList<>();
        requestsList = newList;
        notifyDataSetChanged();
    }
}
