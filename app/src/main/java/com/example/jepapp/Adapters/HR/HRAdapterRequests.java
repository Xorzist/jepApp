package com.example.jepapp.Adapters.HR;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.HR.Requests;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HRAdapterRequests extends RecyclerView.Adapter<HRAdapterRequests.UserViewHolder> {

    private List<Requests> requestsList;
    private List<UserCredentials> userList;
    private Context context;
    private static int record;
    private static int currentPosition = -1;

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
        UserViewHolder holder = new UserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HRAdapterRequests.UserViewHolder holder, final int position) {

        final  Requests user = requestsList.get(position);
        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getUserID().equals(requestsList.get(position).getUserID())){
                 record = i;

            }
        }
        Log.e("confused", String.valueOf(userList.size()));
       final UserCredentials user2 = userList.get(record);
        holder.Username.setText(user.getUsername());
        holder.Request_Amount.setText(user.getBalance());
        holder.linearLayout.setVisibility(View.GONE);

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


                //reloding the list
                notifyDataSetChanged();
            }

        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               // getUserData();
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.create();
                builder1.setTitle("Update User Balance");
                builder1.setMessage("Please note "+ user.getBalance()+ " will be added to "+ user.getUsername()+" current balance");
                builder1.setCancelable(true);
               // final EditText new_balance = promptsView.findViewById(R.id.new_balance_alertdialog);
                builder1.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // if (!new_balance.getText().toString().isEmpty()){
                            int current_balance = Integer.parseInt(user.getBalance());
                            int value = Integer.parseInt(user2.getBalance());
                            int new_balance = current_balance+value;
                            doupdate(String.valueOf(new_balance),user);
                            deleteRequest(requestsList.get(position));
                       // }
                       // else{
//                            Toast toast = Toast.makeText(context,"Please enter an amount", Toast.LENGTH_LONG);
//                            toast.show();
//                        }
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

    private void doupdate(final String value, Requests user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        Query update_Quantity = databaseReference.orderByChild("key").equalTo(user.getKey());
        update_Quantity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updateQuantity: dataSnapshot.getChildren()){
                    updateQuantity.getRef().child("balance").setValue(value.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    class UserViewHolder extends  RecyclerView.ViewHolder{
        TextView Username, Request_Amount;
        LinearLayout parent, linearLayout;
        Button accept, decline;

        public UserViewHolder(View itemview) {
            super(itemview);
            Username = itemview.findViewById(R.id.hr_username);
            Request_Amount = itemview.findViewById(R.id.hr_request_amount);
            parent = itemview.findViewById(R.id.parent_layout2);
            linearLayout = itemview.findViewById(R.id.linearLayout);
            accept = itemview.findViewById(R.id.Accept);
            decline = itemview.findViewById(R.id.decline);
        }
    }
    public void deleteRequest(Requests remove){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");
        databaseReference.child(remove.getKey()).removeValue();
        Log.e("Keytime", remove.getKey());

    }




}

