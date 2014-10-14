package com.example.contactsexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.contactsexplorer.adapters.ContactsListAdapter;
import com.example.contactsexplorer.globals.GlobalDefines;
import com.example.contactsexplorer.models.ContactsModel;

public class ContactsActivity extends ListActivity {

    private ContactsListAdapter adapter;
    private static String contactsFilePath;
    private static JSONObject JSONObject = null;
    private final String TAG = "Contacts Activity";
    private List<ContactsModel> contactsList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		contactsFilePath = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			contactsFilePath = extras.getString(GlobalDefines.getJsonParam());
			Log.d(TAG, "JSON file path received : " + contactsFilePath);
		}
		
		//If the path received successfully then proceed further
		if(!contactsFilePath.equals(""))
		{
			contactsList = new ArrayList<ContactsModel>();
			adapter = new ContactsListAdapter(this, R.layout.contacts_list_row, contactsList);
			this.setListAdapter(adapter);
			
			//Parse json file and show lists
			new ParseContacts().execute(contactsFilePath);
			
		}
		
		
		final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
		inputSearch.addTextChangedListener(new TextWatcher() {		     
		    @SuppressLint("DefaultLocale") 
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        // When user changed the Text
		    	int textlength = cs.length();
	            ArrayList<ContactsModel> tempArrayList = new ArrayList<ContactsModel>();
	            for(ContactsModel c: contactsList){
	              if (textlength <= c.getDetails().length()) {
	                 if (c.getDetails().toLowerCase().contains(cs.toString().toLowerCase())) {
	                    tempArrayList.add(c);
	                 }
	              }
	            }
	            adapter = new ContactsListAdapter(ContactsActivity.this, R.layout.contacts_list_row, tempArrayList);
	            ContactsActivity.this.setListAdapter(adapter);
		    }
		     
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		        // TODO Auto-generated method stub		         
		    }
		     
		    @Override
		    public void afterTextChanged(Editable arg0) {
		        // TODO Auto-generated method stub                          
		    }
		});
	}
	
	
	
	// Async class to parse JSON and display list
	// Async task required if JSON is very big to parse
    private class ParseContacts extends AsyncTask<String, Void, Void> 
    { 
    	ProgressDialog pDialog;
    	JSONArray jsonArray;    	
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog till the list loads
            pDialog = new ProgressDialog(ContactsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 

		@Override
        protected Void doInBackground(String ... path) 
        {
            try 
            {
                JSONObject = parseJSONData(path[0]);
                
                // Getting JSON Array node
                JSONArray contacts = JSONObject.getJSONArray(GlobalDefines.getTagContacts());
 
                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) 
                {
                	//Create a new Contacts model object 
                	ContactsModel record = new ContactsModel();
                	
                    JSONObject contactsObj = contacts.getJSONObject(i);
                    
                    //Check if this object contains name tag
                    if(contactsObj.has(GlobalDefines.getTagName()))
                    	record.setName(contactsObj.getString(GlobalDefines.getTagName()));
                    
                    //Check is company name exists
                    if(contactsObj.has(GlobalDefines.getTagCompany()))
                    	record.setCompany(contactsObj.getString(GlobalDefines.getTagCompany()));
                    
                    //Check is parent name exists
                    if(contactsObj.has(GlobalDefines.getTagParent()))
                    	record.setParent(contactsObj.getString(GlobalDefines.getTagParent()));
                    
                    //Check if phones exists
                    if(contactsObj.has(GlobalDefines.getTagPhones()))
                    {
                    	jsonArray = contactsObj.getJSONArray(GlobalDefines.getTagPhones());
                    	//Log.d(TAG, "Phones Array : " + jsonArray.toString());                    	
                    	for(int j=0; j<jsonArray.length(); j++)
                    		record.addPhone(jsonArray.getString(j));
                    }
                    
                    //Check if managers exists                    
                    if(contactsObj.has(GlobalDefines.getTagManagers()))
                    {
                    	jsonArray = contactsObj.getJSONArray(GlobalDefines.getTagManagers());
                    	//Log.d(TAG, "Managers Array : " + jsonArray.toString());
                    	for(int j=0; j<jsonArray.length(); j++)
                    		record.addManager(jsonArray.getString(j));
                    }
                    
                    //Check if addresses exists                    
                    if(contactsObj.has(GlobalDefines.getTagAddresses()))
                    {
                    	jsonArray = contactsObj.getJSONArray(GlobalDefines.getTagAddresses());
                    	//Log.d(TAG, "Addresses Array : " + jsonArray.toString());
                    	for(int j=0; j<jsonArray.length(); j++)
                    		record.addAddress(jsonArray.getString(j));
                    }                    
                    
                    // adding contact to contact record to global list
                    contactsList.add(record);
                    
                    publishProgress();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
 
        // called from the publish progress
        @Override
        protected void onProgressUpdate(Void ... value) {
            super.onProgressUpdate(value);
            
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            
            //Notify that new data has been added to the adapter
            adapter.notifyDataSetChanged();
        }
        
        
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        } 
    }
	
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);        
        Log.d(TAG, "Clicked Item [" + position + "]  \n" + contactsList.get(position).getDetails());
    }
    
	
    // Method will parse the contacts JSON file and will return a JSONObject 
    public JSONObject parseJSONData(String filePath) 
    {
            String JSONString = null;
            JSONObject JSONObject = null;
            try 
            {
            	//open the inputStream to the file 
            	File contactsFile = new File(filePath);
            	FileInputStream inputStream = new FileInputStream(contactsFile);
            	int sizeOfJSONFile = inputStream.available();
                //array that will store all the data 
                byte[] bytes = new byte[sizeOfJSONFile];
                
                //reading data into the array from the file
                inputStream.read(bytes);

                //close the input stream
                inputStream.close();

                JSONString = new String(bytes, GlobalDefines.getDefaultFileEncoding());
                JSONObject = new JSONObject(JSONString);

            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            catch (JSONException x) {
                x.printStackTrace();
                return null;
            }
            return JSONObject;
       } 
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
