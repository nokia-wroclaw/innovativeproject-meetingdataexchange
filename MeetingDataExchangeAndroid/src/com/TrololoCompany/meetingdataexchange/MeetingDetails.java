package com.TrololoCompany.meetingdataexchange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import meeting_options.FireMissilesDialogFragment;
import meeting_options.MeetDetOnPageListener;
import meeting_options.MeetDetTabListener;
import meeting_options.MeetingTabPagerAdapter;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
public class MeetingDetails extends FragmentActivity 
{
  private ViewPager Tab;
  private MeetingTabPagerAdapter TabAdapter;
  private ActionBar actionBar;
  String path="meeting1";///temp
  
  
  
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        initTab();
        initActionBar();
      
    }
    private File createImageFile(String directory,String name)  {
        // Create an image file name
       // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    	String path= getApplicationContext().getApplicationInfo().dataDir+"/"+directory;
    	File dir=new File(path);
    	if(!dir.isDirectory())
    		dir.mkdir();
    	
    	File [] temp=dir.listFiles();
    	for(int i=0;i<temp.length;i++)
    	{
    		Log.i("tahg",temp[i].getName());
    		Log.i("tahg",temp[i].length()+"");
    	}
    	File image = new File(path,name);
    	try {
			if(image.createNewFile()==true)
				Log.i("TAG","fileCreated");
			else
				Log.i("TAG","errWhilefilecreating");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("TAG","errWhilefilecreating");
		}
        return image;
    }
    public void add_photo_Button(View v) 
    {
    	

		//dialog.show();
    	
       Log.i("dzial", "dziala");
       Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       
       // Ensure that there's a camera activity to handle the intent
       if (takePictureIntent.resolveActivity(getPackageManager()) != null) 
       {
    	   Log.i("info","test");
    	   startActivityForResult(takePictureIntent, 1);
       }
      
       
    }
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           if (requestCode == 1 && resultCode == RESULT_OK) {
               
        	   Log.i("infro","2");
               Bundle temp=data.getExtras();
               Bundle extras = data.getExtras();
               Bitmap imageBitmap = (Bitmap) extras.get("data");
               String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
               File img = createImageFile(path, timeStamp);
               try 
               {
            	   FileOutputStream fileOut= new FileOutputStream(img);
            	   imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOut);
            	   Log.i("infro","done");
               } 
               catch (FileNotFoundException e) 
               {
				// TODO Auto-generated catch block
            	   Log.i("infro","err");
				e.printStackTrace();
               }
               
               
           }
       }
    void initActionBar()
    {
    	actionBar = getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	MeetDetTabListener tabListener = new MeetDetTabListener(Tab);
    	
    	ActionBar.Tab descTab=actionBar.newTab();
    	ActionBar.Tab progressTab=actionBar.newTab();
    	ActionBar.Tab addTab=actionBar.newTab();
    	
    	descTab.setText(getString(R.string.add_item_ac_meeting_desc_button));
    	progressTab.setText(getString(R.string.add_item_ac_meeting_progress_button));
    	addTab.setText(getString(R.string.add_item_ac_meeting_add_button));
    	
    	descTab.setTabListener(tabListener);
    	progressTab.setTabListener(tabListener);
    	addTab.setTabListener(tabListener);
    	
    	actionBar.addTab(descTab);
    	actionBar.addTab(progressTab);
    	actionBar.addTab(addTab);
    }
    void initTab()
    {
    	TabAdapter = new MeetingTabPagerAdapter(getSupportFragmentManager());
        Tab = (ViewPager)findViewById(R.id.pager);
        this.actionBar = getActionBar();
        Tab.setOnPageChangeListener(new MeetDetOnPageListener(actionBar));
        Tab.setAdapter(TabAdapter);
    }
    
}