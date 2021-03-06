package ca.cmpt276.ParentApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ca.cmpt276.ParentApp.model.Child;

/*
    Queue Activity:
    -   Takes in a Child list and display on screen along with their photos.
    -   Allow user to choose a child and save, which takes user back to screen before.
 */

public class Picker_Queue_Activity extends AppCompatActivity {

    ArrayList<Child> children_list;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_queue);
        setup_back_button();

        setup_getIntentData();

        setup_queue_listView();
        setup_save_button();
    }

    private void setup_getIntentData() {
        children_list = getIntent().getParcelableArrayListExtra("CHILDREN_LIST");
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.choose_picker_back_button);
        back_button.setOnClickListener(view -> Picker_Queue_Activity.super.onBackPressed());
    }
    private void setup_queue_listView() {
        ArrayAdapter<Child> adapter = new Picker_Queue_Adapter();
        ListView list = findViewById(R.id.choose_picker_list);
        list.setAdapter(adapter);
    }

    private class Picker_Queue_Adapter extends ArrayAdapter<Child> {
        public Picker_Queue_Adapter() {
            super(Picker_Queue_Activity.this,
                    R.layout.queue_item,
                    children_list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with (could be null)
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.queue_item,parent,false);
            }
            //populate the list
            //get current child
            Child current_child = children_list.get(position);

            //fill view
            CardView cardView = itemView.findViewById(R.id.picker_card_view);
            int width = cardView.getLayoutParams().width;
            cardView.setRadius((float) width/2);

            ImageView imageView = itemView.findViewById(R.id.queue_profile);
            if(current_child.getImagePath().equals("Default pic")){
                imageView.setImageResource(R.drawable.default_profile);
            } else {
                loadImageFromStorage(current_child.getImagePath(),current_child.getName(),imageView);
            }

            TextView nameView = itemView.findViewById(R.id.queue_name);
            nameView.setText(children_list.get(position).getName());

            itemView.setOnClickListener(view -> {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(getColor(R.color.white));
                }
                getWindow().getDecorView().setBackgroundColor(getColor(R.color.white));
                parent.getChildAt(position).setBackgroundResource(R.drawable.coin_flip_queue_selected_background);
                index = position;
            });

            return itemView;
        }
    }


    private void setReturn_Result(int index){
        Intent intent = new Intent();
        intent.putExtra("CHILD_INDEX",index);
        setResult(RESULT_OK,intent);
    }


    private void setup_save_button() {
        Button save = findViewById(R.id.queue_save);
        save.setOnClickListener(view -> {
            if(index == -1){
                Snackbar.make(view,"Choose a child before save",Snackbar.LENGTH_LONG).show();
            } else {
                setReturn_Result(index);
                finish();
            }
        });
    }

    //pass in filename (which is child's name) and the image path to load image.
    private void loadImageFromStorage(String path, String filename, ImageView imageView)
    {

        try {
            File f=new File(path, filename + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


}