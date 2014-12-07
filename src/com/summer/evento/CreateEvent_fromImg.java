package com.summer.evento;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CreateEvent_fromImg extends Activity {

	EditText text_result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event_from_img);
		
		text_result = (EditText) findViewById(R.id.result_text);
		Button take_photo_btn = (Button) findViewById(R.id.btn_take_photo);
		Button select_photo_btn = (Button) findViewById(R.id.btn_select_photo);
		Button confirm_event_btn = (Button) findViewById(R.id.confirm);
		
		take_photo_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick (View v) {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 0);
			}
		});
		
		select_photo_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick (View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(intent, 1);
			}
		});
		
		confirm_event_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent createEvent = new Intent(CreateEvent_fromImg.this, CreateEvent.class);
                createEvent.putExtra("recognizedText", text_result.getText().toString());
                startActivity(createEvent);
            }

        });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Copy file
		String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Evento/";
		File dir = new File(DATA_PATH + "tessdata");
		dir.mkdirs();
		if (!(new File(DATA_PATH + "tessdata/eng.traineddata")).exists()) {
			try {
				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("eng.traineddata");
				OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/eng.traineddata");
				byte[] buffer = new byte[1024];
				int len;
				while((len = in.read(buffer)) > 0) {
					out.write(buffer, 0 , len);
				}
				in.close();
				out.close();
			} catch (IOException e) {}
		}

		// Start decoding
		if (resultCode != RESULT_OK) { return; }
		Bitmap theImage;
		Uri imageUri;
		if (requestCode == 0) {
			theImage = (Bitmap) data.getExtras().get("data");
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		    theImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		    String path = Images.Media.insertImage(getApplicationContext().getContentResolver(), theImage, "Title", null);
		    imageUri = Uri.parse(path);
		} else {
			imageUri = data.getData();
			String[]projection = {MediaStore.Images.Media.DATA};
			
			Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
			cursor.moveToFirst();
			
			int columnIndex = cursor.getColumnIndex(projection[0]);
			String filePath = cursor.getString(columnIndex);
			cursor.close();
			
			theImage = BitmapFactory.decodeFile(filePath);
		}
		
		try {
			// Rotate
			ExifInterface exif = new ExifInterface(imageUri.getPath());
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); 
			int rotationInDegrees = 0;
			if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { rotationInDegrees = 90; } 
		    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  rotationInDegrees = 180; } 
		    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  rotationInDegrees = 270; } 

			if (rotationInDegrees != 0) {
				int w = theImage.getWidth();
				int h = theImage.getHeight();
				Matrix matrix = new Matrix();
				matrix.preRotate(rotationInDegrees);
				theImage = Bitmap.createBitmap(theImage, 0, 0, w, h, matrix, false);
				theImage = theImage.copy(Bitmap.Config.ARGB_8888, true);
			}
		}
		catch (IOException e) {}
		
		// Use Tess
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.init(DATA_PATH, "eng");
		baseApi.setImage(theImage);
		String recognizedText = baseApi.getUTF8Text();
		baseApi.end();
		text_result.setText(recognizedText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event_from_img, menu);
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
