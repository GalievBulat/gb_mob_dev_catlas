 package com.kakadurf.catlas.presentation.map_maintaining

import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import com.kakadurf.catlas.presentation.helpers.LoggingHelper.Companion.sout
import org.json.JSONObject

 class MapMaintainingServiceImpl(private val mMap: GoogleMap) : MapMaintainingService {
     fun setOnClickListener(onClickListener: (Feature) -> Unit) {
         this.onClickListener = onClickListener
     }

     private val selectedStyle = GeoJsonPolygonStyle().apply {
         fillColor = Color.parseColor("#04151F")
         strokeColor = Color.WHITE
         strokeWidth = 1F
     }
     private var currentLand: GeoJsonFeature? = null
     private var onClickListener: (Feature) -> Unit = {}

     private val markerManager = MarkerManager(mMap)
     private val groundOverlayManager = GroundOverlayManager(mMap)
     private val polygonManager = PolygonManager(mMap)
     private val polylineManager = PolylineManager(mMap)
     fun configureMap() {
         mMap.uiSettings.isZoomControlsEnabled = true
         //mMap.mapType = GoogleMap.MAP_TYPE_NONE
         mMap.setOnPolygonClickListener {
             sout(it.tag.toString())
         }
         val sydney = LatLng(-34.0, 151.0)
         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
     }

     fun addLayer(json: JSONObject) {
         val layer = GeoJsonLayer(
             mMap, json,
             markerManager, polygonManager, polylineManager, groundOverlayManager
         )

         val defaultStyle = layer.defaultPolygonStyle.apply {
             strokeColor = Color.BLACK
             strokeWidth = 1F
         }
         layer.setOnFeatureClickListener {
             onClickListener(it)
             currentLand?.polygonStyle = defaultStyle
             currentLand = (it as? GeoJsonFeature)?.apply {
                 polygonStyle = selectedStyle
             }
         }
         layer.addLayerToMap()
     }

     fun clearMap() {
         mMap.clear()
     }
 }