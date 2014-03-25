package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.parse.*;
import org.kakueki61.image_uploader.listener.IEditTextListener;
import org.kakueki61.image_uploader.utils.DialogUtils;

import java.util.List;

public class ParseStarterProjectActivity extends Activity {
    private static final String TAG = ParseStarterProjectActivity.class.getSimpleName();
    private static final String PARSE_OBJECT_NAME = "EditText";

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

                final ParseObject editTextObject = new ParseObject(PARSE_OBJECT_NAME);
                DialogUtils.showLayoutDialog(ParseStarterProjectActivity.this, dialogView, new IEditTextListener() {
                    @Override
                    public void onIputFinish(View view) {
                        EditText editText = (EditText) view.findViewById(R.id.edit_text);
                        String input = editText.getText().toString();
                        editTextObject.put("input", input);
                        editTextObject.saveInBackground();
                        Log.d(TAG, input);
                    }
                });
            }
        });
        findViewById(R.id.button_fetch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(PARSE_OBJECT_NAME);
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        for(int i = 0; i < parseObjects.size(); i++) {
                            Log.d(TAG, parseObjects.get(i).getObjectId());
                            Log.d(TAG, parseObjects.get(i).getCreatedAt().toString());
                            Log.d(TAG, parseObjects.get(i).getString("input"));
                        }
                    }
                });
            }
        });
    }
}
