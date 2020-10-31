package com.tekzee.amiggos.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class GetFilePath {
  public static String getFilePath(Context ctx, Uri uri) {
    ContentResolver cr = ctx.getContentResolver();

    String file_path = null;


    // .
    Cursor cursor = cr.query(uri,
        new String[] { android.provider.MediaStore.MediaColumns.DATA },
        null, null, null);
    if (cursor != null) {
      cursor.moveToFirst();
      file_path = cursor.getString(0);
      cursor.close();
    } else {
      file_path = uri.getPath();
    }
    return file_path;
  }

}
