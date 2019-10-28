package com.example.jepapp.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {


    private boolean a;

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
    private ArrayList<String> arrayListTitle = new ArrayList();
    private ArrayList<String> arrayListQuantity = new ArrayList();



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
        //holder.checkBox.setChecked(mSelectedItemsIds.get(position));
        setA(holder.checkBox.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.checkBox.isChecked()){
                    Log.d("Checkers", "checked");

                    holder.quantity.setVisibility(VISIBLE);

                    arrayListTitle.add(item.getTitle().toString());
                    arrayListQuantity.add(holder.quantity.getText().toString());

                }else{

                    Log.d("Checkers", "unchecked");

                    holder.quantity.setVisibility(View.INVISIBLE);
                    arrayListTitle.remove(item.getTitle().toString());
                    arrayListQuantity.remove(holder.quantity.getText());


                    }
                }

        });
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
    public int checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);


        else
            mSelectedItemsIds.delete(position);

        //notifyDataSetChanged();
        return position;
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public ArrayList<String> getTitle(){

        return arrayListTitle;

    }

    public ArrayList<String> getQuantity(){

        return arrayListQuantity;

    }
    public void getAllSelected(int position, boolean value) {
        final Admin_Made_Menu item = arrayList.get(position);
       // DeleteList();
        if (value) {
            getTitle();
            getQuantity();
            //mSelectedItemsIds.put(position, true);

        }
        else
            //mSelectedItemsIds.delete(position);
            //DeleteList(position);
            DeleteList();

    }

    public void getAllSelectedafterdelete(int position, boolean value) {
        Log.e("m","I'm being called but nah do nunth");

        final Admin_Made_Menu item = arrayList.get(position);
        // DeleteList();

        if (value) {
            //mSelectedItemsIds.put(position, true);
            arrayListTitle.add(item.getTitle());
            arrayListQuantity.add(String.valueOf(item.getQuantity()));
        }
        else
            //mSelectedItemsIds.delete(position);
            //DeleteList(position);
            return;

    }

//    public void DeleteList(int position){
//        arrayListTitle.remove(position);
//        arrayListQuantity.remove(position);
//        Log.e("deleted second value",arrayListTitle.toString());
//
//    }
    public void DeleteList(){
        arrayListTitle.clear();
        arrayListQuantity.clear();
        Log.e("deleted second value",arrayListTitle.toString());
       // findSelected();
        for (int i=0; i<arrayList.size(); i++){
            getAllSelectedafterdelete(i,a);



    }}

    private void findSelected() {


    }
    public boolean isA() {
        return a;
    }

    public void setA(boolean a) {
        this.a = a;
    }


}