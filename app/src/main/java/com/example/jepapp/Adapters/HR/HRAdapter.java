package com.example.jepapp.Adapters.HR;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.GMailSender;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HRAdapter extends RecyclerView.Adapter<HRAdapter.UserViewHolder> {

    private List<UserCredentials> userList;
    private Context context;
    private static int currentPosition = -1;
    private String subject = "Account balance update";

    public HRAdapter(Context context, List<UserCredentials> userList){
    this.context = context;
    this.userList = userList;

    }


    @NonNull
    @Override
    public HRAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hr_userlist_outline, parent,false);
        UserViewHolder holder = new UserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HRAdapter.UserViewHolder holder, final int position) {

        final  UserCredentials user = userList.get(position);
        holder.Username.setText(user.getUsername());
        holder.Balance.setText(user.getBalance());
        holder.linearLayout.setVisibility(View.GONE);
        holder.EmployeeID.setText(user.getEmpID());

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
        if (user.getBalance().equals("new")){
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.update_user_balance, null);
                    final AlertDialog.Builder newUserUpdate = new AlertDialog.Builder(context);
                    newUserUpdate.setView(promptsView);
                    newUserUpdate.setTitle("Edit User Balance");
                    newUserUpdate.setMessage("Please note that the value entered below will become "+ user.getUsername()+ " new balance."+"\n NB: If the user is not a member of staff their status may be changed to 'Visitor'");
                    newUserUpdate.setCancelable(true);
                    final EditText new_balance = promptsView.findViewById(R.id.new_balance_alertdialog);
                    new_balance.setText("0");
                    newUserUpdate.setPositiveButton(context.getString(R.string.Edit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!new_balance.getText().toString().isEmpty()){
                                String value = (new_balance.getText().toString());
                                //search for user info from userlist
                                for (int i=0; i<userList.size(); i++) {
                                    if (userList.get(i).getEmpID().equals(user.getEmpID())) {
                                        List<UserCredentials> newlist = new ArrayList<>();
                                        newlist.add(userList.get(i));
                                        String message = "Dear " + newlist.get(0).getUsername() + ", \nYour balance has been changed. Your new balance is $" + value + "."+"\nThank you for using our system and happy eating";
                                        //updates user balance
                                        doupdate(value, value, user);

                                        //sends an email to the user
                                        sendEmail(user.getEmail(), message, subject);

                                    }
                                }
                            }
                            else{
                                Toast toast = Toast.makeText(context,"Please enter an amount", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });
                    newUserUpdate.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = newUserUpdate.create();
                    alertDialog.show();

                }
            });
        }
        else
            {

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //getting the position of the item to expand it
                    if (currentPosition == position) {
                        currentPosition = -1;

                    } else if (currentPosition != position) {
                        currentPosition = position;
                    }


                    //reloading the list
                    notifyDataSetChanged();
                }

            });


        }
        //shows user information
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.hr_userinfo);
                dialog.setTitle("User Information");
                // set the custom dialog components
                TextView name, empid, username, department, contact, balance, email;
                Button button;
                name = dialog.findViewById(R.id.userinfo_name);
                username = dialog.findViewById(R.id.userinfo_username);
                email = dialog.findViewById(R.id.userinfo_email);
                empid = dialog.findViewById(R.id.userinfo_empid);
                contact = dialog.findViewById(R.id.userinfo_contact);
                balance = dialog.findViewById(R.id.userinfo_balance);
                department = dialog.findViewById(R.id.userinfo_department);
                button= dialog.findViewById(R.id.userinfo_okaybutton);
                name.setText("Name: " +user.getName());
                username.setText("Username: " +user.getUsername());
                email.setText("Email: "+user.getEmail());
                empid.setText("Employee ID: " + user.getEmpID());
                contact.setText("Contact #: "+user.getContactnumber());
                balance.setText("Balance: "+user.getBalance());
                department.setText("Department: "+user.getDepartment());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                //shows custom dialog
                View promptsView = li.inflate(R.layout.update_user_balance, null);
                final AlertDialog.Builder subtractAlert = new AlertDialog.Builder(context);
                subtractAlert.setView(promptsView);
                subtractAlert.setTitle("Subtract From User Balance");
                subtractAlert.setMessage("Please note that the value entered below will be subtracted from "+ user.getUsername()+ "'s balance");
                subtractAlert.setCancelable(true);
                final EditText new_balance = promptsView.findViewById(R.id.new_balance_alertdialog);
                subtractAlert.setPositiveButton(context.getString(R.string.Subtract), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!new_balance.getText().toString().isEmpty()){
                            //gets user data
                            int current_balance = Integer.parseInt(user.getBalance());
                            int available_balance = Integer.parseInt(user.getAvailable_balance());
                            int value = Integer.parseInt(new_balance.getText().toString());
                            //calculates new balances
                            int new_balance = current_balance - value;
                            int avail_bal = available_balance - value;
                            String message = "Your balance has been changed. Your new available balance is $" + avail_bal +"."+"\nThank you for using our system and happy eating";;
                            //updates fields in database
                            doupdate(String.valueOf(new_balance), String.valueOf(avail_bal), user);
                            //sends the user an email notifying them of changes made
                            sendEmail(user.getEmail(),message, subject);


                        }
                        else{
                            Toast toast = Toast.makeText(context,"Please enter an amount", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                subtractAlert.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = subtractAlert.create();
                alertDialog.show();
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutInflater li = LayoutInflater.from(context);
                 //shows custom dialog
                View promptsView = li.inflate(R.layout.update_user_balance, null);
                final AlertDialog.Builder addBalanceBuilder = new AlertDialog.Builder(context);
                addBalanceBuilder.setView(promptsView);
                addBalanceBuilder.setTitle("Update User Balance");
                addBalanceBuilder.setMessage("Please note the value entered below will be added to "+ user.getUsername()+"'s current balance");
                addBalanceBuilder.setCancelable(true);
                final EditText new_balance = promptsView.findViewById(R.id.new_balance_alertdialog);
                addBalanceBuilder.setPositiveButton(context.getString(R.string.Addtobalance), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!new_balance.getText().toString().isEmpty()){
                             //gets user data
                            int current_balance = Integer.parseInt(user.getBalance());
                            int available_balance = Integer.parseInt(user.getAvailable_balance());
                            int value = Integer.parseInt(new_balance.getText().toString());
                            //calculates new balances
                            int new_balance = current_balance+value;
                            int avail_bal = available_balance + value;
                            String message = "$"+value+" has been added to your account. Your new balance is $" + new_balance +"."+"\nThank you for using our system and happy eating";
                            //updates fields in database
                            doupdate(String.valueOf(new_balance), String.valueOf(avail_bal), user);
                            //sends the user an email notifying them of changes made
                            sendEmail(user.getEmail(),message, subject);
                        }
                        else{
                            Toast toast = Toast.makeText(context,"Please enter an amount", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                addBalanceBuilder.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = addBalanceBuilder.create();
                alertDialog.show();
            }
        });

    }


    private void doupdate(final String value, String availbal, final UserCredentials user) {
        final ProgressDialog updatingDialog = new ProgressDialog(context);
        updatingDialog.setMessage("Updating User");
        updatingDialog.show();
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        String key=user.getEmail();
        String email = key.replace(".","");
        databaseReference1.child(email).child("balance").setValue(value);
        databaseReference1.child(email).child("available_balance").setValue(availbal);
        updatingDialog.cancel();
    }
    private void sendEmail(String email, String message, String subject) {

        //Creating SendMail object
        GMailSender sm = new GMailSender(context, email, message, subject);

        //Executing sendmail to send email
        sm.execute();
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends  RecyclerView.ViewHolder{
        TextView Username, Balance, EmployeeID;
        LinearLayout parent, linearLayout;
        Button edit, update;

        UserViewHolder(View itemview) {
            super(itemview);
            Username = itemview.findViewById(R.id.hr_username);
            Balance = itemview.findViewById(R.id.hr_balance);
            parent = itemview.findViewById(R.id.parent_layout2);
            linearLayout = itemview.findViewById(R.id.linearLayout);
            EmployeeID = itemview.findViewById(R.id.hr_empID);
            edit = itemview.findViewById(R.id.edit);
            update = itemview.findViewById(R.id.update);
        }
    }
    public void updateList(List<UserCredentials> newList){
        userList = newList;
        notifyDataSetChanged();
    }

}

