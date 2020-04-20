package com.example.jepapp.Adapters.Admin;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Activities.Admin.SingleItemsReport;
import com.example.jepapp.Activities.Admin.EditItemActivity;
import com.example.jepapp.Models.Cart;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class  AllitemsAdapter extends RecyclerView.Adapter<AllitemsAdapter.AllitemsViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the menu items in a list
    private List<MItems> MenuItemList;
    private static DatabaseReference databaseReference;

    //used to set the adapter position to null
    private static int currentPosition = -1;

    private String person;


    //getting the context and item list with constructor
    public AllitemsAdapter(Context mCtx, List<MItems> MenuItemList) {
        this.mCtx = mCtx;
        this.MenuItemList = MenuItemList;

    }

    public AllitemsAdapter(Context applicationContext, List<MItems> foodItemList, String admin) {
        this.mCtx = applicationContext;
        this.MenuItemList = foodItemList;
        this.person = admin;
    }



    @NotNull
    @Override
    public AllitemsViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.all_menu_items_recylayout, null);
        AllitemsViewHolder holder = new AllitemsViewHolder(view);
        //calling the firebase nodes
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");
        MultiDex.install(mCtx);
        //return view holder
        return holder;
    }


    @Override
    public void onBindViewHolder(final AllitemsAdapter.AllitemsViewHolder holder, final int position) {
        //getting the item of the specified position

        final MItems item = MenuItemList.get(position);

        //binding the data with the viewholder views
        holder.Title.setText(item.getTitle());
        Picasso.with(mCtx)
                .load(item.getImage())
                .transform(new AllitemsViewHolder.CircleTransform()).into(holder.itempics);
        holder.buttonslinearlayout.setVisibility(View.GONE);
        holder.addcartlayout.setVisibility(View.GONE);
        holder.addbreakfast.setVisibility(View.GONE);
        holder.addlunch.setVisibility(View.GONE);

        //check if the second constructor was used and a person item was passed
        if (person != null) {
            //check the contents of the person parameter that was passed and animate the corresponding linear layout
            if ( person.equals("Report")) {
                if (currentPosition == position) {
                    //creating an animation
                    Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

                    //toggling visibility
                    holder.reportlayout.setVisibility(View.VISIBLE);

                    //adding sliding effect
                    holder.reportlayout.startAnimation(slideDown);
                } else if (currentPosition == -1) {
                    Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
                    holder.reportlayout.setVisibility(View.GONE);

                    //adding sliding effect
                    holder.reportlayout.startAnimation(slideUp);

                }
            }else if ( person.equals("Menu")){
                holder.Prices.setText(String.valueOf(item.getIngredients()));
                holder.Prices.setTextSize(15);


            if (currentPosition == position) {
                //creating an animation
                Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

                //toggling visibility
                holder.addcartlayout.setVisibility(View.VISIBLE);
                holder.addcart.setVisibility(View.GONE);
                holder.addlunch.setVisibility(View.VISIBLE);
                holder.addbreakfast.setVisibility(View.VISIBLE);

                //adding sliding effect
                holder.addcartlayout.startAnimation(slideDown);
            } else if (currentPosition == -1) {
                Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
                holder.addcartlayout.setVisibility(View.GONE);

                //adding sliding effect
                holder.addcartlayout.startAnimation(slideUp);

            }


        } else {
                holder.Prices.setText(String.valueOf(item.getPrice()));
                if (currentPosition == position) {
                    //creating an animation
                    Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

                    //toggling visibility
                    holder.addcartlayout.setVisibility(View.VISIBLE);

                    //adding sliding effect
                    holder.addcartlayout.startAnimation(slideDown);
                } else if (currentPosition == -1) {
                    Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
                    holder.addcartlayout.setVisibility(View.GONE);

                    //adding sliding effect
                    holder.addcartlayout.startAnimation(slideUp);
                }
            }
        }
        //if no person parameter was passes
        else {
            holder.Prices.setText(String.valueOf(item.getPrice()));
            // using holder position to display/hide buttons on holder
            if (currentPosition == position) {
                //creating an animation
                Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

                //toggling visibility
                holder.buttonslinearlayout.setVisibility(View.VISIBLE);

                //adding sliding effect
                holder.buttonslinearlayout.startAnimation(slideDown);
            } else if (currentPosition == -1) {
                Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
                holder.buttonslinearlayout.setVisibility(View.GONE);

                //adding sliding effect
                holder.buttonslinearlayout.startAnimation(slideUp);

            }
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                if (currentPosition == position) {
                    currentPosition = -1;

                } else {
                    currentPosition = position;
                }

                //reloading the list
                notifyItemChanged(position);
            }

        });


        //delete function for holder item
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating alert dialog to confirm/cancel item deletion
                AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(mCtx,R.style.datepicker);
                alertDialogDelete.setTitle("Delete Item");
                alertDialogDelete.setMessage("Do you want to delete " + item.getTitle()+ " ?");
                alertDialogDelete.setPositiveButton(R.string.dialogYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteItem(item);

                            }
                        });

                alertDialogDelete.setNegativeButton(R.string.dialogNo,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialogDelete = alertDialogDelete.create();
                dialogDelete.show();

            }
        });

        //editing an item on the holder
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(item);
                notifyItemChanged(position);
            }
        });
        //Function to increment the desired quantity by 1
        holder.plusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldvalue = holder.addquantity.getText().toString();
                String newvalue = String.valueOf((Integer.valueOf(oldvalue)+1));
                holder.addquantity.setText(newvalue);
            }
        });
        //Function to decrement the desired quantity by 1
        holder.minusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldvalue = holder.addquantity.getText().toString();
                String newvalue = String.valueOf((Integer.valueOf(oldvalue)-1));
                holder.addquantity.setText(newvalue);
            }
        });
        //Launches Report for holder item clicked
        holder.generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, SingleItemsReport.class);
                intent.putExtra("name", item.getTitle());
                mCtx.startActivity(intent);
            }
        });

        //adds holder item to Breakfast Cart. This is used when admin is updating a user's order
        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Statement to check if the value enter is less than or equal to zero or exceeds the quantity
                if ((Integer.valueOf(holder.addquantity.getText().toString()) <= 0)) {
                    Toast.makeText(mCtx, "Please correct the value entered", Toast.LENGTH_SHORT).show();
                }
                //adds item to database
                else{
                    String type = "Breakfast";
                    String username = "Admin";
                    com.example.jepapp.Models.Cart cart = new Cart(item.getPrice().toString(), item.getImage(), item.getTitle(), holder.addquantity.getText().toString(), type, username);
                    FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastCart")
                            .child(username)
                            .child(item.getTitle())
                            .setValue(cart);
                    notifyItemChanged(position);

                    ((Activity) mCtx).finish();


                }
            }

        });
        //Function to add holder item to Breakfast Cart
        holder.addbreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Statement to check if the value enter is less than or equal to zero or exceeds the quantity
                if ((Integer.valueOf(holder.addquantity.getText().toString()) <= 0)) {
                    Toast.makeText(mCtx, "Please correct the value entered", Toast.LENGTH_SHORT).show();
                }
                else{
                    String type = "Breakfast";
                    String username = "Admin Menu";
                    com.example.jepapp.Models.Cart cart = new Cart(item.getPrice().toString(), item.getImage(), item.getTitle(), holder.addquantity.getText().toString(), type, username,item.getIngredients(),item.getId());
                    FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastCart")
                            .child(username)
                            .child(item.getTitle())
                            .setValue(cart);
                    Toast.makeText(mCtx, "You may check the breakfast list by clicking on the cart icon above", Toast.LENGTH_LONG).show();



                }
            }

        });
        //Function to add holder item to Lunch Cart
        holder.addlunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Statement to check if the value enter is less than or equal to zero or exceeds the quantity
                if ((Integer.valueOf(holder.addquantity.getText().toString()) <= 0)) {
                    Toast.makeText(mCtx, "Please correct the value entered", Toast.LENGTH_SHORT).show();
                }
                else{
                    String type = "Lunch";
                    String username = "Admin Menu";
                    com.example.jepapp.Models.Cart cart = new Cart(item.getPrice().toString(), item.getImage(), item.getTitle(), holder.addquantity.getText().toString(), type, username, item.getIngredients(),item.getId());
                    FirebaseDatabase.getInstance().getReference("JEP").child("LunchCart")
                            .child(username)
                            .child(item.getTitle())
                            .setValue(cart);
                    Toast.makeText(mCtx, "You may check the lunch list by clicking on the cart icon above", Toast.LENGTH_LONG).show();



                }
            }

        });
    }

    private void deleteItem(MItems item) {
        //deletes item from database
        databaseReference.child(item.getKey()).removeValue();
    }

    //launches interface that allows editing of item
    private void editItem(MItems item) {
        String key = item.getKey();
        String  title = item.getTitle();
        String  ingredients = item.getIngredients();
        String  image = item.getImage();
        String price = String.valueOf(item.getPrice());
        //sends information to intent class
        Intent intent = new Intent(mCtx, EditItemActivity.class);
        intent.putExtra("key", String.valueOf(key));
        intent.putExtra("title", title);
        intent.putExtra("ingredients", ingredients);
        intent.putExtra("price", price);
        intent.putExtra("image", image);

        mCtx.startActivity(intent);

    }


    @Override
    //determines the number of items to be reflected in the recycler view
    public int getItemCount() {
        return MenuItemList.size();
    }

    public void updateList(List<MItems> newreviewList) {
        MenuItemList = newreviewList;
        notifyDataSetChanged();
    }



    static class AllitemsViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Prices, Imageurl;
        ImageView itempics;
        EditText addquantity;
        Button edit, delete, addcart,plusquantity,minusquantity, generate_report, addbreakfast, addlunch;
        LinearLayout parentLayout, buttonslinearlayout, addcartlayout, reportlayout;

        AllitemsViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.itemtitle);
            itempics = itemView.findViewById(R.id.itempic);
            Prices = itemView.findViewById(R.id.prices);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            Imageurl = itemView.findViewById(R.id.imageurl);
            parentLayout = itemView.findViewById(R.id.allitemslinearLayout);
            buttonslinearlayout = itemView.findViewById(R.id.buttonslinearLayout);
            addcartlayout = itemView.findViewById(R.id.addcartlayout);
            addcart = itemView.findViewById(R.id.adminaddtocart);
            addquantity = itemView.findViewById(R.id.adminaddquantity);
            plusquantity = itemView.findViewById(R.id.adminplusquantity);
            addbreakfast = itemView.findViewById(R.id.adminaddtobreakfast);
            addlunch = itemView.findViewById(R.id.adminaddtolunch);
            minusquantity = itemView.findViewById(R.id.adminminusquantity);
            generate_report = itemView.findViewById(R.id.generate_report);
            reportlayout = itemView.findViewById(R.id.generateReportLayout);

        }

        //gives image a circular shape
        public static class CircleTransform implements Transformation {
            @Override
            public Bitmap transform(Bitmap source) {
                int size = Math.min(source.getWidth(), source.getHeight());

                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }

                Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);

                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "circle";
            }
        }
    }
}
