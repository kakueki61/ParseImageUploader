package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import org.kakueki61.image_uploader.listener.IEditTextListener;
import org.kakueki61.image_uploader.utils.DialogUtils;

public class ParseStarterProjectActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ParseAnalytics.trackAppOpened(getIntent());

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        findViewById(R.id.button_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.dialog_input_text, null);
                DialogUtils.showLayoutDialog(ParseStarterProjectActivity.this, dialogView, new IEditTextListener() {
                    @Override
                    public void onIputFinish(View view) {
                        EditText editText = (EditText) view.findViewById(R.id.edit_text);
                        String input = editText.getText().toString();
                        Log.d("ImageUploader", input);
                    }
                });
            }
        });
        findViewById(R.id.button_fetch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
