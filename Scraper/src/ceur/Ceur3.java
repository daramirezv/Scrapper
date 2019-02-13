package ceur;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.SearchException;
import com.jaunt.UserAgent;

public class Ceur3 {
	
	public static void main(String[] args) throws SearchException, JauntException {
		
		
		for(int pos = 2316; pos < 0; pos--)
		{
			//http://ceur-ws.org/Vol-1/
			
		}
		
		try{
			  UserAgent userAgent = new UserAgent();                
			  userAgent.visit("http://ceur-ws.org");                   

			  Elements lista = userAgent.doc.findEvery("<tr>"); 
			  
			  for(Element ele : lista){
				  
				  if(ele.findEvery("<td>").size() == 0)
				  {
					  continue;
				  }
				  
				  if(ele.findEvery("<td align>").size() != 0 && ele.findEvery("<a name>").size() != 0)
				  {
					  System.out.println(ele.findFirst("<a name>").getChildText());  
				  }
				  else
				  {
					  System.out.println("wtf");
					  Element x = ele.getElement(1);
					  System.out.println(x.getChildText());
					  System.out.println("____________");
				  }
			  }
			  
			}
		
			catch(JauntException e){       
				
			  System.err.println(e);
		}

	}
}
