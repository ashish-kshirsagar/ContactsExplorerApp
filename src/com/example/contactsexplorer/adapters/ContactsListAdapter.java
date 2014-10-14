package com.example.contactsexplorer.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.contactsexplorer.R;
import com.example.contactsexplorer.models.ContactsModel;

public class ContactsListAdapter extends ArrayAdapter<ContactsModel> implements Filterable
{
	private Context c;
    private int id;
    private List<ContactsModel>items;
    
	public ContactsListAdapter(Context context, int ViewResourceId, List<ContactsModel> objects) {
        super(context, ViewResourceId, objects);
        c = context;
        id = ViewResourceId;
        items = objects;
	}
	
	public ContactsModel getItem(int i)
	{
	    return items.get(i);
	}
	
	@Override
    public int getCount() {
        return items.size();
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
       /* create a new view of passed layout and inflate it in the row */       
       if (convertView == null) {
           LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = vi.inflate(id, null);
       }      
       
       final ContactsModel o = items.get(position);
       if (o != null) {
           TextView contactDetails = (TextView) convertView.findViewById(R.id.contactDetails);
           if(contactDetails != null)
        	   contactDetails.setText(o.getDetails());
       }
       return convertView;
	}
}
