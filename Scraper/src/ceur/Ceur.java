package ceur;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.SearchException;
import com.jaunt.UserAgent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Ceur {
	
	public static void main(String[] args) throws SearchException, JauntException {
		
		for(int pos = 2316; pos >= 2310; pos--)
		{
			switch (pos) {
			case 2312:
				continue;
			case 2308:
				continue;
			}
			
			try{
				  UserAgent userAgent = new UserAgent();                
				  userAgent.visit("http://ceur-ws.org/Vol-"+ pos + "/");                   
				  Element titulo = userAgent.doc.findFirst("<span class = \"CEURVOLTITLE\">");
				  System.out.println(titulo.getChildText());
				  Element fecha = userAgent.doc.findFirst("<span class = \"CEURLOCTIME\">"); 
				  System.out.println(fecha.getChildText());

				  Elements lista = userAgent.doc.findEvery("<li id>"); 
				  
				  for(Element ele : lista){
					  
					  if(ele.findEvery("<span class=\"CEURTITLE\">").size() == 0)
					  {
						  continue;
					  }
					  
					    System.out.println(ele.findFirst("<span class=\"CEURTITLE\">").getChildText());  
					    Elements autores = ele.findEach("<span class=\"CEURAUTHOR\">"); 
					    
					    for(Element aut : autores){                            
						    System.out.println(aut.getChildText());    
					  }   
				  }
				  
				}
			
				catch(JauntException e){       
					
				  System.err.println(e);
			}
			
			try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {

			      StringBuilder sb = new StringBuilder();
			      sb.append("id,");
			      sb.append(',');
			      sb.append("Name");
			      sb.append('\n');

			      sb.append("1");
			      sb.append(',');
			      sb.append("Prashant Ghimire");
			      sb.append('\n');

			      writer.write(sb.toString());

			      System.out.println("done!");

			    } catch (FileNotFoundException e) {
			      System.out.println(e.getMessage());
			    }
	
		}
	}
}
