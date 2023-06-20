package com.example.caritaappnew.view.main.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.caritaappnew.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.caritaappnew.databinding.ActivityMapsBinding
import com.example.caritaappnew.model.UserModel
import com.example.caritaappnew.model.respon.Stories
import com.example.caritaappnew.model.userPreference
import com.example.caritaappnew.view.main.dataStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocation : FusedLocationProviderClient
    private lateinit var userPrefs: userPreference
    private lateinit var viewmodel : MapsViewModel


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){ permissions ->
            when{
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getLocation()
                    setMapStyle()
                }
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getLocation()
                    setMapStyle()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewmodel = ViewModelProvider(this)[MapsViewModel::class.java]
        userPrefs = userPreference.getInstance(dataStore)
        lifecycleScope.launch {
            userPrefs.getData().collect { userModel ->
                val userToken = userModel.token
                viewmodel.storyLocation(userToken)
            }
        }
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        viewmodel.getMapStory().observe(this){
            if (it != null){
                addStoryMarker(it)
            }
        }
        getLocation()
        setMapStyle()
    }

    private fun addStoryMarker(stories: List<Stories>) {
        val boundsBuilder = LatLngBounds.Builder()
        stories.forEach{ user ->
            val latLng = LatLng(user.lat, user.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(user.name).snippet(user.description)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            boundsBuilder.include(latLng)
        }
        val bounds : LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                30
            )
        )
    }

    private fun checkPermission(permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation(){
        if(checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocation.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showMarker(location)
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            }
        }

    private fun showMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions().position(startLocation).title(getString(R.string.my_position))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15f))
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}