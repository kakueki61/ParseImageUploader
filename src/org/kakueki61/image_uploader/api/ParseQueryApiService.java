package org.kakueki61.image_uploader.api;

import android.util.Log;
import com.parse.*;
import org.kakueki61.image_uploader.listener.IRetrieveParseFileListener;

import java.util.List;

/**
 * Service class for calling Queries of Parse.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/03/28 kodama-t
 */
public class ParseQueryApiService {

    public static final String TAG = ParseQueryApiService.class.getSimpleName();

    public static void getParseFile(String parseObjectName, String key, final IRetrieveParseFileListener listener) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(parseObjectName);
        parseQuery.whereEqualTo("password", key);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ParseFile imageParseFile = (ParseFile) parseObjects.get(0).get("imageFile");
                imageParseFile.getDataInBackground(
                        new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if(e == null) {
                                    listener.onFinished(bytes);
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
}
