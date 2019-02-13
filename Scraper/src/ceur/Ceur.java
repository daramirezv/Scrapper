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
		
		try (PrintWriter writer = new PrintWriter(new File("test.csv"))){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Conferencia");
	    sb.append('|');
	    sb.append("Ciudad");
	    sb.append('|');
	    sb.append("Pais");
	    sb.append('|');
	    sb.append("Fecha");
	    sb.append('|');
	    sb.append("Paper");
	    sb.append('|');
	    sb.append("Autores");
	    sb.append('\n');

	    //mal:2313 y 2297
		for(int pos = 2313; pos >= 2313; pos--)
		{
			switch (pos) {
			case 2312:
				continue;
			case 2308:
				continue;
			}
			
			try {
				  UserAgent userAgent = new UserAgent();                
				  userAgent.visit("http://ceur-ws.org/Vol-"+ pos + "/");                   
				  Element titulo = userAgent.doc.findFirst("<span class = \"CEURVOLTITLE\">");
				  System.out.println(titulo.getChildText());
				  
				  
				  //System.out.println(titulo.getChildText());
				  Element fecha = userAgent.doc.findFirst("<span class = \"CEURLOCTIME\">"); 
				  //System.out.println(fecha.getChildText());
				  
				  
				  
				  Elements lista = userAgent.doc.findEvery("<li id>"); 
				  
				  for(Element ele : lista){
					  
					  if(ele.findEvery("<span class=\"CEURTITLE\">").size() == 0)
					  {
						  continue;
					  }
					  
					  sb.append(titulo.getChildText());
					  sb.append('|');
					  
					    String time = fecha.getChildText();
					    
					    if(time.equals(","))
					    {
					    	String lugar = fecha.findFirst("<span rel= \"event:place\">").getChildText();
//					    	String[] lugarArray
					    	
					    	fecha.findFirst("<span rel= \"event:place\">");
					    }
					    System.out.println(time);
						String[] arreglo = time.split(",");
						  
						  sb.append(arreglo[0]);
						  sb.append('|');
						  sb.append(arreglo[1]);
						  sb.append('|');
						  sb.append(arreglo[2] + " " + arreglo[3]);
						  sb.append('|');
						  
						  sb.append(ele.findFirst("<span class=\"CEURTITLE\">").getChildText());
						  sb.append('|');
						  
					    //System.out.println(ele.findFirst("<span class=\"CEURTITLE\">").getChildText());  
					    Elements autores = ele.findEach("<span class=\"CEURAUTHOR\">"); 
					    
					    String respuestaAutores = "";
					    for(Element aut : autores)
					    {   
					    	respuestaAutores += aut.getChildText();
					    	respuestaAutores += "-";
						    //System.out.println(aut.getChildText());    
					    }
					    sb.append(respuestaAutores);
					    sb.append('\n');
				  }
				  
				}
				
				catch(JauntException e){       
					
				  System.err.println(e);
				}
			
			
		}
		
		writer.write(sb.toString());
		
		System.out.println("terminado");
	} catch (FileNotFoundException e1) {
		System.err.println(e1);
	}
	}
}
