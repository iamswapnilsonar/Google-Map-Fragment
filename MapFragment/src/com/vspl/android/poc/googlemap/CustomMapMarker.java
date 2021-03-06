package com.vspl.android.poc.googlemap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This is example of Google Map V2. It also have functionality of adding the marker on it.  
 * @author Swapnil Sonar
 */
public class CustomMapMarker extends FragmentActivity {

	private GoogleMap mMap;
	
	@SuppressWarnings("unused")
	private Marker customMarker;
	
	private LatLng markerLatLng;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);

		markerLatLng = new LatLng(18.5062, 73.8288);

		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {

		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * Map settings
	 */
	private void setUpMap() {

		View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.custom_marker_layout, null);
		TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
		numTxt.setText("27");

		customMarker = mMap.addMarker(new MarkerOptions()
				.position(markerLatLng)
				.title("VSPL")
				.snippet("Vatsa Solutions Pvt.Ltd.")
				.icon(BitmapDescriptorFactory
						.fromBitmap(createDrawableFromView(this, marker))));

		final View mapView = getSupportFragmentManager().findFragmentById(
				R.id.map).getView();
		
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@SuppressLint("NewApi")

						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							
							LatLngBounds bounds = new LatLngBounds.Builder().include(markerLatLng).build();

							mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
						}
					});
		}
	}

	
	/**
	 * Convert the view into Bitmap image. 
	 * @param context
	 * @param view
	 * @return bitmap image
	 * @author Swapnil Sonar
	 */
	public static Bitmap createDrawableFromView(Context context, View view) {
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,displayMetrics.heightPixels);
		
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}
}