
import src.org.apache.html.dom.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ParkingRipperPaginasAmarillas {

	private static String html;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		java.lang.String jsonResponse = getJSONData("http://v1.ws.instapark.es/index.php?m=listallparkings&f=json");
//		System.out.println(jsonResponse + "\n\n");
		
//		JSONObject jsonObj = new JSONObject();
//		jsonObj.put("name",	"Alex");
//		
//		JSONObject jsonObj2 = null;
//		try {
//			jsonObj2 = new JSONObject(jsonResponse);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////	
//		System.out.println(jsonObj2.toString());

		
	    html = getUrl("http://www.paginasamarillas.es/search/all-ac/all-ma/all-pr/all-is/all-ci/all-ba/all-pu/all-nc/1?cu=aparcamientos&ub=false");
	    String numpagestag = "<!-- Inicio Tittle -->";
	    int startnumpages = html.indexOf(numpagestag) + numpagestag.length();
	    numpagestag = "(";
	    startnumpages = html.indexOf(numpagestag, startnumpages) + numpagestag.length();
	    String numpagesendtag = ")";
	    int numpages = Integer.parseInt(html.substring(startnumpages, html.indexOf(numpagesendtag, startnumpages)))/15;
	    System.out.println(numpages);
	    FileWriter outFile = null;
		try {
			outFile = new FileWriter("output.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    PrintWriter fileOut = new PrintWriter(outFile);
	    
	    for (int i = 1; i <= numpages; i++) {
	    	String url = "http://www.paginasamarillas.es/search/all-ac/all-ma/all-pr/all-is/all-ci/all-ba/all-pu/all-nc/" + String.valueOf(i) + "?cu=aparcamientos&ub=false";
	    	html = getUrl(url);

//	    	String resultados = getSubstrings(html, "<div class=\"resultado\">", "</div>", true);	    	
	    	BufferedReader resultados = StringToBR(getSubstrings(html, "<div class=\"microficha\">", "</div>", true));
	    	String line;
	    	try {
				while ((line = resultados.readLine()) != null) {
					String nombre = getSubstring(line, "<a", "</a>", false);
					nombre = "nombre: " + "\"" + nombre.substring(nombre.lastIndexOf(">") + 1).trim().replace("\"", "").replace("\"", "") + "\"";
					String direccion = getSubstring(line, "<p class=\"adr\">", "</p>", false);
					String codigopostal = getSubstring(direccion, " , <span class=\"postal-code\">", "</span>", false);
					String municipio = getSubstring(direccion, " , <span class=\"locality\">", "</span>", false);
//					String provincia = getSubstring(direccion, " , <span class=\"region\">", "</span>", false);
					codigopostal = "codigopostal: " + "\"" + codigopostal.trim().replaceAll("\\s+", " ").replace("\"", "") + "\"";
					municipio = "municipio: " + "\"" + municipio.trim().replaceAll("\\s+", " ").replace("\"", "") + "\"";					
//					provincia = "provincia: " + "\"" + provincia.trim().replaceAll("\\s+", " ") + "\"";

					// busca y borra "" y <span del archivo de salida para validar el JSON
					int k;
					if ( (k = direccion.indexOf(", <")) >= 0 )
						direccion = direccion.substring(0, k - 1);
					else
						direccion = "";
					
					direccion = "direccion: " + "\"" + direccion.trim().replaceAll("\\s+", " ").replace("\"", "") + "\"";
					
					System.out.println("{ " + nombre + ", " + direccion + ", " + codigopostal + ", " + municipio + ", " + /*provincia +*/ " },");
					fileOut.println("{ " + nombre + ", " + direccion + ", " + codigopostal + ", " + municipio + ", " + /*provincia +*/ " },");
				}
				System.out.println("\n" + String.valueOf(numpages - i) + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	     	    	
//	    	String nombre = getSubstrings(line, "<h6><a", "</a>", false);
//			nombre = nombre.substring(nombre.lastIndexOf(">") );

//	    	System.out.println(resultados);
	    }
	    
	    
	    
	    try {
			outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    	
	}

	public static String getSubstring (String texto, String resultadotag, String resultadoendtag, Boolean incluyelimites) {
		
	    int resultadotagindex;
	    	if ((resultadotagindex = texto.indexOf(resultadotag)) < 0) {
	    		return "";
	    	}
	    int resultadoendtagindex = 0;
	    StringBuilder output = new StringBuilder();

    	resultadoendtagindex = texto.indexOf(resultadoendtag, resultadotagindex);
    	if (incluyelimites) {
    		resultadoendtagindex = resultadoendtagindex + resultadoendtag.length();
    	}
    	else {
    		resultadotagindex = resultadotagindex + resultadotag.length();
    	}
    	
	    output.append(texto.substring(resultadotagindex, resultadoendtagindex).replaceAll("\\s+", " "));
    	resultadotagindex = texto.indexOf(resultadotag, resultadoendtagindex + resultadoendtag.length());
	
		return output.toString();
	}

	
	public static String getSubstrings (String texto, String resultadotag, String resultadoendtag, Boolean incluyelimites) {
	
	    int resultadotagindex;
	    	if ((resultadotagindex = texto.indexOf(resultadotag)) < 0) {
	    		return "";
	    	}
	    	
	    int resultadoendtagindex = 0;
	    StringBuilder output = new StringBuilder();

		while ( resultadotagindex > -1 ) {
	    	resultadoendtagindex = texto.indexOf(resultadoendtag, resultadotagindex);
	//    	System.out.println("\n" + resultadotagindex  + " " + resultadoendtagindex);
	    	if (incluyelimites) {
	    		resultadoendtagindex = resultadoendtagindex + resultadoendtag.length();
	    	}
	    	else {
	    		resultadotagindex = resultadotagindex + resultadotag.length();
	    	}
	    	
		    output.append(texto.substring(resultadotagindex, resultadoendtagindex).replaceAll("\\s+", " ") + "\n");
	    	resultadotagindex = texto.indexOf(resultadotag, resultadoendtagindex + resultadoendtag.length());
	
	    }
		return output.toString();
	}
	 
	public static BufferedReader StringToBR (String str) {
	 
		InputStream is = new ByteArrayInputStream(str.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
	 	 
		return br;
	}
	
	
	public static java.lang.String getUrl(String url){
        HttpClient httpClient = new DefaultHttpClient();
        URI uri; // for URL
        InputStream data = null; 
        StringBuilder builder = new StringBuilder();

        try {
            uri = new URI(url);
            HttpGet method = new HttpGet(uri); // Get URI
            HttpResponse response = httpClient.execute(method); // Get response from method.  
            data = response.getEntity().getContent(); // Data = Content from the response URL.
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(data, "UTF-8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");

            }			
			
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
	
}
