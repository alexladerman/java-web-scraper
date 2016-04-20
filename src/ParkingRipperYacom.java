
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ParkingRipperYacom {

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

	
	    html = getUrl("http://paginas-amarillas.ya.com/SPagAmarillas/search?actividad=aparcamientos");
	    String numpagestag = "<div class=\"paginas\">";
	    int startnumpages = html.indexOf(numpagestag) + numpagestag.length();
	    numpagestag = "<h5><span>";
	    startnumpages = html.indexOf(numpagestag, startnumpages) + numpagestag.length();
	    String numpagesendtag = "</span> resultados referentes a";
	    int numpages = Integer.parseInt(html.substring(startnumpages, html.indexOf(numpagesendtag, startnumpages)))/15;
	    System.out.println(numpages);
	    
	    for (int i = 1; i <= numpages; i++) {
	    	String url = "http://paginas-amarillas.ya.com/SPagAmarillas/search?actividad=aparcamientos&page=" + String.valueOf(i);
	    	html = getUrl(url);

//	    	String resultados = getSubstrings(html, "<div class=\"resultado\">", "</div>", true);	    	
	    	BufferedReader resultados = StringToBR(getSubstrings(html, "<div class=\"resultado\">", "</div>", true));
	    	String line;
	    	try {
				while ((line = resultados.readLine()) != null) {
					String nombre = getSubstring(line, "<h6><a", "</a>", false);
					nombre = "nombre: " + "\"" + nombre.substring(nombre.lastIndexOf(">") + 1).trim() + "\"";
					String direccion = getSubstring(line, "<span>", "</span>", false);
					direccion = "direccion: " + "\"" + direccion.trim() + "\"";
					System.out.println("{ " + nombre + ", " + direccion + " },");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     	    	
//	    	String nombre = getSubstrings(line, "<h6><a", "</a>", false);
//			nombre = nombre.substring(nombre.lastIndexOf(">") );

//	    	System.out.println(resultados);
	    }
	    	
	}

	public static String getSubstring (String texto, String resultadotag, String resultadoendtag, Boolean incluyelimites) {
		
	    int resultadotagindex = texto.indexOf(resultadotag);
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
	
	    int resultadotagindex = texto.indexOf(resultadotag);
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
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(data));
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");

            }			
			
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
	
	

}
