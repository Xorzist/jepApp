package com.example.jepapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;

import java.util.List;

import static android.view.View.VISIBLE;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView label;
        //private RelativeLayout quantity;
        private CheckBox checkBox;
        private EditText quantity;


        RecyclerViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.label);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox1);
            //quantity = (RelativeLayout) view.findViewById(R.id.quantityvisible);
            quantity = (EditText) view.findViewById(R.id.quantity);


        }

    }

    private List<Admin_Made_Menu> arrayList;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;


    public RecyclerViewAdapter(Context context, List<Admin_Made_Menu> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_custom_row_layout, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final Admin_Made_Menu item = arrayList.get(position);
        holder.label.setText(item.getTitle());
        holder.checkBox.setChecked(mSelectedItemsIds.get(position));


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    Log.d("Checkers", "checked");
                    //checkCheckBox(position, !mSelectedItemsIds.get(position));
                    holder.quantity.setVisibility(VISIBLE);
                }else{
                    Log.d("Checkers", "unchecked");
                    //checkCheckBox(position, !mSelectedItemsIds.get(position));
                    holder.quantity.setVisibility(View.INVISIBLE);
                }
//                Log.d("Checkers", "clickr");
//                //checkCheckBox(position, !mSelectedItemsIds.get(position));
//                holder.quantity.setVisibility(VISIBLE);
            }
        });
//
//        holder.label.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkCheckBox(position, !mSelectedItemsIds.get(position));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);


        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}