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

	public static int isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			try {
				double f = Double.parseDouble(str.substring(0, 4));
				return 1;
			} catch (NumberFormatException fe) {
				return -1;
			}
		}
		return 2;
	}

	public static void main(String[] args) throws SearchException, JauntException {

		// Se inicializa el stream de datos que va a escribir el primer CSV.
		try (PrintWriter writer = new PrintWriter(new File("sucio.csv"))) {

			StringBuilder sb = new StringBuilder();

			// Estas son las columnas que se van a tomar del HTML. Vamos a dividir cada
			// columna con un "|".
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

			// Hay unas conferencias que no se encuentran en la página, por lo que se van a
			// ignorar.
			// Además, las conferencias muy viejas tienen formatos sin ningún orden.
			// Decidimos ignorarlas también.
			for (int pos = 2316; pos >= 559; pos--) {
				switch (pos) {
				case 2312:
					continue;
				case 2308:
					continue;
				}

				try {
					// Lo primero que se realiza es visitar la página que queremos descargar.
					UserAgent userAgent = new UserAgent();
					userAgent.visit("http://ceur-ws.org/Vol-" + pos + "/");

					// tomamos el título.
					Element titulo = userAgent.doc.findFirst("<span class = \"CEURVOLTITLE\">");

					// tomamos la fecha.
					Element fecha = userAgent.doc.findFirst("<span class = \"CEURLOCTIME\">");

					// tomamos todos los papers de esta conferencia.
					Elements lista = userAgent.doc.findEvery("<li id>");

					for (Element ele : lista) {

						// buscamos para cada paper, el título.
						if (ele.findEvery("<span class=\"CEURTITLE\">").size() == 0) {
							continue;
						}

						sb.append(titulo.getChildText());
						sb.append('|');

						String time = fecha.getChildText();

						// Este condicional está porque hay unas conferencias con otro formato.
						if (time.equals(", ")) {
							String lugar = fecha.findFirst("<span rel= \"event:place\">").getChildText();
							String[] lugarArray = lugar.split(",");
							if (lugarArray.length != 2) {
								continue;
							}
							// Se empieza a escribir los datos de la publicación en el StringBuilder.
							sb.append(lugarArray[0]);
							sb.append('|');
							sb.append(lugarArray[1].substring(1));
							sb.append('|');

							String date = fecha.findFirst("<span property= \"dcterms:date\">").getChildText();
							String[] listaDate = date.split(",");
							if (listaDate.length != 2) {
								continue;
							}
							sb.append(listaDate[1].substring(1));
							sb.append('|');

							// Este condicional es para el formato de HTML más común de la página.
						} else {
							String[] arreglo = time.split(",");

							if (arreglo.length != 4) {
								continue;
							}
							// Se empieza a escribir los datos de la publicación en el StringBuilder.
							sb.append(arreglo[0]);
							sb.append('|');
							sb.append(arreglo[1].substring(1));
							sb.append('|');
							sb.append(arreglo[3].substring(1));
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

				// Esto es para acceder a la página cada 250 milisegundos.
				Thread.sleep(20);
			}

			// Se escribe toda la información en el CSV.
			writer.write(sb.toString());

			System.out.println("terminado de escribir");

		} catch (FileNotFoundException e1) {
			System.err.println(e1);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		// Esta es la primera parte de la limpieza, en donde vamos a tomar las filas y
		// vamos a eliminar
		// las que no tienen el número de campos necesarios.
		// Si un campo tiene algo diferente a un integer en el campo de año, también lo
		// eliminamos.
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

				if (Ceur.isNumeric(temp[3]) == -1) {
					line = reader.readLine();
					continue;
				} else if (Ceur.isNumeric(temp[3]) == 1) {
					String real = temp[0] + "|" + temp[1] + "|" + temp[2] + "|" + temp[3].substring(0, 4) + "|"
							+ temp[4] + "|" + temp[5];
					sb.append(real);
					sb.append('\n');
				} else {
					sb.append(line);
					sb.append('\n');
				}

				line = reader.readLine();
			}

			writer.write(sb.toString());

			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Esta parte va a leer el CSV anterior y va a separar cada fila por autores.

		BufferedReader readerAutores;
		PrintWriter writerAutores;
		StringBuilder sbAutores = new StringBuilder();
		try {
			readerAutores = new BufferedReader(new FileReader("./limpio.csv"));
			writerAutores = new PrintWriter(new File("limpioAutores.csv"));
			String line = readerAutores.readLine();

			while (line != null) {

				String[] temp = line.split("\\|");

				String[] diferentesAutores = temp[temp.length - 1].split(", ");

				for (String au : diferentesAutores) {
					String respuesta = temp[0] + "|" + temp[1] + "|" + temp[2] + "|" + temp[3] + "|" + temp[4] + "|"
							+ au;
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
