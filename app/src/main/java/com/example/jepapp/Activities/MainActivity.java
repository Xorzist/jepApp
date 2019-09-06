package com.example.jepapp.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;

import com.example.jepapp.Adapters.RecyclerViewAdapter;
import com.example.jepapp.Fragments.Make_Menu;
import com.example.jepapp.R;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    private RecyclerViewAdapter adapter;
    private ArrayList<String> arrayList, arrayList2;
    private Button selectButton;

//    public FragmentRefreshListener getFragmentRefreshListener() {
//        return fragmentRefreshListener;
//    }
//
//    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
//        this.fragmentRefreshListener = fragmentRefreshListener;
//    }
//
//    private FragmentRefreshListener fragmentRefreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectButton = (Button) findViewById(R.id.select_button);
        populateRecyclerView();
       // loadData();
        onClickEvent();
    }
    private void populateRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        arrayList.add("RecyclerView Items ");
        arrayList.add("Banana");//Adding items to recycler view
        arrayList.add("Apple");


        adapter = new RecyclerViewAdapter(this, arrayList);
       // adapter2 = new RecyclerViewAdapter(this, arrayList2);
        recyclerView.setAdapter(adapter);
    }
    private void onClickEvent() {
        findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                if (selectedRows.size() > 0) {
                    arrayList2 = new ArrayList<>();
                   // StringBuilder stringBuilder = new StringBuilder();
                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedRows.size(); i++) {

                        //Check if selected rows have value i.e. checked item
                        if (selectedRows.valueAt(i)) {

                            //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                            String selectedRowLabel = arrayList.get(selectedRows.keyAt(i));
                            arrayList2.add(selectedRowLabel);
                            //append the row label text
                            //stringBuilder.append(selectedRowLabel + "\n");
                        }
                    }
//                    RecyclerView recyclerView2 = (RecyclerView)findViewById(R.id.recycler_view2);
//                    recyclerView2.setHasFixedSize(true);
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
//                    recyclerView2.setLayoutManager(linearLayoutManager);
//                    adapter2 = new RecyclerViewAdapter(getBaseContext(), arrayList2);
//                    recyclerView2.setAdapter(adapter2);

                    if (selectedRows != null && !arrayList2.isEmpty()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mylist", arrayList2);
                        Make_Menu make_menu = new Make_Menu();
                        //Make_Menu make_menu = (Make_Menu) getFragmentManager().findFragmentById(R.id.fragment_container);
                        make_menu.setArguments(bundle);
                        FragmentTransaction fragment_transaction = getSupportFragmentManager().beginTransaction();
                        fragment_transaction.replace(R.id.fragment_containeryes, make_menu);
                        fragment_transaction.commit();

                        //return make_menu;
                        Log.d("ting","List being set");
                        Log.d("list", bundle.toString());

//                        Make_Menu myFragment = (Make_Menu) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                        if(myFragment != null && myFragment.isAdded(){
//                            myFragment.myRecyclerView.notifyItemRemoved();
//                        }

//                        if(getFragmentRefreshListener()!=null){
//                            getFragmentRefreshListener().onRefresh();
//                        }


//
//                        FragmentTransaction fragment_transaction = getSupportFragmentManager().beginTransaction();
//                        fragment_transaction.add(R.id.fragment_container, make_menu);
//                        fragment_transaction.commit();

//                        Intent intent = new Intent(getApplicationContext(), Make_Menu.class);
//                        intent.putExtra("mylist", arrayList2);
//                        startActivity(intent);
//                        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        Gson gson = new Gson();
//                        String json = gson.toJson(arrayList2);
//                        editor.putString("task list", json);
//                        editor.apply();
                    } else {
                        Log.e("123", "Avoiding null pointer, the routes are null!!!");

                    }


                   // Toast.makeText(.this, "Selected Rows\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
//        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
//                //Check if item is selected or not via size
//                if (selectedRows.size() > 0) {
//                    //Loop to all the selected rows array
//                    for (int i = (selectedRows.size() - 1); i >= 0; i--) {
//
//                        //Check if selected rows have value i.e. checked item
//                        if (selectedRows.valueAt(i)) {
//
//                            //remove the checked item
//                            arrayList.remove(selectedRows.keyAt(i));
//                        }
//                    }
//
//                    //notify the adapter and remove all checked selection
//                    adapter.removeSelection();
//                }
//            }
//        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check the current text of Select Button
                if (selectButton.getText().toString().equals(getResources().getString(R.string.select_all))) {

                    //If Text is Select All then loop to all array List items and check all of them
                    for (int i = 0; i < arrayList.size(); i++)
                        adapter.checkCheckBox(i, true);

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.deselect_all));
                } else {
                    //If button text is Deselect All remove check from all items
                    adapter.removeSelection();

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.select_all));
                }
            }
        });
    }


//    public interface FragmentRefreshListener{
//        void onRefresh();
//    }

}

//    private  void loadData(){
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("task list", null);
//        Type type = new TypeToken<ArrayList<Data>>() {}.getType();
//        arrayList2 = gson.fromJson(json, type);
//
//        if (arrayList2 == null) {
//            arrayList2 = new ArrayList<>();
//        }
//    }


