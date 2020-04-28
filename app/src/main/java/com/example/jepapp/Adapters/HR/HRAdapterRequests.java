package com.example.jepapp.Adapters.HR;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.GMailSender;
import com.example.jepapp.Models.HR.Requests;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class HRAdapterRequests extends RecyclerView.Adapter<HRAdapterRequests.UserViewHolder> {

    private List<Requests> requestsList;
    private List<UserCredentials> userList;
    private Context context;
    private static int currentPosition = -1;
    private String subject = "RE: Request for balance addition";
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");

    public HRAdapterRequests(Context context, List<Requests> requestsList, List<UserCredentials> userList){
        this.context = context;
        this.requestsList = requestsList;
        this.userList = userList;

    }


    @NonNull
    @Override
    public HRAdapterRequests.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hr_requests_outline, parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HRAdapterRequests.UserViewHolder holder, final int position) {

        final  Requests user = requestsList.get(position);
        holder.Username.setText("Username:\n" + user.getUsername());
        holder.Request_Amount.setText("Request amount:\n" + user.getamount());
        holder.Request_Date.setText("Date: \n" + user.getdate());
        holder.linearLayout.setVisibility(View.GONE);
        holder.Request_EmpID.setText("Employee ID:\n" + user.getEmpID());


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
                    if (currentPosition == position) {
                        currentPosition = -1;

                    } else {
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
                final AlertDialog.Builder acceptRequestDialog = new AlertDialog.Builder(context);
                acceptRequestDialog.create();
                acceptRequestDialog.setTitle("Accept User Request");
                acceptRequestDialog.setMessage("Please note $"+ user.getamount()+ " will be added to "+ user.getUsername()+"'s"+" current balance");
                acceptRequestDialog.setCancelable(true);
                acceptRequestDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //checking userlist for model that has same userID
                        for(int i=0; i<userList.size(); i++){
                            if(userList.get(i).getUserID().equals(requestsList.get(position).getUserID())){
                                final UserCredentials userinfo = userList.get(i);
                                //adding request amount to users current balance
                                int current_balance = Integer.parseInt(userinfo.getBalance());
                                int avail_balance = Integer.parseInt(userinfo.getAvailable_balance());
                                int value = Integer.parseInt(user.getamount());
                                int new_balance = current_balance+value;
                                int newavail_balance = avail_balance + value;
                                //update users balance in the database
                                doupdate(String.valueOf(new_balance),String.valueOf(newavail_balance),userinfo);
                                String state = "accepted";
                                String message = "Dear "+ userinfo.getUsername() +",\n"+"Your request of $"+ user.getamount()+" has been "+ state +"."  + " Please check your balance for updates"+"\nThank you for using our system and happy eating";
                                //send user an email with the new status of their application
                                sendEmail(userinfo.getEmail(), message, subject);
                                // update the state of request in database
                                updateRequest(requestsList.get(position), state, databaseReference);

                            }
                        }

                    }
                });
                acceptRequestDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = acceptRequestDialog.create();
                alertDialog.show();
            }
        });
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder declineRequestDialog = new AlertDialog.Builder(context);
                declineRequestDialog.create();
                declineRequestDialog.setTitle("Decline User Request");
                declineRequestDialog.setMessage("Please note if you decline this request the user's balance will not be updated");
                declineRequestDialog.setCancelable(true);
                declineRequestDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
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
                                notifyDataSetChanged();

                            }
                        }

                    }
                });
                declineRequestDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = declineRequestDialog.create();
                alertDialog.show();
            }
        });

    }
    //updates users balance and available balance in firebase
    private void doupdate(final String value, String s, UserCredentials user) {
        final ProgressDialog progressDialog1 = new ProgressDialog(context);
        progressDialog1.setMessage("Updating User");
        progressDialog1.show();
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        String key=user.getEmail();
        String email = key.replace(".","");
        databaseReference1.child(email).child("balance").setValue(value);
        databaseReference1.child(email).child("available_balance").setValue(s);
        progressDialog1.cancel();
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

        UserViewHolder(View itemview) {
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

    //updates the state of request in database
    private void updateRequest(Requests request, final String state, DatabaseReference databaseReference){
        databaseReference.child(request.getKey()).child("status").setValue(state);
        notifyDataSetChanged();
        Toast toast = Toast.makeText(context,
                "Request has been processed",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    //updates the adapter with the results of the search
    public void updateList(List<Requests> newList){
        requestsList = new ArrayList<>();
        requestsList = newList;
        notifyDataSetChanged();
    }


}
