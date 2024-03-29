package com.example.contactsexplorer.adapters;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.contactsexplorer.R;
import com.example.contactsexplorer.models.FileSystemItem;

public class FileListAdapter extends ArrayAdapter<FileSystemItem> {
	private Context c;
    private int id;
    private List<FileSystemItem>items;
   
    public FileListAdapter(Context context, int textViewResourceId, List<FileSystemItem> objects) {
            super(context, textViewResourceId, objects);
            c = context;
            id = textViewResourceId;
            items = objects;
    }
    
    public FileSystemItem getItem(int i)
    {
             return items.get(i);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View v = convertView;
       if (v == null) {
           LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           v = vi.inflate(id, null);
       }
      
       /* create a new view of my layout and inflate it in the row */
        //convertView = ( RelativeLayout ) inflater.inflate( resource, null );
       
       final FileSystemItem o = items.get(position);
       if (o != null) {
           TextView t1 = (TextView) v.findViewById(R.id.TextView01);
           TextView t2 = (TextView) v.findViewById(R.id.TextView02);
           TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
           /* Take the ImageView from layout and set the city's image */
           ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);
           
           Drawable image = null;
           if(!o.getImage().equalsIgnoreCase("process_icon"))
           {
        	   String uri = "drawable/" + o.getImage();               
        	   int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());               
        	   image = c.getResources().getDrawable(imageResource);
           }
           else
           {
        	   PackageManager pk1 = c.getPackageManager();// FileSelector.pk;
        	   try
        	   {
        		   image = pk1.getApplicationIcon(o.getName());
        	   }
        	   catch(Exception e)
        	   {
        		   String uri = "drawable/" + o.getImage();               
            	   int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());               
            	   image = c.getResources().getDrawable(imageResource);
        	   }
           }
           imageCity.setImageDrawable(image);
          
           if(t1!=null)
        	   t1.setText("" + o.getName());
           if(t2!=null)
        	   t2.setText("" + o.getData());
           if(t3!=null)
               t3.setText("" + o.getDate());
       }
       return v;
   }
}
