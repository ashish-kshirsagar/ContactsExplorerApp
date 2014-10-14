package com.example.contactsexplorer.models;

import java.util.ArrayList;

public class ContactsModel {
    private String companyName, name, parent;
    private ArrayList<String> phones, addresses, managers;
 
    public ContactsModel() {
    	name = "";
    	parent = "";
    	companyName = "";
    	phones = new ArrayList<String>();
    	addresses = new ArrayList<String>();
    	managers = new ArrayList<String>();
    }
 
    public ContactsModel(String name, String cname, String parent, ArrayList<String> phones, ArrayList<String> addrs, ArrayList<String> mgrs) {
        this.name = name;
        this.companyName = cname;
        this.parent = parent;
        this.phones = phones;
        this.addresses = addrs;
        this.managers = mgrs;
    }
 
 
    //Setter methods
    public void setName(String str) {
        this.name = str;
    }
 
    public void setCompany(String str) {
        this.companyName = str;
    }
 
    public void setParent(String str) {
        this.parent = str;
    }
    
    
    
    public void addPhone(String str) {
        this.phones.add(str);
    }
    
    public void addAddress(String str) {
        this.addresses.add(str);
    }
    
    public void addManager(String str) {
        this.managers.add(str);
    }
    
    
    
    public String getDetails()
    {
    	String item;
    	
    	item = (name.length() > 0)?("Name: " + name + "\n"):"";
    	item += (companyName.length() > 0)?("Company: " + companyName + "\n"):"";
    	item += (parent.length() > 0)?("Parent: " + parent + "\n"):"";
    	
    	item += (phones.size() > 0)?("\nPhones: "):"";
    	for(int i=0; i < phones.size(); i++)
    		item += "\n\t" + phones.get(i);
    	
    	item += (managers.size() > 0)?("\nManagers: "):"";
    	for(int i=0; i < managers.size(); i++)
    		item += "\n\t" + managers.get(i);
    	
    	item += (addresses.size() > 0)?("\nAddresses: "):"";
    	for(int i=0; i < addresses.size(); i++)
    		item += "\n\t" + addresses.get(i);
    	    	
    	return item;
    }
}
