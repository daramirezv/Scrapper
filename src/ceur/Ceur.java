package ceur;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.SearchException;
import com.jaunt.UserAgent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Ceur {

	public static void main(String[] args) throws SearchException, JauntException {

		try (PrintWriter writer = new PrintWriter(new File("sucio.csv"))) {

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

			for (int pos = 2316; pos >= 559; pos--) {
				switch (pos) {
				case 2312:
					continue;
				case 2308:
					continue;
				}

				try {
					UserAgent userAgent = new UserAgent();
					userAgent.visit("http://ceur-ws.org/Vol-" + pos + "/");
					Element titulo = userAgent.doc.findFirst("<span class = \"CEURVOLTITLE\">");

					Element fecha = userAgent.doc.findFirst("<span class = \"CEURLOCTIME\">");

					Elements lista = userAgent.doc.findEvery("<li id>");

					for (Element ele : lista) {

						if (ele.findEvery("<span class=\"CEURTITLE\">").size() == 0) {
							continue;
						}

						sb.append(titulo.getChildText());
						sb.append('|');

						String time = fecha.getChildText();
						if (time.equals(", ")) {
							String lugar = fecha.findFirst("<span rel= \"event:place\">").getChildText();
							String[] lugarArray = lugar.split(",");
							if (lugarArray.length != 2) {
								continue;
							}
							sb.append(lugarArray[0]);
							sb.append('|');
							sb.append(lugarArray[1]);
							sb.append('|');

							String date = fecha.findFirst("<span property= \"dcterms:date\">").getChildText();
							String[] listaDate = date.split(",");
							if (listaDate.length != 2) {
								continue;
							}
							sb.append(listaDate[0] + " " + listaDate[1]);
							sb.append('|');

						} else {
							String[] arreglo = time.split(",");

							if (arreglo.length != 4) {
								continue;
							}
							sb.append(arreglo[0]);
							sb.append('|');
							sb.append(arreglo[1]);
							sb.append('|');
							sb.append(arreglo[2] + " " + arreglo[3]);
							sb.append('|');

							sb.append(ele.findFirst("<span class=\"CEURTITLE\">").getChildText());
							sb.append('|');
						}

						Elements autores = ele.findEach("<span class=\"CEURAUTHOR\">");

						if (autores.size() == 0) {
							Element auts = ele.findFirst("<span class=\"CEURAUTHORS\">");
							String respuestaAutores = auts.getChildText();
							sb.append(respuestaAutores);
							sb.append('\n');
						} else {
							String respuestaAutores = "";
							for (Element aut : autores) {
								respuestaAutores += aut.getChildText();
								respuestaAutores += ", ";
							}
							int tam = respuestaAutores.length();
							String real = respuestaAutores.substring(0, tam - 2);
							sb.append(real);
							sb.append('\n');
						}
					}

				}

				catch (JauntException e) {

					System.err.println(e);
				}

				Thread.sleep(20);
			}

			writer.write(sb.toString());

			System.out.println("terminado de escribir");

		} catch (FileNotFoundException e1) {
			System.err.println(e1);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		
		// limpieza

		BufferedReader reader;
		PrintWriter writer;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader("./sucio.csv"));
			writer = new PrintWriter(new File("limpio.csv"));
			String line = reader.readLine();

			while (line != null) {

				String[] temp = line.split("\\|");

				if (temp.length != 6) {
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
		
		BufferedReader readerAutores;
		PrintWriter writerAutores;
		StringBuilder sbAutores = new StringBuilder();
		try {
			readerAutores = new BufferedReader(new FileReader("./limpio.csv"));
			writerAutores = new PrintWriter(new File("limpioAutores.csv"));
			String line = readerAutores.readLine();

			while (line != null) {

				String[] temp = line.split("\\|");

				String[] diferentesAutores = temp[temp.length-1].split(", ");
				
				for(String au: diferentesAutores)
				{
					String respuesta = temp[0] + "|" + temp[1] + "|" + temp[2] + "|" + temp[3] + "|" + temp[4] + "|" + au;
					sbAutores.append(respuesta);
					sbAutores.append('\n');
				}

				line = readerAutores.readLine();
			}

			writerAutores.write(sbAutores.toString());
			readerAutores.close();
			writerAutores.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
