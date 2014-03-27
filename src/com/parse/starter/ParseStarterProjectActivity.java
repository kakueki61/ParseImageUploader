package com.parse.starter;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.*;
import org.kakueki61.image_uploader.listener.IEditTextListener;
import org.kakueki61.image_uploader.model.ParseService;
import org.kakueki61.image_uploader.utils.BitmapDecodeLoader;
import org.kakueki61.image_uploader.utils.DialogUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ParseStarterProjectActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Bitmap> {
    private static final String TAG = ParseStarterProjectActivity.class.getSimpleName();
    private static final String PARSE_OBJECT_NAME = "EditText";
    private static final String IMAGE_PARSE_OBJECT_NAME = "ImageParseObject";
    private static final String BUNDLE_BYTES_KEY = "byteArray";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ParseAnalytics.trackAppOpened(getIntent());

        findViewById(R.id.button_upload_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MediaImageGalleryActivity.class);
//                startActivity(intent);

                /*
                Cursor cursor = managedQuery(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI, null, null, null, null
                );
                cursor.moveToFirst();

                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri bmpUri = ContentUris.withAppendedId(MediaStore.Images.Media.INTERNAL_CONTENT_URI, id);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bmpUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bytes = baos.toByteArray();

                    ParseFile parseFile = new ParseFile("test_image.jpg", bytes);
                    parseFile.saveInBackground();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */

                // Try to call Gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1111);
            }
        });

        findViewById(R.id.button_fetch_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(IMAGE_PARSE_OBJECT_NAME);
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        String pass = parseObjects.get(0).getString("password");
                        ((TextView) findViewById(R.id.textView)).setText(pass);
                        ParseFile imageParseFile = (ParseFile) parseObjects.get(0).get("imageFile");
                        imageParseFile.getDataInBackground(
                            new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if(e == null) {
                                        Log.i(TAG, "byte size: " + bytes.length);
                                        Bundle bundle = new Bundle();
                                        bundle.putByteArray(BUNDLE_BYTES_KEY, bytes);
                                        getSupportLoaderManager().initLoader(0, bundle, ParseStarterProjectActivity.this);
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new ProgressCallback() {
                                @Override
                                public void done(Integer integer) {
                                    Log.i(TAG, "progress: " + integer);
                                }
                            }
                        );
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 1111 && resultCode == RESULT_OK) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.dialog_input_text, null);

            DialogUtils.showLayoutDialog(ParseStarterProjectActivity.this, dialogView, new IEditTextListener() {
                @Override
                public void onIputFinish(View view) {
                    EditText editText = (EditText) view.findViewById(R.id.edit_text);
                    String input = editText.getText().toString();

                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    ParseService imageParseService = new ParseService(IMAGE_PARSE_OBJECT_NAME);
                    imageParseService.saveData(input, bitmap);

                    Toast.makeText(getApplicationContext(), "Image Uploading...", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "onCreateLoader");
        BitmapDecodeLoader bitmapDecodeLoader
                = new BitmapDecodeLoader(getApplicationContext(), bundle.getByteArray(BUNDLE_BYTES_KEY));
        bitmapDecodeLoader.forceLoad();
        return bitmapDecodeLoader;
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> bitmapLoader, Bitmap bitmap) {
        Log.d(TAG, "onLoadFinished");
        ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> bitmapLoader) {
        //TODO
    }
}
