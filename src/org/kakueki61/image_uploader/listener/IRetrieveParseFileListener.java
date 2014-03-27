package org.kakueki61.image_uploader.listener;

/**
 * Callback interface which is run after retrieving ParseFile.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/03/28 kodama-t
 */
public interface IRetrieveParseFileListener {

    public void onFinished(byte[] bytes);
}
