package com.example.contactsexplorer;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsexplorer.adapters.FileListAdapter;
import com.example.contactsexplorer.globals.GlobalDefines;
import com.example.contactsexplorer.models.FileSystemItem;

public class MainActivity extends ListActivity {

    private File currentDir;
    private FileListAdapter adapter;
    
    private final String TAG = "ContactExplorer";
    private final String ROOT_DIR = "/";
    private static int dirDepthLevel = 0;    
    private String extSDPath = "";
    
    private final String TAG_FOLDER_ICON = "folder_icon";
    private final String TAG_FILE_ICON = "file_icon";
    private final String TAG_DIR_BACK = "directory_back";
    private final String TAG_PARENT_DIRECTORY = "Parent Directory";
    private final String TAG_EXT_STORAGE = "external_storage";
    private final String TAG_INT_STORAGE = "internal_storage";
    
    private final String[] ACCEPTABLE_FILE_TYPES = {"json", "JSON"}; 
    private final Set<String> SET_ACCEPTABLE_FILE_TYPES = new HashSet<String> (Arrays.asList(ACCEPTABLE_FILE_TYPES));
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		currentDir = new File(ROOT_DIR);
		Log.d(TAG, "Current directory selected : " + currentDir.isDirectory());
		
		//populate the list by directory contents
		//Set the depth 0 means at the top list
		dirDepthLevel = 0;
		
        fill(currentDir);
        
        ShowToastMessage("Browse and Select contacts file....");        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	private void ShowToastMessage(String msg)
	{
		//Show custom toast to indicate user to select file
	    LayoutInflater inflater = getLayoutInflater();
	    View layout = inflater.inflate(R.layout.custom_toast_view, (ViewGroup) findViewById(R.id.toast_layout_root));
	    TextView text = (TextView) layout.findViewById(R.id.toast_text);
	    text.setText(msg);
	    Toast toast = new Toast(getApplicationContext());
	    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	    toast.setDuration(Toast.LENGTH_SHORT);
	    toast.setView(layout);
	    toast.show();
	}

	private void fill(File f)
    {
		String items_count = "0 item";
		Resources res = getResources();
		
		//Check if list already at root level
		if(dirDepthLevel <= 0)
        {
			 dirDepthLevel = 0;
			
	       	 Log.d(TAG, "Adding home menu ------ ");
	       	 List<FileSystemItem>dir = new ArrayList<FileSystemItem>();
	       	 this.setTitle(TAG);
	       	 
	       	 //add internal memory list item.        	 
	       	 File[]fbuf = f.listFiles();  
	       	         	 
	       	 Date lastModDate = new Date(f.lastModified());
	         DateFormat formater = DateFormat.getDateTimeInstance();
	         String date_modify = formater.format(lastModDate);
	         items_count = res.getQuantityString(R.plurals.numberOfItemsAvailable, fbuf.length, fbuf.length);
	       	 dir.add(new FileSystemItem("Internal Storage", items_count, date_modify, ROOT_DIR, TAG_INT_STORAGE));
	       	 
	       	        	 
	       	 //if string is empty means no external SD card available.
       		 File primaryExtSd = Environment.getExternalStorageDirectory();
       		 extSDPath = primaryExtSd.getAbsolutePath();
       		 Log.d(TAG, "SD Card path " + extSDPath);

       		 if(!extSDPath.equals(""))
       		 {
	       		 File ff = new File(extSDPath);
	       		 File[]fbuf1 = ff.listFiles();     
	           	 
	           	 lastModDate = new Date(ff.lastModified());
	             formater = DateFormat.getDateTimeInstance();
	             date_modify = formater.format(lastModDate);
	             items_count = res.getQuantityString(R.plurals.numberOfItemsAvailable, fbuf1.length, fbuf1.length);
	           	 dir.add(new FileSystemItem("External Storage", items_count, date_modify, extSDPath, TAG_EXT_STORAGE));
       		 }
       		 
	       	 adapter = new FileListAdapter(this, R.layout.folder_list, dir);
	         this.setListAdapter(adapter);
        }
        else
        { 
	         File[]dirs = f.listFiles();
	         
	         //Set current directory path 
	         String curDirName = f.getPath();
	         this.setTitle(curDirName);
	         
	         List<FileSystemItem>dir = new ArrayList<FileSystemItem>();
	         List<FileSystemItem>fls = new ArrayList<FileSystemItem>();
	         try
	         {
                 for(File ff: dirs)
                 {
                	//Get last modified date
                    Date lastModDate = new Date(ff.lastModified());
                    DateFormat formater = DateFormat.getDateTimeInstance();
                    String date_modify = formater.format(lastModDate);
                    
                    //if this item id a directory then display corresponding icon.
                    //Also show the directory contents again
                    if(ff.isDirectory())
                    {
                        File[] fbuf = ff.listFiles();
                        if(fbuf != null)
                        {
                        	//Log.d(TAG, "Files count : " + fbuf.length);
                        	items_count = res.getQuantityString(R.plurals.numberOfItemsAvailable, fbuf.length, fbuf.length);
                        }
                        else
                        {
                        	items_count = res.getQuantityString(R.plurals.numberOfItemsAvailable, 0, 0);
                        }
                                                
                        dir.add(new FileSystemItem(ff.getName(), items_count, date_modify, ff.getAbsolutePath(), TAG_FOLDER_ICON));
                    }
                    else
                    {
                        fls.add(new FileSystemItem(ff.getName(), ff.length() + " Bytes", date_modify, ff.getAbsolutePath(), TAG_FILE_ICON));
                    }
                 }
	         }
	         catch(Exception e)
	         {
	        	 Log.d(TAG, "Exception while showing list." + e.getMessage());
	         }
	         
	         //Sort the list alphabetically
	         Collections.sort(dir);
	         Collections.sort(fls);
	         dir.addAll(fls);
	
	         if(f.getName().trim().equals(""))
	         {
	        	 dir.add(0,new FileSystemItem("..",TAG_PARENT_DIRECTORY,"",ROOT_DIR,TAG_DIR_BACK));
	         }
	         else if(f.getName().trim().equals(extSDPath))
	         {
	        	 dir.add(0,new FileSystemItem("..",TAG_PARENT_DIRECTORY,"",extSDPath,TAG_DIR_BACK));
	         }
	         //If not showing home screen then add parent directory icon
	         else
	         {
	        	 dir.add(0,new FileSystemItem("..",TAG_PARENT_DIRECTORY,"",f.getParent(),TAG_DIR_BACK));
	         }
	         
	         //Add list adapter for the folder list.
	         adapter = new FileListAdapter(this, R.layout.folder_list,dir);
	         this.setListAdapter(adapter);
        }
    }
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        //Get the current selected item object
        FileSystemItem o = adapter.getItem(position);
        //Log.d(TAG, "Clicked  " + o.getImage() + "  " + o.getPath());

        //Check if clicked on back to parent
        if(o.getImage().equalsIgnoreCase(TAG_DIR_BACK))
        {
        	//If going upward means decreasing depth 
        	if(position == 0)
    			dirDepthLevel--;
        	currentDir = new File(o.getPath());
            fill(currentDir);
        }
        //Else this if file item so perform the action accordingly
        else if(o.getImage().equalsIgnoreCase(TAG_FILE_ICON))
        {
            onFileClick(o);
        }
        //Else if clicked on folder icon or storage icon
        else
        {
        	dirDepthLevel++;            	
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
    }
    
    private void onFileClick(FileSystemItem o)
    {
        // Check if this is json file
        String filenameArray[] = o.getName().trim().split(GlobalDefines.getFileNameExtnSprtor());
        String extension = filenameArray[filenameArray.length-1];
        
        if(!SET_ACCEPTABLE_FILE_TYPES.contains(extension))
        {
        	Log.d(TAG, "Clicked file type unacceptable extension :  " + extension);
        	ShowToastMessage("Not a contact file !!!");
        	return;
        }
        
        // acceptable file type.. proceed further
        Log.d(TAG, "Clicked file type acceptable extension : " + extension);
        
        // Check if file contains some data and not null
        if((new File(o.getPath())).length() <= 0)
        {
        	Log.d(TAG, "Clicked file does not contain anything");
        	ShowToastMessage("File is empty !!!");
        	return;
        }
        
        // Show contacts activity that will read file and process further 
        Intent intent = new Intent(getBaseContext(), ContactsActivity.class);
        //Pass JSON file path
        intent.putExtra(GlobalDefines.getJsonParam(), o.getPath());
        startActivity(intent);
        //finish();
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
