package com.tiger.arch.codelab.location;

import android.annotation.SuppressLint;
import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;

public class BoundLocationManager {


    public static void bindLocationListener(LifecycleOwner lifecycleOwner, LocationListener locationListener, Context context) {
        new LocationObserver(lifecycleOwner, locationListener, context);
    }

    private static class LocationObserver implements DefaultLifecycleObserver {
        private static final String TAG = "LocationObserver";

        private LocationListener mLocationListener;
        private Context mContext;
        private LocationManager mLocationManager;

        public LocationObserver(LifecycleOwner lifecycleOwner,LocationListener locationListener, Context context) {
            mLocationListener = locationListener;
            mContext = context;
            lifecycleOwner.getLifecycle().addObserver(this);
        }


        @Override
        public void onResume(@NonNull LifecycleOwner owner) {
            addLocationListener();
        }

        @Override
        public void onPause(@NonNull LifecycleOwner owner) {
            removeLocationListener();
        }

        @SuppressLint("MissingPermission")
        void addLocationListener() {
            Log.d(TAG, "addLocationListener: ");
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (mLocationListener != null) {
                mLocationListener.onLocationChanged(lastKnownLocation);
            }
        }

        private void removeLocationListener() {
            if (mLocationManager != null) {
                mLocationManager.removeUpdates(mLocationListener);
                mLocationManager = null;
            }
        }


    }

}
