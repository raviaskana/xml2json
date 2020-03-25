package org.developer.util;

import java.io.File;
import java.util.Arrays;

public class Utility {

    public static String getTempFolder(){
        File tempFolder = new File(System.getProperty("user.dir"),String.valueOf(System.currentTimeMillis()));
        tempFolder.mkdir();
        return tempFolder.getAbsolutePath();
    }
    public static void deleteFolder(String folder){
        File fileFolder = new File(folder);
        if(fileFolder.exists() && fileFolder.isDirectory()){
            Arrays.stream(fileFolder.listFiles()).forEach(file -> {
                if(file.isDirectory())
                    deleteFolder(folder);
                else{
                    file.delete();
                }
            });
        }else{
            fileFolder.delete();
        }
    }
}
