package org.kakueki61.image_uploader.model;

import android.graphics.Bitmap;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

/**
 * Service class for manipulate Parse.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/03/27 kodama-t
 */
public class ParseService {

    private String parseClassName;
    private ParseObject parseObject;

    public ParseService(String parseClassName) {
        this.parseClassName = parseClassName;
        this.parseObject = new ParseObject(parseClassName);
    }

    public ParseObject getParseObject() {
        return this.parseObject;
    }

    public void saveData(String key, String value) {
        this.parseObject.put(key, value);
        this.parseObject.saveInBackground();
    }

    public void saveData(String pass, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        ParseFile parseFile = new ParseFile("image.jpg", bytes);
        parseFile.saveInBackground();

        this.parseObject.put("password", pass);
        this.parseObject.put("imageFile", parseFile);
        this.parseObject.put("imageSize", bytes.length);
        this.parseObject.saveInBackground();
    }
}
