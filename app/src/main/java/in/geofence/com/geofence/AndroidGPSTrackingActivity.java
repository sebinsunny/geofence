package in.geofence.com.geofence;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class AndroidGPSTrackingActivity extends Activity {
	private ArrayList<Product> productList;
	///double lat=9.951;
	//double lng=76.631;
double r1;
	double r2;
	private ListView lvProduct;



	public EditText editTextName;
	public EditText editTextAdd;
	public Button btnShowLocation;
	protected EditText memail;
	protected EditText mpassword;
	TextView s, ss;

	double latitude, longitude;
	// GPSTracker class
	GPSTracker gps;
	String d,b,c,k;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
		s = (TextView) findViewById(R.id.b);
		ss = (TextView) findViewById(R.id.c);

		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try{

					final HashMap postData = new HashMap();

					// create class oject

					gps = new GPSTracker(AndroidGPSTrackingActivity.this);

					// check if GPS enabled


					if (gps.canGetLocation()) {

						latitude = gps.getLatitude();
						longitude = gps.getLongitude();
//r1=lat-latitude;
//r2=lng-longitude;
						// \n is for new line
						s.setText("" + latitude);
						ss.setText("" + longitude);

						String username = s.getText().toString();
						String password = ss.getText().toString();
						Bundle bundle = getIntent().getExtras();
						final String a = bundle.getString("s");
						postData.put("username", a);
						postData.put("lattitude", username);
						postData.put("longitude", password);
						///  if(r1>=-0.08 && r2>=-.035){
						// String s="inside";
						// postData.put("status",s);
						// }else {
						//  String f="outside";
						//  postData.put("status",f);
						//}


						PostResponseAsyncTask task1 = new PostResponseAsyncTask(AndroidGPSTrackingActivity.this, postData, new AsyncResponse() {
							@Override


							public void processFinish(String s) {
								//     Log.d(LOG, s);
								if (s.contains("success")) {
									//Toast.makeText(AndroidGPSTrackingActivity.this, "sending", Toast.LENGTH_LONG).show();
									PostResponseAsyncTask t = new PostResponseAsyncTask(AndroidGPSTrackingActivity.this, s, new AsyncResponse() {

										@Override
										public void processFinish(String s) {

											productList = new JsonConverter<Product>().toArrayList(s, Product.class);

											BindDictionary<Product> dict = new BindDictionary<Product>();
											dict.addStringField(R.id.tvName, new StringExtractor<Product>() {
												@Override
												public String getStringValue(Product product, int position) {
													return product.username;

												}


											});


											dict.addStringField(R.id.tvNam, new StringExtractor<Product>() {
												@Override
												public String getStringValue(Product product, int position) {
													return product.status;

												}


											});


											FunDapter<Product> adapter = new FunDapter<>(AndroidGPSTrackingActivity.this, productList, R.layout.layout_list, dict);

											lvProduct = (ListView) findViewById(R.id.lvProduct);
											lvProduct.setAdapter(adapter);

											//lvProduct.setOnItemClickListener(this);


										}
									});

									t.execute("http://geofence.in/retrieve.php?id="+a);


								} else {
									Toast.makeText(AndroidGPSTrackingActivity.this, "inserted", Toast.LENGTH_LONG).show();
								}
							}


						});
						task1.execute("http://geofence.in/check/retrieve.php");



						try {
							d= URLEncoder.encode(a, "utf-8");
							b = URLEncoder.encode(username, "utf-8");
							c = URLEncoder.encode(password, "utf-8");

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


						k = "http://geofence.in/value3.php?un="+d+"&lp="+b+"&lg="+c;

						HttpAsyncTask hat = new HttpAsyncTask();
						hat.execute(k);









					}

					else

					{
						// can't get location
						// GPS or Network is not enabled
						// Ask user to enable GPS/network in settings
						gps.showSettingsAlert();
					}










					//do your code here
					//also call the same runnable
					handler.postDelayed(this, 30000);
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				finally{
					//also call the same runnable
					handler.postDelayed(this,30000);
				}
			}
		};
		handler.postDelayed(runnable, 30000);











	}


	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {

			return httpRequestResponse(urls[0]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

		}
	}

	//For HttpAsync Functions: sending requests and receiving responses
	public static String httpRequestResponse(String url){
		InputStream inputStream = null;
		String result = "";
		try {
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert InputStream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "InputStream did not work";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}





}






