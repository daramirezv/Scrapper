package ceur;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.SearchException;
import com.jaunt.UserAgent;

public class Ceur2 {
	
	public static void main(String[] args) throws SearchException, JauntException {
		
		
		for(int pos = 559; pos > 0; pos--)
		{
			try{
				  UserAgent userAgent = new UserAgent();                
				  userAgent.visit("http://ceur-ws.org/Vol-"+pos+"/");                   
				  Element titulo = userAgent.doc.findFirst("<h1>");
				  System.out.println(titulo.getChildText());
				  Element fecha = userAgent.doc.findFirst("<h3>"); 
				  System.out.println(fecha.getChildText());

				  System.out.println("___");
				  
				  Elements lista = userAgent.doc.findEvery("<li>"); 
				  
				  for(Element ele : lista){
					  
					  if(ele.findEvery("<a href>").size() == 0)
					  {
						  continue;
					  }
					  
					    System.out.println(ele.findFirst("<a href>").getChildText());  
					    Elements autores = ele.findEach("<i>"); 
					    
					    for(Element aut : autores){                            
						    System.out.println(aut.getChildText());    
					  }
					    
					    System.out.println("___");
				  }
				  
				}
			
				catch(JauntException e){       
					
				  System.err.println(e);
			}
			
		}
	}
}
