package ceur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Limpieza {
	
	public static void main(String[] args) {
		BufferedReader reader;
		PrintWriter writer;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader("./test.csv"));
			writer = new PrintWriter(new File("test2.csv"));
			String line = reader.readLine();
			
			while (line != null) {
				
				System.out.println(line);
				String[] temp = line.split("\\|");
				
				System.out.println(temp.length);
				if(temp.length != 6)
				{
					line = reader.readLine();
					continue;
					
				}
				 
					sb.append(line);
					sb.append('\n');
				
				line = reader.readLine();
			}
			
			writer.write(sb.toString());
			
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
