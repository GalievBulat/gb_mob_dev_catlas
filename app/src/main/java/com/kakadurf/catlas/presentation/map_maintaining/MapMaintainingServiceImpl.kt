 package com.kakadurf.catlas.presentation.map_maintaining

import android.content.Context
import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import com.kakadurf.catlas.R
import com.kakadurf.catlas.data.http.helper.MAP_REGION_STROKE
import com.kakadurf.catlas.data.http.helper.MAP_SELECTED_COLOUR
import org.json.JSONObject

 class MapMaintainingServiceImpl(private val mMap: GoogleMap, var context: Context) :
     MapMaintainingService {
     fun setOnClickListener(onClickListener: (Feature) -> Unit) {
         this.onClickListener = onClickListener
     }

     private val selectedStyle = GeoJsonPolygonStyle().apply {
         fillColor = Color.parseColor(MAP_SELECTED_COLOUR)
         strokeColor = Color.WHITE
         strokeWidth = MAP_REGION_STROKE
     }
     private var currentLand: GeoJsonFeature? = null
     private var onClickListener: (Feature) -> Unit = {}

     private val markerManager = MarkerManager(mMap)
     private val groundOverlayManager = GroundOverlayManager(mMap)
     private val polygonManager = PolygonManager(mMap)
     private val polylineManager = PolylineManager(mMap)

     init {
         mMap.uiSettings.isZoomControlsEnabled = true
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
             strokeColor = Color.BLACK
             strokeWidth = MAP_REGION_STROKE
         }
         layer.setOnFeatureClickListener {
             onClickListener(it)
             currentLand?.polygonStyle = defaultStyle
             currentLand = (it as? GeoJsonFeature)?.apply {
                 polygonStyle = selectedStyle
             }
         }
         layer.addLayerToMap()
         //###
         layer.features.firstOrNull()?.boundingBox?.center?.let {
             mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
         }
     }

     override fun clearMap() {
         mMap.clear()
     }
 }