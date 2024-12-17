package com.example.mystoryappsubmission.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.mystoryappsubmission.R
import com.example.mystoryappsubmission.databinding.FragmentMapsBinding
import com.example.mystoryappsubmission.ui.ViewModelFactory
import com.example.storyappsubmission.data.response.ListStoryItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var Map: GoogleMap
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // Memperbaiki error dengan menggunakan requireActivity().menuInflater.inflate
        requireActivity().menuInflater.inflate(R.menu.map_options, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                Map.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                Map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                Map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                Map.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Map = googleMap


        Map.uiSettings.isCompassEnabled = true


        val javaIsland = LatLng(-7.536064, 110.712246)
        Map.moveCamera(CameraUpdateFactory.newLatLngZoom(javaIsland, 7f))

        getMyLocation()

        viewModel.getUser().observe(viewLifecycleOwner) { data ->
            data?.let {
                val token = data.token
                if (token.isNotEmpty()) {
                    viewModel.getStories(token = data.token)
                    viewModel.stories.observe(viewLifecycleOwner) { stories ->
                        updateUserLocation(stories)
                    }
                }
            }
        }
    }


    private fun updateUserLocation(listStoryItems: List<ListStoryItem>?) {
        val boundsBuilder = LatLngBounds.Builder()

        listStoryItems?.forEach { story ->
            val lat = story.lat ?: return@forEach
            val lon = story.lon ?: return@forEach

            val markerOptions = MarkerOptions()
                .position(LatLng(lat, lon))
                .title(story.name)
                .snippet(story.description)
            Map.addMarker(markerOptions)

            boundsBuilder.include(LatLng(lat, lon))
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Map.isMyLocationEnabled = true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Izin lokasi diperlukan untuk fitur ini.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}





