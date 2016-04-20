
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class outputGeocode {

	
	private static String html;

	/**
	 * @param args
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws JSONException, IOException {
		// TODO Auto-generated method stub
		

		BufferedReader br = new BufferedReader(new FileReader("/Users/alex/Documents/workspace/ParkingRipper/output.txt"));
		int c;
		StringBuilder jsonSB = new StringBuilder();

		while ((c = br.read()) != -1) {
		    jsonSB.append( (char)c ) ;  
		}    
		String jsonString = jsonSB.toString();
				

//		System.out.println(jsonArray.toString());
		
		
	    FileWriter outFile = null;
		try {
			outFile = new FileWriter("/Users/alex/Documents/workspace/ParkingRipper/outputgeocode.txt");	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    PrintWriter fileOut = new PrintWriter(outFile);
		
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONArray jsonArray = jsonObj.getJSONArray("data");
//		System.out.println(jsonArray.toString());

		fileOut.println("{data:[");
		for (int i = 0; i < 2000; i++) {
		    JSONObject row = jsonArray.getJSONObject(i);
		    System.out.println(row);

		    JSONObject geocodeResponse = new JSONObject(getUrl("http://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(row.getString("direccion") + ", " + row.getString("municipio") + ", " + row.getString("codigopostal"), "UTF-8") + "&sensor=false"));
		    JSONArray results = geocodeResponse.getJSONArray("results");
		    JSONObject data = results.getJSONObject(0);
		    JSONObject geometry = data.getJSONObject("geometry");
		    JSONObject location = geometry.getJSONObject("location");
		    System.out.println(location.toString());

		    row.append("latitud", location.getDouble("lat"));
		    row.append("longitud", location.getDouble("lng"));
			fileOut.println(row + ",");
		    System.out.println(row  + ",");
		    waiting((int) (Math.random() * ( 0 - 500 )));
		}
	    
		
	    try {
			outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	 
	public static void waiting (int n){         
        long t0, t1;
   
          t0 =  System.currentTimeMillis();
  
          do{
              t1 = System.currentTimeMillis();
          }
       while (t1 - t0 < n);
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
