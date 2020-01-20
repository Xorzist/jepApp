package com.example.jepapp.Fragments.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Users.MyOrdersAdapter;
import com.example.jepapp.Models.Comments;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class profilepage extends Fragment {
    private FirebaseAuth mAuth;
    DatabaseReference myDBRef;
    List<Orders> myOrderslist = new ArrayList<>();
    List<Comments> myCommentslist = new ArrayList<>();
    TextView orderscount,commentscount;

    // RecyclerView.Adapter adapter ;
    public MyOrdersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_profilepage, container, false);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        myOrderslist = new ArrayList<>();
        myCommentslist = new ArrayList<Comments>();
        mAuth = FirebaseAuth.getInstance();
        orderscount= rootView.findViewById(R.id.ordercount);
        commentscount=rootView.findViewById(R.id.commentscount);
        Query allordersquery = myDBRef.child("Orders").orderByChild("orderID").equalTo(mAuth.getUid());

        allordersquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders allmyorders = dataSnapshot.getValue(Orders.class);
                    //Log.e("onDataChange: ", allmyorders.getTitle().toString());

                    myOrderslist.add(allmyorders);

                }
                orderscount.setText(String.valueOf(myOrderslist.size()));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
        Query allcommentsquery = myDBRef.child("Comments").orderByChild("commentID").equalTo(mAuth.getUid());

        allcommentsquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Comments allmycomments = dataSnapshot.getValue(Comments.class);
                    //Log.e("onDataChange: ", allmyorders.getTitle().toString());

                    myCommentslist.add(allmycomments);

                }
                commentscount.setText(String.valueOf(myCommentslist.size()));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });




        return rootView;
    }

}


