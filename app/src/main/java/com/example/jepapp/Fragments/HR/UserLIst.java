package com.example.jepapp.Fragments.HR;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.HR.HRAdapter;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserLIst extends Fragment{

    private RecyclerView hrrecyclerView;
    HRAdapter adapter;
    private List<UserCredentials> userlist;
    private Button update_all;
    LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    ProgressDialog progressDialog;
    private TextView emptyView;
    DatabaseReference databaseReference;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hr_userlist_layout, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        emptyView = rootView.findViewById(R.id.empty_view);
        userlist = new ArrayList<>();
        update_all = rootView.findViewById(R.id.update_all);
        hrrecyclerView = (RecyclerView) rootView.findViewById(R.id.hr_people_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(hrrecyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new HRAdapter(getContext(),userlist);
        hrrecyclerView.setLayoutManager(linearLayoutManager);
        hrrecyclerView.addItemDecoration(dividerItemDecoration);
        hrrecyclerView.setAdapter(adapter);

        getUserData();

        update_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());

                View promptsView = li.inflate(R.layout.update_user_balance, null);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setView(promptsView);
                builder1.setTitle("Update All User Balances");
                builder1.setMessage("Please note the value entered below will be added to all users' current balance");
                builder1.setCancelable(true);
                final EditText new_balance = promptsView.findViewById(R.id.new_balance_alertdialog);
                builder1.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!new_balance.getText().toString().isEmpty()){

                            databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

                            final int user_balance_to_add = Integer.parseInt(new_balance.getText().toString());
                            for (int i=0; i<userlist.size(); i++){
                                int balance = Integer.parseInt(userlist.get(i).getBalance());
                                String key = userlist.get(i).getKey();
                                final int value= balance + user_balance_to_add;

                                Query update= databaseReference.orderByChild("key").equalTo(key);
                                update.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        //UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);
                                        dataSnapshot.getRef().child("balance").setValue(String.valueOf(value));

                                    }
                                    adapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });
                        }}
                        else{
                            Toast toast = Toast.makeText(getContext(),"Please enter an amount", Toast.LENGTH_LONG);
                            toast.show();
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


        return  rootView;
    }

    private void getUserData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Users");

        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);

                    userlist.add(allusers);

                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }
}
