package com.example.contactsexplorer.globals;

public class GlobalDefines {
	private final static String PARAM_JSON_FILE_PATH = "PARAM_JSON_FILE_PATH";
	private final static String FILE_NAME_N_EXTN_SEPARATOR = "\\.";
	private final static String DEFAULT_FILE_ENCODING = "UTF-8";
	
	//JSON data tags
	//Arrays
	private final static String JSON_ARR_CONTACTS = "contacts";
	private final static String JSON_ARR_MANAGERS = "managers";
	private final static String JSON_ARR_ADDRESSES = "addresses";
	private final static String JSON_ARR_PHONES = "phones";
	
	//Data Tags
	private final static String JSON_TAG_COMPANY_NAME = "companyName";
	private final static String JSON_TAG_PARENT = "parent";
	private final static String JSON_TAG_NAME = "name";
	
	
	public static String getJsonParam()
	{
		return PARAM_JSON_FILE_PATH;
	}
	
	public static String getFileNameExtnSprtor()
	{
		return FILE_NAME_N_EXTN_SEPARATOR;
	}
	
	public static String getDefaultFileEncoding()
	{
		return DEFAULT_FILE_ENCODING;
	}
	
	
	//JSON Array tags get methods
	public static String getTagContacts()
	{
		return JSON_ARR_CONTACTS;
	}
	
	public static String getTagManagers()
	{
		return JSON_ARR_MANAGERS;
	}
	
	public static String getTagAddresses()
	{
		return JSON_ARR_ADDRESSES;
	}
	
	public static String getTagPhones()
	{
		return JSON_ARR_PHONES;
	}
	
	
	
	//JSON data tags get methods
	public static String getTagCompany()
	{
		return JSON_TAG_COMPANY_NAME;
	}
	
	public static String getTagParent()
	{
		return JSON_TAG_PARENT;
	}
	
	public static String getTagName()
	{
		return JSON_TAG_NAME;
	}

}
