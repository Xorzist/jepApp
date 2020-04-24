package com.example.jepapp.Adapters.Users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Users.Cart;
import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.Fragments.User.MyOrders;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyorderequestsAdapter extends RecyclerView.Adapter<MyorderequestsAdapter.MyorderequestAdapterViewHolder> {
    private Context mcontext;

    private List<Orders> RequestsList;
    private DatabaseReference mydbreference;
    private FirebaseAuth mAuth;
    private UserCredentials TheUser;

    public MyorderequestsAdapter(Context mCtx, List<Orders> requestsList) {
        this.mcontext = mCtx;
        this.RequestsList = requestsList;
    }

    @Override
    public MyorderequestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout for the items that will populate or list
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.myorderequestslayout,null);
        mydbreference = FirebaseDatabase.getInstance().getReference("JEP");
        mAuth = FirebaseAuth.getInstance();
        GetUser();
        return new MyorderequestAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyorderequestAdapterViewHolder holder, int position) {
        //Gets the specific object based on location of an item on the recycler view
        final Orders item  = RequestsList.get(position);
        holder.username.setText(item.getUsername());
        holder.orderamount.setText(String.valueOf(item.getCost()));
        //Approve the order to be paid for by the current user on behalf of someone else
        holder.approve.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Long payeeBalance = (Float.valueOf(TheUser.getAvailable_balance())).longValue();
                if (payeeBalance> item.getCost()) {
                    mydbreference.child(item.getType() + "Orders")
                            .child(item.getOrderID())
                            .child("status")
                            .setValue("Incomplete");
                    String newbalance = String.valueOf((payeeBalance - item.getCost()));
                    String emailfield = mAuth.getCurrentUser().getEmail().toString().replace(".", "");
                    mydbreference.child("Users").child(emailfield).child("available_balance").setValue(newbalance);
                    for (String s : item.getOrdertitle()) {
                        String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                        String noparantheses = s.split("[\\](},]")[0];
                        if (item.getType().toLowerCase().equals("Lunch")) {
                            UpdateMenu("Lunch", number, noparantheses);
                        } else {
                            UpdateMenu("BreakfastMenu", number, noparantheses);
                        }

                    }
                    Intent inside = new Intent(mcontext, CustomerViewPager.class);
                    mcontext.startActivity(inside);
                    ((Activity) mcontext).finish();

                }
                else{
                    Toast.makeText(mcontext.getApplicationContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Deny the order to be paid for by the current user on behalf of someone else
        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydbreference.child(item.getType()+"Orders")
                        .child(item.getOrderID())
                        .child("status")
                        .setValue("denied");
                Intent inside = new Intent(mcontext, CustomerViewPager.class);
                mcontext.startActivity(inside);
                ((Activity)mcontext).finish();

            }
        });


    }

    @Override
    public int getItemCount() {
        return RequestsList.size();
    }

    class MyorderequestAdapterViewHolder  extends RecyclerView.ViewHolder{
        TextView username,orderamount;
        Button approve,deny;
        public MyorderequestAdapterViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.payeeid);
            orderamount = itemView.findViewById(R.id.payeeamount);
            approve = itemView.findViewById(R.id.approverequest);
            deny = itemView.findViewById(R.id.denyrequest);

        }
    }
    //Function to get the details of the user who will pay for the order
    public void GetUser(){

        Query emailquery = mydbreference.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());


        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);
                    TheUser = (userCredentials);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    //This function will use only the title of an item within a specific menutype and update the quantity
    // of the corresponding Menu item
    private void UpdateMenu(String mMenuType, final String morderquantities, final String mitemtitlesonly) {
        final DatabaseReference ref = mydbreference.child(mMenuType);
        ref.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String title=data.child("title").getValue().toString();
                    if(title.equals(mitemtitlesonly)){
                        String keyid=data.getKey();
                        String oldvalue = data.child("quantity").getValue().toString();
                        int newvalue= (Integer.valueOf(oldvalue)) - (Integer.valueOf(morderquantities));
                        ref.child(keyid).child("quantity").setValue(String.valueOf(newvalue));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
