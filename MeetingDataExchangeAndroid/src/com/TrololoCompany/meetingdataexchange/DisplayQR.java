package com.TrololoCompany.meetingdataexchange;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class DisplayQR extends Activity {

	private ServerEntity server;
	private MeetingEntity meeting;
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_qr);
		Intent intent=getIntent();
		server=(ServerEntity)
						intent.getSerializableExtra("com.TrololoCompany.meetingdataexchange.server");
		meeting=(MeetingEntity)
				intent.getSerializableExtra("com.TrololoCompany.meetingdataexchange.meeting");
		
		 imageView = (ImageView) findViewById(R.id.qrCode);
		 String text="mde;"+
				 	 server.getAddress()+";"+
				 	 meeting.getServerMeetingID()+";"+
				 	 meeting.getCode();
		 initTextView();
		 create_QRCode(text);
	 
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_qr, menu);
		return true;
	}
	private void initTextView()
	{
		TextView textViewServer=(TextView) findViewById(R.id.qrCode_server);
		TextView textViewmeeting=(TextView) findViewById(R.id.qrCode_meeting_id);
		TextView textViewAccessCode=(TextView) findViewById(R.id.qtCOde_meeting_access_code);
		textViewServer.setText("Serwer "+server.getAddress());
		textViewmeeting.setText("Identyfikator spotkania "+meeting.getServerMeetingID()+"");
		textViewAccessCode.setText("Kod dostepu "+meeting.getCode() );
	}
	private void create_QRCode(String input)
	{
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

	 QRCodeWriter qr= new QRCodeWriter();
	 BitMatrix result;
	 Bitmap photo;
	 try {
		result=qr.encode(input,BarcodeFormat.QR_CODE, width, height);
		photo=encodeAsBitmap(result);
		imageView.setImageBitmap(photo);
	} catch (WriterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	static Bitmap encodeAsBitmap(BitMatrix result) throws WriterException
            {
		 int width = result.getWidth();
		  int height = result.getHeight();
		  int[] pixels = new int[width * height];
		  // All are 0, or black, by default
		  for (int y = 0; y < height; y++) {
		    int offset = y * width;
		    for (int x = 0; x < width; x++) {
		      pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
		    }
		  }

		  Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		  bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		  return bitmap;
		}


}
