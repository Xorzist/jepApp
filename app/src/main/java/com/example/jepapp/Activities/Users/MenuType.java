package com.example.jepapp.Activities.Users;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Users.MenuTypeAdapter;
import com.example.jepapp.Models.MenuTypes;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class MenuType extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuTypeAdapter adapter;
    private List<MenuTypes> foodgenres;


    private String usename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menutype);



        //usename=getIntent().getStringExtra("UN");


        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        foodgenres = new ArrayList<>();
        adapter = new MenuTypeAdapter(this, foodgenres,usename);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        displayGenres();

    }


    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getUsename() {
        return usename;
    }

    private void displayGenres() {
        int[] covers = new int[]{
                R.drawable.breakfast,
                R.drawable.lunch,

        };

        MenuTypes a = new MenuTypes("Breakfast", covers[0]);
        foodgenres.add(a);

        a = new MenuTypes("Lunch", covers[1]);
        foodgenres.add(a);




        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column


            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
