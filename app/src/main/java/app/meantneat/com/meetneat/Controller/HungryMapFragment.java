package app.meantneat.com.meetneat.Controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.meantneat.com.meetneat.Camera.SpecifiecChefEventsDialogBox;
import app.meantneat.com.meetneat.EventDishes;
import app.meantneat.com.meetneat.Model.MyModel;
import app.meantneat.com.meetneat.R;
//import com.google.android.gms.maps.*;
//import com.google.android.gms.maps.model.*;



/**
 * Created by mac on 5/17/15.
 */

 public class HungryMapFragment extends Fragment implements OnMapReadyCallback , LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static View view;
    private Map<Marker, EventDishes> allMarkersMap = new HashMap<Marker, EventDishes>();
    private ArrayList<EventDishes> eventsArray = new ArrayList<>();
    private ArrayList<LatLng> coordinatesArray = new ArrayList<>();
    private GoogleMap googleMapHungry;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    ViewGroup fragmentViewGroup;
    public LatLng lastCenter;
    public static LatLng lastCenterStatic;
    boolean firstTime = true;
    Marker lastMarker;
    BitmapDescriptor fixed;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        this.fragmentViewGroup=container;
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.hungry_map_fragment, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
       SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
       mapFragment.getMapAsync(this);
        mGoogleApiClient.connect();
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Google_Map = supportMapFragment.getMap();
//               SupportMapFragment mapFragment = (SupportMapFragment) (getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map));

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        Drawable dr = getResources().getDrawable(R.drawable.logo1);
        final Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        fixed = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 150, 150, true));


        this.googleMapHungry = googleMap;
        googleMapHungry.setMyLocationEnabled(true);
        //LatLng NEWARK = new LatLng(40.714086, -74.228697);

//        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
//                .image(BitmapDescriptorFactory.fromResource(R.drawable.newark_nj_1922))
//                .position(NEWARK, 8600f, 6500f);
        googleMapHungry.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {


            @Override
            public void onCameraChange(CameraPosition cameraPosition) {


                if(firstTime == true) {
                    lastCenter = new LatLng(50, 50);
                    lastCenterStatic = lastCenter;
                    firstTime = false;

                }


                    // CENTER MARKER //
                //Drawable dr = getResources().getDrawable(R.drawable.logo1);
                //final Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                //BitmapDescriptor fixed = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 150, 150, true));

                if(lastMarker != null)
                    lastMarker.remove();
                lastMarker = googleMapHungry.addMarker(new MarkerOptions()
                                .position(googleMapHungry.getCameraPosition().target)
                                .title("Center")

                                .icon(fixed)
                );



                float[] results = new float[10];
                Location.distanceBetween(
                        lastCenter.latitude,
                        lastCenter.longitude,
                        cameraPosition.target.latitude,
                        cameraPosition.target.longitude,
                        results);

                //results[0] = distance in Meters
                if(results[0] > 0)

                    getClosestCoordinatesFromServer();



                lastCenter = googleMapHungry.getCameraPosition().target;
                lastCenterStatic = lastCenter;
            }

        });

        googleMapHungry.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

//                final SpecifiecChefEventsDialogBox dialogBox = new SpecifiecChefEventsDialogBox(
//                        getActivity(),"pFubDWWXGT",marker.getPosition());
                EventDishes eventByMarker =  allMarkersMap.get(marker);
                final SpecifiecChefEventsDialogBox dialogBox = new SpecifiecChefEventsDialogBox(
                        getActivity(),eventByMarker.getChefID(),eventByMarker.getChefName(),marker.getPosition());


//                final SpecificEventDishesDialogBox dialogBox = new SpecificEventDishesDialogBox(getActivity(),"7k60BVnPPQ","Jonathan Roshfeld","01.08.2004 - 03.09.2014","Italian Party");
//                dialogBox.getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        //dialogBox.getDialog().show();
//                    }
//                });

                dialogBox.show();
                return false;
            }
        });
        //googleMapHungry.animateCamera(Cmera);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.073776, 34.781890), 16));
        //ArrayList<LatLng> coordinatesArr = getClosestCoordinatesFromServer();
        //getClosestCoordinatesFromServer();

    }

    private void getClosestCoordinatesFromServer() {

        MyModel.getInstance().getModel().getClosestChefsRadius(
                new ChefEventDishesFragment.GetEventDishesCallback() { //Callback
                    @Override
                    public void done(ArrayList<EventDishes> eventDisheses) {

                        eventsArray = eventDisheses;
                        ArrayList<LatLng> coordinatesArrayTmp = new ArrayList<>();
                        for (EventDishes e : eventsArray) {

                            LatLng tmp = new LatLng(e.getLatitude(), e.getLongitude());
                            coordinatesArrayTmp.add(tmp);


                        }
                        coordinatesArray = coordinatesArrayTmp;
                        showClosestEvents();
                    }
                }
                , googleMapHungry.getCameraPosition().target); //MapCenter

    }

    private void showClosestEvents()
    {
        //make hashmap of markers and coordinates - if there is
        // 2 diffrent chefs in the same coordinate rotate the marker
        // if there same chef in the same location diffrent events - add only one event...




        // dr = getResources().getDrawable(R.drawable.logo1);
        //final Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        //BitmapDescriptor fixed = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 150, 150, true));
        int i=0;
        googleMapHungry.clear();
        lastMarker = googleMapHungry.addMarker(new MarkerOptions()
                        .position(googleMapHungry.getCameraPosition().target)
                        .title("Center")

                        .icon(fixed)
        );

        allMarkersMap.clear();
        for (i = 0;i< coordinatesArray.size(); i++) {




            Marker m = googleMapHungry.addMarker(new MarkerOptions()
                            .position(coordinatesArray.get(i))
                            .title("Marker")
                            .rotation((float)90.0)
                            .icon(fixed)
            );

            allMarkersMap.put(m, eventsArray.get(i));
        }
    }

        
    @Override
    public void onConnected(Bundle bundle) {
        Log.e("LNGLTD", "Connected");

        // on location update by time - (listener in the class)
        //LocationServices.FusedLocationApi.requestLocationUpdates(
           //    mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getActivity(),"Location Updated",Toast.LENGTH_SHORT).show();
        googleMapHungry.clear();
        getClosestCoordinatesFromServer();
        //googleMapHungry.clear();
    }
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.map_activity);
//
//            MapFragment mapFragment = (MapFragment) getFragmentManager()
//                    .findFragmentById(R.id.map);
//            mapFragment.getMapAsync(this);
//        }
//
//        @Override
//        public void onMapReady(GoogleMap map) {
//            LatLng sydney = new LatLng(-33.867, 151.206);
//
//            map.setMyLocationEnabled(true);
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//            map.addMarker(new MarkerOptions()
//                    .title("Sydney")
//                    .snippet("The most populous city in Australia.")
//                    .position(sydney));
//        }
//    }


private Bitmap createChefTemplate(Bitmap chefImage)
{
    View v = getActivity().getLayoutInflater().inflate(R.layout.chef_template,fragmentViewGroup,false);
    ImageView imageView = new ImageView(getActivity());
    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
    //int px = Math.round(256 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1048,1048);
    params.gravity= Gravity.CENTER;
    imageView.setLayoutParams(params);
    imageView.setBackground(getResources().getDrawable(R.drawable.eyal_shani));
    FrameLayout layout = (FrameLayout)v.findViewById(R.id.chef_template_container);
    layout.addView(imageView);

   // if the view wasn't displayed before the size of it will be zero. Its possible to measure it like this:
    Bitmap b;
    if (v.getMeasuredHeight() <= 0) {
        v.measure(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        b = Bitmap.createBitmap(1048, 1512, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        return b;
    }
    else
    {
         b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
    }


return b;

}

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }


}
