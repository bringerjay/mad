package edu.neu.madcourse.numad20s_qizhou.data;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class Storage {

    private Context context;

    public Storage(Context context) {
        this.context =context;
    }

    public void store(Object object, String fileName, Directory directoryName) {
        try {
            File directory = context.getDir(directoryName.getPathName(), Context.MODE_PRIVATE);
            //File directory = new File(context.getFilesDir(), directoryName.getPathName());
            File file = new File(directory, fileName);

            FileOutputStream fileOut =
                    new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in "+file.getPath());
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public Object retrieve(String fileName, Directory directoryName) {
        Object localObject = null;

        try {
            File directory = context.getDir(directoryName.getPathName(), Context.MODE_PRIVATE);
            //File directory = new File(context.getFilesDir(), directoryName.getPathName());
            File file = new File(directory, fileName);
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            localObject = in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println(" class not found");
            c.printStackTrace();
        }
        return localObject;
    }

    public enum Directory{
        Documents,
        Cache;

        public String getPathName() {
            switch (this){
                case Documents: return "documents";
                case Cache: return "cache";
                default: return "documents";
            }
        }
    }

}
