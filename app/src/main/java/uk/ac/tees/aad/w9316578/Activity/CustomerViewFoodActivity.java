package uk.ac.tees.aad.w9316578.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.tees.aad.w9316578.Model.Food;
import uk.ac.tees.aad.w9316578.R;
import uk.ac.tees.aad.w9316578.SqliteDatabase.DataBaseHelper;

public class CustomerViewFoodActivity extends AppCompatActivity {


    private static final int SELECT_PICTURE = 111;
    ImageView imageViewFood;
    TextView foodName, foodPrice, foodDesc;
    Button btnAddToCart, btnMinus, btnPlus;
    Toolbar toolbar;
    Food food;
    boolean isInnEditMode = false;
    ProgressDialog progressDialog;
    DatabaseReference mFoodRef;
    StorageReference mFoodStorageRef;
    int numberCount = 1;
    TextView counterTV;
    DataBaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_food);

        food = (Food) getIntent().getSerializableExtra("food");

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Food Item");

        mFoodRef = FirebaseDatabase.getInstance().getReference().child("Food");
        mFoodStorageRef = FirebaseStorage.getInstance().getReference().child("FoodImage");

        imageViewFood = findViewById(R.id.imageViewFood);
        foodName = findViewById(R.id.foodName);
        foodPrice = findViewById(R.id.foodPrice);
        foodDesc = findViewById(R.id.foodDesc);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        counterTV = findViewById(R.id.itemsCounter);

        helper = new DataBaseHelper(this);

        ElegentAssign();
        AssignData();

    }

    private void ElegentAssign() {
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberCount++;
                counterTV.setText("" + numberCount);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberCount != 1) {
                    numberCount--;
                    counterTV.setText("" + numberCount);
                }
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartItems();
            }
        });
    }

    private void addToCartItems() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);

        helper.insertData(strDate, food.getFoodId(), food.getFoodName(), food.getFoodPrice(), food.getFoodDesc(), numberCount + "", food.getFoodImageUri());
        Toast.makeText(this, "Added to Card", Toast.LENGTH_SHORT).show();

    }

    private void AssignData() {
        foodName.setText(food.getFoodName());
        foodPrice.setText("$ " + food.getFoodPrice());
        foodDesc.setText(food.getFoodDesc());
        Picasso.get().load(food.getFoodImageUri()).into(imageViewFood);

    }
}