package com.kakadurf.catlas.data.timeline.http.openstreetmap

import javax.inject.Inject

class RegionFetching @Inject constructor(
    val service: RegionHttpRetriever,
    // val cachingService: CachingService
) {
    suspend fun getGeometries(name: String): String {
        // cachingService

        return if (!name.contains("Britain")) service.searchForARegion(name)
            .charStream()
            .readText() else "{\"type\":\"FeatureCollection\",\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"place_id\":258268651,\"osm_type\":\"relation\",\"osm_id\":62149,\"display_name\":\"Великобритания\",\"place_rank\":4,\"category\":\"boundary\",\"type\":\"administrative\",\"importance\":0.8723780132837334,\"icon\":\"https://nominatim.openstreetmap.org/ui/mapicons//poi_boundary_administrative.p.20.png\"},\"bbox\":[-14.015517,49.695,2.0909314,61.041],\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-14.015517,57.6026322],[-13.7179419,57.4213286],[-13.3609679,57.5937691],[-13.6742834,57.7730825],[-14.015517,57.6026322]]],[[[-9.0234247,57.829],[-8.4704675,57.594],[-8.1022246,57.873],[-8.5331216,58.082],[-9.0234247,57.829]]],[[[-8.1775099,54.4647612],[-7.5473753,54.1221425],[-7.2789457,54.1225548],[-6.9816489,54.4094617],[-6.6238101,54.0364597],[-6.2847299,54.112224],[-5.826004,53.8701676],[-5.2677592,54.174],[-5.0777816,54.4645486],[-4.153469,54.5506519],[-3.3917274,53.8697263],[-3.4402166,53.547],[-4.6646014,53.621],[-5.0052832,53.389],[-4.8118632,53.085],[-5.1106143,52.675],[-4.4539835,52.582],[-4.4143324,52.45],[-5.2718937,52.189],[-5.7773936,51.815],[-5.7378356,51.606],[-5.3803492,51.5023917],[-5.3781003,50.543],[-6.0462888,50.114],[-6.6180506,50.087],[-6.7001177,49.913],[-6.5097183,49.695],[-6.1843044,49.71],[-5.9395258,49.909],[-5.1015274,49.769],[-4.4374942,50.134],[-3.6174848,50.013],[-3.1556735,50.453],[-2.8714952,50.517],[-2.4521988,50.313],[-1.6990258,50.4743478],[-1.1848442,50.385],[-0.2361009,50.626],[0.3459442,50.545],[1.1541993,50.746],[1.8875556,51.1993611],[1.6732673,51.528],[1.2852703,51.584],[1.8902408,52.024],[2.0909314,52.467],[1.9955572,52.81],[1.4589347,53.11],[0.6889586,53.1889735],[0.1660577,53.972],[0.2236768,54.212],[-0.9754801,54.862],[-1.2856928,55.73],[-2.1642484,56.2758307],[-2.026576,56.4176676],[-2.1218263,56.591],[-1.4126114,57.42],[-1.4790481,57.689],[-1.8822704,57.889],[-3.0547803,57.885],[-3.2541721,58.0236106],[-2.7655964,58.285],[-1.9787836,59.397],[-1.3098582,59.393],[-1.2634363,59.654],[-0.9411211,59.744],[-0.3522503,60.3486985],[-0.3577917,60.86],[-0.6417069,61.027],[-1.0514547,61.041],[-1.7061669,60.7771917],[-2.5043845,60.194],[-2.2744024,59.94],[-1.776096,59.9708061],[-1.6736123,59.7529494],[-2.0517401,59.504],[-3.3263499,59.484],[-3.7064736,59.205],[-3.801226,58.807],[-6.3281346,58.7161537],[-7.2478567,58.407],[-7.9790497,58.386],[-7.983712,58.183],[-7.6400315,58.0748074],[-8.0948678,57.694948],[-7.8442708,57.210549],[-8.012301,56.735],[-7.451861,56.1639097],[-6.9677946,56.0197571],[-6.8647299,55.6217767],[-6.5787357,55.4435849],[-7.3913226,55.0223489],[-7.5346934,54.7470212],[-7.9208904,54.6959641],[-7.6972521,54.6100698],[-8.1775099,54.4647612]]],[[[-6.5600264,59.102],[-6.1530746,58.888],[-5.6081412,58.943],[-5.419679,59.117],[-5.8313334,59.335],[-6.3762581,59.272],[-6.5600264,59.102]]],[[[-4.8966569,59.025],[-4.4520511,58.825],[-4.0110607,59.097],[-4.4657763,59.287],[-4.8966569,59.025]]]]}}]}"
    }
}