package hust.hgbk.vtio.vinafood.media;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class FileOperartion {
	private String dataFolderPath = "";
	public static boolean hasSDCard() {
		File root = Environment.getExternalStorageDirectory();
		return (root.exists() && root.canWrite());
	}
	public abstract String getPackagePath();
	public String getDataPath() {
		if (dataFolderPath.length() > 0){
			return dataFolderPath;
		}
		if (hasSDCard()) {
			dataFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/data/"+ getPackagePath() +"/files/";
		} else {
			dataFolderPath = "/data/data/"+ getPackagePath() +"/files/";
		}
		File file = new File(dataFolderPath);
		file.mkdirs();
		return dataFolderPath;
	}
	public ArrayList<String> getStringArray(File aFile) {
        ArrayList<String> line = new ArrayList<String>();
        try {
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try {
                int i = 0;
                String temp = input.readLine();
                while (temp != null) {
                    line.add(temp);
                    temp = input.readLine();
                    i++;
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return line;
    }
	
}
