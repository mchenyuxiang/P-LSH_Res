package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Init {
	public static void InitArray(Integer[][] base,int hang,int lie,int init){
		for(int i = 0; i < 943; i++){
			for(int j = 0; j < 1682; j++){
					base[i][j] = init;

			}
		}
	}
}
