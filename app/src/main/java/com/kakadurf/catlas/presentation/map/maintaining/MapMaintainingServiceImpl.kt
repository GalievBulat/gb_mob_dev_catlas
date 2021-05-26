package com.kakadurf.catlas.presentation.map.maintaining

import android.graphics.Color
import androidx.annotation.MainThread
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.kakadurf.catlas.R
import org.json.JSONObject

const val MAP_SELECTED_COLOUR = "#04151F"
const val MAP_REGION_STROKE = 1F

class MapMaintainingServiceImpl(
    private val mMap: GoogleMap,
    mapStyle: MapStyleOptions
) :
    MapMaintainingService {

    private var onClickListener: (Feature) -> Unit = {}
    private val markerManager = MarkerManager(mMap)
    private val groundOverlayManager = GroundOverlayManager(mMap)
    private val polygonManager = PolygonManager(mMap)
    private val polylineManager = PolylineManager(mMap)

    init {
        with(mMap.uiSettings) {
            isMapToolbarEnabled = false
            isCompassEnabled = false
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = false
            isMyLocationButtonEnabled = false
            isRotateGesturesEnabled = false
        }
        mMap.setMapStyle(mapStyle)
    }

    @MainThread
    override fun addLayer(json: JSONObject) {
        val layer = GeoJsonLayer(
            mMap,
            json,
            markerManager,
            polygonManager,
            polylineManager,
            groundOverlayManager
        )
        layer.defaultPolygonStyle.apply {
            fillColor = Color.parseColor(MAP_SELECTED_COLOUR)
            strokeColor = Color.WHITE
            strokeWidth = MAP_REGION_STROKE
        }
        layer.setOnFeatureClickListener {
            onClickListener(it)
        }
        layer.addLayerToMap()
        layer.features.firstOrNull()?.run {
            this.pointStyle.icon = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_launcher_foreground)
            /*boundingBox?.center?.let {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            }*/
            boundingBox?.let {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(it, 0))
            }
        }
    }

    override fun clearMap() {
        mMap.clear()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 2F))
    }

    override fun setOnClickListener(onClickListener: (Feature) -> Unit) {
        this.onClickListener = onClickListener
    }
}
