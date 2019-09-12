package com.mobileapp.placesapisample;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.mobileapp.placesapisample.databinding.ActivityPlacesSearchBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class PlacesSearch extends AppCompatActivity  {
    private static final String LOG_TAG = "PlacesSearch";
    private static final String TAG = "PlacesSearch" ;
    private ActivityPlacesSearchBinding rootView;

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlacesArrayAdapter mPlaceArrayAdapter;
    // private PlaceArrayAdapter mPlaceArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = DataBindingUtil.setContentView(this, R.layout.activity_places_search);
        initViews();
    }

    private void initViews() {





//        mPlaceArrayAdapter = new PlaceArrayAdapter(this,
//                null, null);
//        rootView.placesTextview.setAdapter(mPlaceArrayAdapter);

       //rootView.placesTextview.addTextChangedListener(new onTextChangeListner());
        initializePlacesAdapter();
    }

    /**
     *
     */
    private void initializePlacesAdapter() {
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
//        rootView.placeSearch.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlacesArrayAdapter(this,
                R.layout.places_item_layout, null,placesClient);
        rootView.placesTextview.setAdapter(mPlaceArrayAdapter);
    }

    //================================================== Local callbacks ===================================================================================//





    //==================================Helper Methods ================================================================================================//

    private void getPredictions(String query){
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Create a RectangularBounds object.
//        RectangularBounds bounds = RectangularBounds.newInstance(
//                new LatLng(-33.880490, 151.184363),
//                new LatLng(-33.858754, 151.229596));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
               // .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
              // .setCountry("pk")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build();
        placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse findAutocompletePredictionsResponse) {
                for (AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());

            }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException) {
                ApiException apiException = (ApiException) e;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }

            }
        });





    }


    private class onTextChangeListner implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(final Editable s) {
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  getPredictions(s.toString());
              }
          },200);

        }
    }
}
