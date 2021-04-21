 package com.kakadurf.catlas.presentation.map_maintaining

import android.content.Context
import android.graphics.Color
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
import com.kakadurf.catlas.data.http.helper.MAP_REGION_STROKE
import com.kakadurf.catlas.data.http.helper.MAP_SELECTED_COLOUR
import org.json.JSONObject

 class MapMaintainingServiceImpl(private val mMap: GoogleMap, var context: Context) :
     MapMaintainingService {

     /*private val selectedStyle = GeoJsonPolygonStyle().apply {
         fillColor = Color.parseColor(MAP_SELECTED_COLOUR)
         strokeColor = Color.WHITE
         strokeWidth = MAP_REGION_STROKE
     }
     private var currentLand: GeoJsonFeature? = null*/
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
         mMap.setMapStyle(
                 MapStyleOptions.loadRawResourceStyle(
                         context, R.raw.map_style
                 )
         )
     }
     override fun addLayer(json: JSONObject) {
         val layer = GeoJsonLayer(
             mMap, json,
             markerManager, polygonManager, polylineManager, groundOverlayManager
         )

         val defaultStyle = layer.defaultPolygonStyle.apply {
             fillColor = Color.parseColor(MAP_SELECTED_COLOUR)
             strokeColor = Color.WHITE
             strokeWidth = MAP_REGION_STROKE
         }
         layer.setOnFeatureClickListener {
             onClickListener(it)
         }
         /*layer.setOnFeatureClickListener {
             onClickListener(it)
             currentLand?.polygonStyle = defaultStyle
             currentLand = (it as? GeoJsonFeature)?.apply {
                 polygonStyle = selectedStyle
             }
         }*/
         layer.addLayerToMap()
         //###
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

     fun setOnClickListener(onClickListener: (Feature) -> Unit) {
         this.onClickListener = onClickListener
     }
 }