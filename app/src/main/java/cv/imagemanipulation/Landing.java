package cv.imagemanipulation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Landing extends AppCompatActivity {

    Button fileButton;
    Button undoButton;
    Button processButton;
    Button aboutButton;
    Dialog fileDialogue;
    ListView lView;
    File currentFile;
    File mCurrentPhoto;
    String picturePath = new String("");
    ImageView iv;
    private final int RESULT_LOAD_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        iv = (ImageView) findViewById(R.id.imageView);
        fileButton = (Button)findViewById(R.id.btnFile);
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Landing.this, fileButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.file_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        /*Toast.makeText(
                                Landing.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();*/

                        if(item.getTitle().equals("Open"))
                        {
                            fileOpen();
                        }
                        return true;
                    }
                });
                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
    }

    protected void fileOpen()
    {
        /*Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageFunctions imgFun = new ImageFunctions();
        switch (requestCode)
        {
            case RESULT_LOAD_IMAGE:
            {
                if (resultCode == RESULT_OK && null != data)
                {
                    Uri selectedImage = data.getData();
                    try
                    {
                        imgFun.ActiveImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        imgFun.BackupImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);

                        if (cursor == null || cursor.getCount() < 1)
                        {
                            mCurrentPhoto = null;
                            return; // no cursor or no record. DO YOUR ERROR HANDLING
                        }

                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                        if (columnIndex < 0)
                        {// no column index
                            mCurrentPhoto = null;
                            return; // DO YOUR ERROR HANDLING
                        }

                        picturePath = cursor.getString(columnIndex);
                        mCurrentPhoto = new File(cursor.getString(columnIndex));

                        cursor.close(); // close cursor

                        imgFun.createPixels(imgFun.ActiveImage);
                        //imgFun.brighten(-255);

                    }
                    catch(Exception e)
                    {
                        Toast.makeText(
                                Landing.this,
                                "Selected Image can't be loaded.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
                break;
            }


        }
        //Picasso.with(App.Instance).load(mCurrentPhoto).into(imageView);
        System.out.println("Loading Image from Path" + picturePath);
        //iv.setImageURI(Uri.parse(picturePath));
        Bitmap setImage = imgFun.brighten(-255);
        iv.setImageBitmap(setImage);
        System.out.println("Loaded Image from Path" + picturePath);
        //iv.refreshDrawableState();

    }

}
