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
            FileOutputStream outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(bitmapToByteArray(bitmap));
            outputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return new File(mContext.getFilesDir(), filename);
    }

    public File save(byte[] bytes, String filename){
        try{
            FileOutputStream outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return new File(mContext.getFilesDir(), filename);
    }

    public byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
