
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONTest {

	private static String html;

	/**
	 * @param args
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		
		java.lang.String jsonString = getUrl("http://v1.ws.instapark.es/index.php?m=listallparkings&f=json");
				
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONArray jsonArray = jsonObj.getJSONArray("data");
		System.out.println(jsonArray.toString());
//		System.out.print(jsonString);
		
		
//	    FileWriter outFile = null;
//		try {
//			outFile = new FileWriter("JSONTest.txt");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	    PrintWriter fileOut = new PrintWriter(outFile);
//	    
//		fileOut.print(jsonString);
//		
//	    try {
//			outFile.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
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
