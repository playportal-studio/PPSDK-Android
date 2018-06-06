package examples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.dynepic.ppsdk_android.LoginActivity;
import com.dynepic.ppsdk_android.PPManager;
import com.dynepic.ppsdk_android.R;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	// OnDeck
	private static String id = "iok-cid-444ad028be7e16c1157962992ccd35e33823d6728a33d8fa";
	private static String sec = "iok-cse-93714e707231284f4d30de950c5d6580434c0b7ae6e7d7f5";
	private static String uri = "ondeck://redirect";

	private TextView mTextMessage;
	private TextView mGlobalTextMessage;
	private Button incrementButton;

	// Simple KV data test vars
	private static int readIterationCount = 0;
	private static int writeIterationCount = 0;
	private static int globalIterationCount = 100;
	private static Boolean firstTime = true;

	// Complex data test vars
	private static Boolean complexFirstTime = true;
	private static int authorCount = 0;

private static class TestData {
	public String toJSONString() {
		return "{" +
				"Title='" + Title + '\'' +
				", Author='" + Author + '\'' +
				", Content='" + Content + '\'' +
				", Pages='" + Pages + '\'' +
				'}';
	}

	@Override
	public String toString() {
		return "TestData{" +
				"Title='" + Title + '\'' +
				", Author='" + Author + '\'' +
				", Content='" + Content + '\'' +
				", Pages='" + Pages + '\'' +
				'}';
	}
	@SerializedName("Title")
	@Expose
	String Title;

	public String getTitle() {
		return Title;
	}

	@SerializedName("Author")
	@Expose
	String Author;

	public String getAuthor() {
		return Author;
	}

	@SerializedName("Content")
	@Expose
	String Content;

	public String getContent() {
		return Content;
	}

	@SerializedName("ArrayOfStrings")
	@Expose
	ArrayList ArrayOfStrings;
	public ArrayList getArrayOfStrings() {
		return ArrayOfStrings;
	}

	@SerializedName("Pages")
	@Expose
	Integer Pages;

	public Integer getPages() {
		return Pages;
	}
};



private PPManager ppsdk;
public void configurePPSDK()
		{
		ppsdk = PPManager.getInstance();
		ppsdk.configure(id, sec, uri, getApplicationContext());
		}


@Override
public Context getApplicationContext() {
		return super.getApplicationContext();
		}

private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
		= new BottomNavigationView.OnNavigationItemSelectedListener() {

private void writeComplexJSONTest() {
		Gson gson = new Gson();
		TestData td = new TestData();

		Log.d("reading complex data", "");
		ppsdk.readBucket(ppsdk.getPrivateDataStorage(), "TestData", (String readKey, String readValue, String readData, String readError) -> {
		if (readError == null) {
		Log.d("Testdata read key:", readKey + " value:" + readValue + " data:" + readData);
		TestData anotherTd = gson.fromJson(readData, TestData.class);
		Log.d("anotherTd Title:", anotherTd.getTitle());
		Log.d("anotherTd Author:", anotherTd.getAuthor());
		Log.d("anotherTd Content:", anotherTd.getContent());
		Log.d("anotherTd Pages:", anotherTd.getPages().toString());

		if(firstTime) {
		String[] tokens = anotherTd.Author.split(" ");
		Log.d("count:", tokens[1]);
		authorCount = new Integer(tokens[1]);
		authorCount++;
		firstTime = false;
		mTextMessage.setText("Private- read bucket: " + "syncing bucket");

		} else {
		Log.d("anotherTd ArrayOfStrings:", anotherTd.getArrayOfStrings().toString());
		}

		td.Author = "Author " + new Integer(authorCount).toString();
		td.Content = "This is a sample of trivial content";
		td.Title = "Forgetable Title " + new Integer(authorCount++).toString();
		td.Pages = authorCount % 11;
		td.ArrayOfStrings = new ArrayList();
		td.ArrayOfStrings.add("first string");
		td.ArrayOfStrings.add("second string");
		mTextMessage.setText("Private- read bucket: " + new Integer(authorCount).toString());

		ppsdk.writeBucket(ppsdk.getPrivateDataStorage(), "TestData", gson.toJson(td), false, (String writeKey, String writeValue, String writeData, String writeError) -> {
		if (writeError != null) {
		Log.e("Error writing complex data to private bucket:", writeError);
		}
		});
		} else {
		Log.e("Error reading complex data from private bucket:", readError);
		}
		});
		}

@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
		case R.id.navigation_home:

		writeComplexJSONTest();


		return true;

		case R.id.navigation_profile:
		mTextMessage.setText("Profile");
//		Intent profileIntent = new Intent(MainActivity.this, UserProfileActivity.class);
//		profileIntent.putExtra("key", "1"); //Optional parameters
//		MainActivity.this.startActivity(profileIntent);

		return true;

		case R.id.navigation_logout:
		mTextMessage.setText("logout");
AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", dialogClickListener)
		.setNegativeButton("No", dialogClickListener).show();

		return true;
		}
		return false;
		}
		};

@Override
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTextMessage = (TextView) findViewById(R.id.message);
		mGlobalTextMessage = (TextView) findViewById(R.id.globalmessage);
		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		configurePPSDK();

		// Determine if user is already authenticated
		if(!ppsdk.isAuthenticated()) {
		Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
		myIntent.putExtra("key", "1"); //Optional parameters
		MainActivity.this.startActivity(myIntent);
		}
}


		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
		switch (which){
		case DialogInterface.BUTTON_POSITIVE:
		ppsdk.logout();
		finish();
		break;

		case DialogInterface.BUTTON_NEGATIVE:
		//No button clicked
		break;
		}
		}
		};


		}
