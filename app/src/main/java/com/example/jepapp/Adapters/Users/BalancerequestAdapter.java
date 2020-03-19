package com.example.jepapp.Adapters.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.HR.Requests;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;

import java.util.List;

public class BalancerequestAdapter extends RecyclerView.Adapter<BalancerequestAdapter.BalancerequestViewHolder> {
    private Context mcontext;

    private List<Requests> RequestsList;

    public BalancerequestAdapter(Context mCtx, List<Requests> requestsList) {
        this.mcontext = mCtx;
        this.RequestsList = requestsList;
    }

    @Override
    public BalancerequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout for the items that will populate or list
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.pastbalancerequestslayout,null);
        return new BalancerequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BalancerequestViewHolder holder, int position) {
        //Gets the specific object based on location of an item on the recycler view
        final Requests item  = RequestsList.get(position);

        //Assigning attributes to the specific location item
        holder.balancerequest.setText(String.format("Your request of %s ", String.valueOf(item.getAmount())));


    }

    @Override
    public int getItemCount() {
        return RequestsList.size();
    }

     class BalancerequestViewHolder  extends RecyclerView.ViewHolder{
         TextView balancerequest;
         ImageView balancestatus;
         public BalancerequestViewHolder(View itemView) {
             super(itemView);

             balancerequest = itemView.findViewById(R.id.balance_requestinfo);

         }
     }
}
