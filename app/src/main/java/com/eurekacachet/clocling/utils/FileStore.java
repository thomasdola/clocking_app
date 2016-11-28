package com.eurekacachet.clocling.utils;


import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileStore {

    Context mContext;

    public FileStore(Context context){
        mContext = context;
    }

    public File save(Bitmap bitmap, String filename){
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            FileOutputStream outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(stream.toByteArray());
            outputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return new File(mContext.getFilesDir(), filename);
    }
}
