package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import uk.ac.tees.aad.w9316578.Fragments.BottomSheetAddFoodFragment;
import uk.ac.tees.aad.w9316578.R;

public class AdminHomeActivity extends AppCompatActivity {
    FloatingActionButton fab;
    public static BottomSheetAddFoodFragment btsf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        fab  = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 btsf=new BottomSheetAddFoodFragment();
                btsf.show(getSupportFragmentManager(),btsf.getTag());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addFood) {

        }
        return super.onOptionsItemSelected(item);
    }
}