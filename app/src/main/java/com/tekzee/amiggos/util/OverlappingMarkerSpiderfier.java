//package com.tekzee.amiggos.util;
//
//import java.util.*;
//
//import android.graphics.Color;
//import android.graphics.Point;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.Projection;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.Polyline;
//import com.google.android.gms.maps.model.PolylineOptions;
//
//public class OverlappingMarkerSpiderfier {
//
//    /** Corresponds to line 12 of original code*/
//    private static final String VERSION = "0.3.3"; //version of original code.
//    private static final String LOGTAG = "MarkerSpiderfier";
//    private static final double TWO_PI = Math.PI * 2;
//    private static final double RADIUS_SCALE_FACTOR = 10; // TODO: needs to be computed according to the device px density & zoom lvl
//    public static final double SPIRAL_ANGLE_STEP = 0.2; //in radians
//
//    // Passable params' names
//    private static final String ARG_KEEP_SPIDERFIED = "keepSpiderfied";
//    private static final String ARG_MARK_WONT_HIDE  = "markersWontHide";
//    private static final String ARG_MARK_WONT_MOVE  = "markersWontMove";
//    private static final String ARG_NEARBY_DISTANCE = "nearbyDistance";
//    private static final String ARG_CS_SWITCHOVER   = "circleSpiralSwitchover";
//    private static final String ARG_LEG_WEIGHT      = "legWeight";
//
//    private GoogleMap gm;
////  ge = gm.event // Found in original code; however this is simpler in android, as events aren't "general"...
//    private int mt; // map type
//    private Projection proj;
//
//    /////// START OF PASSABLE PARAMS ///////
//
//    /** By default, the OverlappingMarkerSpiderfier works like Google Earth, in that when you click a spiderfied marker,
//     * the markers unspiderfy before any other action takes place.
//     * Since this can make it tricky for the user to work through a set of markers one by one, you can override this
//     * behaviour by setting the keepSpiderfied option to true. */
//    private boolean keepSpiderfied = false;
//
//    /** By default, change events for each added marker???s position and visibility are observed (so that, if a
//     * spiderfied marker is moved or hidden, all spiderfied markers are unspiderfied, and the new position is respected
//     * where applicable). However, if you know that you won???t be moving and/or hiding any of the markers you add to
//     * this instance, you can save memory (a closure per marker in each case) by setting the options named
//     * markersWontMove and/or markersWontHide to true. */
//    private boolean markersWontHide = false;
//    private boolean markersWontMove = false;
//
//    /** nearbyDistance is the pixel radius within which a marker is considered to be overlapping a clicked marker. */
//    private int nearbyDistance = 20; //this is important when spiderfying unclustered markers.. I think..
//
//    /** circleSpiralSwitchover is the lowest number of markers that will be fanned out into a spiral instead of a circle.
//     0 -> always spiral; Infinity -> always circle. */
//    private int circleSpiralSwitchover = 9; // show spiral instead of circle from this marker count upwards
//                                            //
//    /** This determines the thickness of the lines joining spiderfied markers to their original locations.
//     *  The Android default for Polylines is 10F */
//    private float legWeight = 3F;
//
//    /////// END OF PASSABLE PARAMS ////////
//
//    private int circleFootSeparation = 23; // related to circumference of circles
//    private double circleStartAngle = TWO_PI / 12;
//    private int spiralFootSeparation = 26; // related to size of spiral (experiment!)
//    private int spiralLengthStart = 11;    // ditto
//    private int spiralLengthFactor = 4;    // ditto
//
//    private int spiderfiedZIndex = 1000;   // ensure spiderfied markersInCluster are on top
//    private int usualLegZIndex = 0;       // for legs
//    private int highlightedLegZIndex = 20; // ensure highlighted leg is always on top
//
//    private class _omsData{
//        private LatLng usualPosition;
//        private Polyline leg;
//
//        public LatLng getUsualPosition() {
//            return usualPosition;
//        }
//
//        public Polyline getLeg() {
//            return leg;
//        }
//
//        public _omsData leg(Polyline newLeg){
//            if(leg!=null)
//                leg.remove();
//            leg=newLeg;
//            return this; // return self, for chaining
//        }
//
//        public _omsData usualPosition(LatLng newUsualPos){
//            usualPosition=newUsualPos;
//            return this; // return self, for chaining
//        }
//
//    }
//
//    private class MarkerData{
//        public Marker marker;
//        public Point markerPt;
//        public boolean willSpiderfy = false;
//        public MarkerData(Marker mark, Point pt){
//            marker = mark;
//            markerPt = pt;
//        }
//        public MarkerData(Marker mark, Point pt, boolean spiderfication){
//            marker = mark;
//            markerPt = pt;
//            willSpiderfy = spiderfication;
//        }
//    }
//
//    private class LegColor{
//
//        private final int type_satellite;
//        private final int type_normal; // in the javascript version this is known as "roadmap"
//
//        public LegColor(int set, int road){
//            type_satellite = set;
//            type_normal = road;
//        }
//
//        public LegColor(String set, String road){
//            type_satellite = Color.parseColor(set);
//            type_normal = Color.parseColor(road);
//        }
//
//        public int getType_satellite() {
//            return type_satellite;
//        }
//
//        public int getType_normal() {
//            return type_normal;
//        }
//    }
//
//    public final LegColor usual       = new LegColor(0xAAFFFFFF,0xAA0F0F0F);
//    public final LegColor highlighted = new LegColor(0xAAFF0000,0xAAFF0000);
//
//    /** Corresponds to line 292 of original code */
///*  // TODO Class seems unnecessary
//    private class ProjHelper { // moved here from the end of the file
//        private GoogleMap googM;
//        public ProjHelper(GoogleMap gm){
//            googM = gm;
//        }
//        public Projection getProjection
//        public void draw(){
//            // dummy function (?!)
//        }
//    }*/
//
//    //the following lists are initialized later
//    private List<Marker> markersInCluster; // refers to the current clicked cluster
//    private List<Marker> displayedMarkers;
//    private List<Marker> spiderfiedClusters; // as the name suggests
//    private List<Marker> spiderfiedUnclusteredMarkers; // intended to hold makers that were tightly packed but not clustered before spiderfying
//
//    private boolean spiderfying = false;   //needed for recursive spiderfication
//    private boolean unspiderfying = false; // TODO: check if needed for recursive unspiderfication
//    private boolean isAnythingSpiderfied = false;
//    private float zoomLevelOnLastSpiderfy;
//
//    private HashMap<Marker,_omsData> omsData= new HashMap<Marker, _omsData>();
//    private HashMap<Marker,Boolean> spiderfyable = new HashMap<Marker, Boolean>();
//
//    ////////////////////////////////////////////////////////////////////////////////////
//    /////////////////////////////  METHODS BEGIN HERE  /////////////////////////////////
//    ////////////////////////////////////////////////////////////////////////////////////
//
//    /** Corresponds to line 53 of original code*/
//    public OverlappingMarkerSpiderfier(GoogleMap gm, Object ... varArgs) throws IllegalArgumentException {
//        this.gm=gm;
//        mt = gm.getMapType();
////      ProjHelper projHelper = new ProjHelper(gm);
//        if (varArgs.length > 0)
//            assignVarArgs(varArgs);
//        initMarkerArrays();
//
//        // Listeners:
//        gm.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                if(spiderfiedClusters.size()>0 && cameraPosition.zoom != zoomLevelOnLastSpiderfy)
//                    unspiderfyAll();
//            }
//        });
//    }
//
//    /** Corresponds to line 61 of original code*/
//    private void initMarkerArrays(){ //TODO: call on unspiderfyall?
//        markersInCluster = new ArrayList<Marker>();
//        displayedMarkers = new ArrayList<Marker>();
//        spiderfiedClusters = new ArrayList<Marker>();
//        spiderfiedUnclusteredMarkers = new ArrayList<Marker>();
//    }
//
//    /** Corresponds to line 65 of original code
//     *  Adds marker to be tracked.*/ /*
//    private Marker addMarker(Marker marker){
//        if(!isSpiderfyalbe(marker)){
////          markerListenerRefs = [ge.addListener(marker, 'click', (event) => @spiderListener(marker, event))]
//            if (!markersWontHide){
//            //markerListenerRefs.push(ge.addListener(marker, 'visible_changed', => @markerChangeListener(marker, no)))
//                markerChangeListener(marker,"visible_changed",false);
//            }
//            if (!markersWontMove){
//            //markerListenerRefs.push(ge.addListener(marker, 'position_changed', => @markerChangeListener(marker, yes)))
//                markerChangeListener(marker,"position_changed",false);
//            }
//            markersInCluster.add(marker);
//            setSpiderfyalbe(marker, true);
//        }
//        return marker;
//    }
//*/
//    /** Corresponds to line 77 of original code*//*
//    private void markerChangeListener(Marker marker,String action, boolean positionChanged){
//        if (omsData.containsKey(marker) && (positionChanged || !marker.isVisible()) && !(spiderfying || unspiderfying) ){
//            unspiderfy(positionChanged ? marker : null);
//        }
//    }
//*/
//    /** Corresponds to line 81 of original code*//*
//    private List<Marker> getMarkersInCluster(){
//        //  Meant to return a COPY of the original markersInCluster' list; Currently, this is a SHALLOW copy and might cause problems.
//        List<Marker> markersCopy = new ArrayList<Marker>(markersInCluster);
//        return markersCopy;
//    }
//*/
//    /** Corresponds to line 83 of original code
//     * Removes marker from those being tracked. This does not remove the marker from the map (to remove a marker from
//     * the map you must call setMap(null) on it, as per usual). *//*
//    private void removeMarker(Marker marker){
//        if (omsData.containsKey(marker))
//            unspiderfy(marker);
//        int i = markersInCluster.indexOf(marker);
//        if (i>=0){
//            // Remove listeners
//                //TODO?
//            markersInCluster.remove(i);
//        }
//    }
//*/
//    /** Corresponds to line 93 of original code
//     * Removes every marker from being tracked. Much quicker than calling removeMarker in a loop, since that has
//     * to search the markers array every time. This does not remove the markers from the map (to remove the markers
//     * from the map you must call setMap(null) on each of them, as per usual).*//*
//    private void clearMarkers(){
//        for (Marker marker : markersInCluster) {
//            // Remove listeners (methinks this is redundant in java due to "garbage collection")
//            unspiderfy(marker);
//        }
//        initMarkerArrays();
//    }
//*/
//    /**
//     * Lines 102-117 in original code seems irrelevant in java
//     */
//
//    /** Corresponds to line 119 of original code
//    // available listeners: click(marker), spiderfy(markersInCluster), unspiderfy(markersInCluster) */
//    private List<Point> generatePtsCircle (int count, Point centerPt){
//        int circumference = circleFootSeparation * ( 2 + count);
//        double legLength = circumference / TWO_PI * RADIUS_SCALE_FACTOR; // = radius from circumference
//        double angleStep = TWO_PI / count;
//        double angle;
//        List<Point> points = new ArrayList<Point>(count);
//        for (int ind = 0; ind < count; ind++) {
//            angle = circleStartAngle + ind * angleStep;
//            points.add(new Point((int)(centerPt.x + legLength * Math.cos(angle)),(int)(centerPt.y + legLength * Math.sin(angle))));
//        }
//        return points;
//    }
//
//    /** Corresponds to line 128 of original code*/
//    private List<Point> generatePtsSpiral (int count, Point centerPt){
//        double legLength = spiralLengthStart * RADIUS_SCALE_FACTOR;
//        double angle = 0;
//        List<Point> points = new ArrayList<Point>(count);
//        for (int ind = 0; ind < count; ind++) {
//            angle += spiralFootSeparation / legLength + ind * SPIRAL_ANGLE_STEP;
//            points.add(new Point((int)(centerPt.x + legLength * Math.cos(angle)),(int)(centerPt.y + legLength * Math.sin(angle))));
//            legLength += TWO_PI * spiralLengthFactor / angle;
//        }
//        return points;
//    }
//
//    public void spiderListener(Marker cluster){ /** Corresponds to line 138 of original code*/
//
//        if (isAnythingSpiderfied && !spiderfying){ // unspiderfy everything before spiderfying anything new
//            unspiderfyAll();
//        }
//        List<MarkerData> closeMarkers = new ArrayList<MarkerData>();
//        List<MarkerData> displayedFarMarkers   = new ArrayList<MarkerData>();
//        int nDist = nearbyDistance;
//        int pxSq = nDist * nDist;
//        Point mPt, markerPt = llToPt(cluster.getPosition());
//        List<Marker> tmpMarkersInCluster = new ArrayList<Marker>();
//        tmpMarkersInCluster.addAll(cluster.getMarkers());
//        markersInCluster.addAll(cluster.getMarkers());
//        /*
//        if (!spiderfying)
//            displayedMarkers = gm.getDisplayedMarkers(); //could be very slow
//        List<Marker> markersToConsider = new ArrayList<Marker>();
//        markersToConsider.addAll(displayedMarkers); markersToConsider.addAll(markersInCluster);
//        LatLngBounds llb = gm.getProjection().getVisibleRegion().latLngBounds;
//        for (Marker markers_item : markersToConsider) {
//            if(!llb.contains(markers_item.getPosition()) || markers_item == cluster)
//                continue;
//            mPt = proj.toScreenLocation(markers_item.getPosition());
//            if (ptDistanceSq(mPt,markerPt) < pxSq)
//                closeMarkers.add(new MarkerData(markers_item,mPt));
//            else
//                displayedFarMarkers.add(new MarkerData(markers_item,mPt));
//        }
//        if (closeMarkers.size() > 0) {// 0 => only the one clicked is displayed => none nearby
//            //TODO Probably missing something here
//        }
//        */
//
//        for (Marker markers_item : tmpMarkersInCluster) {
//            if (markers_item.isCluster()) {
//                recursiveAddMarkersToSpiderfy(markers_item);
//            }
//            mPt = proj.toScreenLocation(markers_item.getPosition());
//            if (ptDistanceSq(mPt,markerPt) < pxSq)
//                closeMarkers.add(new MarkerData(markers_item,mPt));
//            else
//                displayedFarMarkers.add(new MarkerData(markers_item,mPt));
//        }
//
//        spiderfy(closeMarkers,displayedFarMarkers);
//        spiderfiedClusters.add(cluster);
//        zoomLevelOnLastSpiderfy = gm.getCameraPosition().zoom;
//    }
//
//    private void recursiveAddMarkersToSpiderfy(Marker markers_item) {
//        List<Marker> nestedMarkers = markers_item.getMarkers();
//        for (Marker nestedMarker : nestedMarkers) {
//            if (!nestedMarker.isCluster()) // inception.... (cluster within a cluster within a cl.....)
//                tryAddMarker(markersInCluster,nestedMarker);
//            else
//                recursiveAddMarkersToSpiderfy(markers_item);
//        }
//    }
//
//    /** Corresponds to line 161 of original code.
//     * Returns an array of markers within nearbyDistance pixels of marker ??? i.e. those that will be spiderfied when
//     * marker is clicked.
//     * - *Don???t* call this method in a loop over all your markers, since this can take a very long time.
//     * - The return value of this method may change any time the zoom level changes, and when any marker is added,
//     *   moved, hidden or removed. Hence you???ll very likely want call it (and take appropriate action) every time the
//     *   map???s zoom_changed event fires and any time you add, move, hide or remove a marker.
//     * - Note also that this method relies on the map???s Projection object being available, and thus cannot be called
//     *   until the map???s first idle event fires. *//*
//    private List<Marker> markersNearMarker(Marker marker) {
//        int nDist = nearbyDistance;
//        int pxSq = nDist * nDist;
//        Point markerPt = proj.toScreenLocation(marker.getPosition()); //using android maps api instead of llToPt and ptToLl
//        Point mPt;
//        List<Marker> markersNearMarker = new ArrayList<Marker>();
//        for (Marker markers_item : markersInCluster) {
//            if(!markers_item.isVisible() /* || markers_item instanceof Marker || (markers_item.map != null) * /){
//                //no idea whether check for the rest of the conditions; remove space in "* /" if uncommenting block.
//                continue;
//            }
//            mPt = proj.toScreenLocation(omsData.containsKey(marker) ? omsData.get(markers_item).usualPosition : markers_item.getPosition());
//            if (ptDistanceSq(mPt,markerPt) < pxSq){
//                markersNearMarker.add(markers_item);
//                if (firstonly){
//                    firstonly = false;
//                    break;
//                }
//            }
//        }
//        return markersNearMarker;
//    }
//*/
//    /**
//     * Corresponds to line 176 of original code
//     *
//     * Returns an array of all markers that are near one or more other markersInCluster ??? i.e. those will be
//     * spiderfied when clicked.
//     * This method is several orders of magnitude faster than looping over all markersInCluster calling markersNearMarker
//     * (primarily because it only does the expensive business of converting lat/lons to pixel coordinates once per marker).
//     *
//     * @param marker
//     * @return
//     */
///*
//    private List<Marker> markersNearAnyOtherMarker(Marker marker){
//        try {waitForMapIdle();} catch (InterruptedException e){}
//        int nDist = nearbyDistance;
//        int pxSq = nDist * nDist;
//        int numMarkers = markersInCluster.size();
//        List<MarkerData> nearbyMarkerData = new ArrayList<MarkerData>(numMarkers);
//        Point pt;
//        for (Marker markers_item : markersInCluster) {
//            pt = proj.toScreenLocation(omsData.containsKey(marker) ? omsData.get(markers_item).usualPosition : markers_item.getPosition());
//            nearbyMarkerData.add(new MarkerData(markers_item,pt,false));
//        }
//        Marker m1, m2;
//        MarkerData m1Data, m2Data;
//        for (int i1=0; i1 < numMarkers; i1++) {
//            m1 = markersInCluster.get(i1);
//            if(!m1.isVisible() /* || (markers_item.map != null) * /){ //remove space from "* /" if uncommenting block
//                continue;
//            }
//            m1Data = nearbyMarkerData.get(i1);
//            if(m1Data.willSpiderfy)
//                continue;
//            for(int i2=0; i2 < numMarkers; i2++){
//                m2 = markersInCluster.get(i2);
//                if(i2==i1 || !m2.isVisible())
//                    continue;
//                m2Data = nearbyMarkerData.get(i2);
//                if(i2 < i1 && !m2Data.willSpiderfy)
//                    continue;
//                if(ptDistanceSq(m1Data.markerPt,m2Data.markerPt) < pxSq){
//                    m1Data.willSpiderfy=true;
//                    m2Data.willSpiderfy=true;
//                    break;
//                }
//            }
//        }
//        ArrayList<Marker> toSpiderfy = new ArrayList<Marker>(numMarkers);
//        for (int i=0; i < numMarkers; i++) {
//            if (nearbyMarkerData.get(i).willSpiderfy)
//                toSpiderfy.add(nearbyMarkerData.get(i).marker);
//        }
//        return toSpiderfy;
//    }
//*/
//    /** Corresponds to line 198 of original code.*/
///*    private Marker highlight (Marker marker){
//        _omsData data = omsData.get(marker);
//        switch (gm.getMapType()){
//            case GoogleMap.MAP_TYPE_NORMAL:
//                data.leg.setColor(highlighted.getType_normal());
//                data.leg.setZIndex(highlightedLegZIndex);
//                break;
//            case GoogleMap.MAP_TYPE_SATELLITE:
//                data.leg.setColor(highlighted.getType_satellite());
//                data.leg.setZIndex(highlightedLegZIndex);
//                break;
//            default:
//                throw new IllegalArgumentException("Passed array is empty.");
//        }
//        return marker;
//    }
//*/
//    /** Corresponds to line 202 of original code*//*
//    private void unhighlight (Marker marker){
//        _omsData data = omsData.get(marker);
//        switch (gm.getMapType()){
//            case GoogleMap.MAP_TYPE_NORMAL:
//                data.leg.setColor(usual.getType_normal());
//                data.leg.setZIndex(usualLegZIndex);
//                break;
//            case GoogleMap.MAP_TYPE_SATELLITE:
//                data.leg.setColor(usual.getType_satellite());
//                data.leg.setZIndex(usualLegZIndex);
//                break;
//            default:
//                throw new IllegalArgumentException("Passed array is empty.");
//        }
//    }
//*/
//    /** Corresponds to line 207 of original code*/
//    // renamed original inputs as follows: markersData => clusteredMarkersData; nonNearbyMarkers => nearbyMarkers
//    private void spiderfy(List<MarkerData> clusteredMarkersData,List<MarkerData> nearbyMarkers){
////        List<MarkerData> listToUse;
////        if ( clusteredMarkersData.size() == 0 && markersInCluster.size() > 0){ //could happen when GoogleMap.clusterSize is too large
//////            listToUse = nearbyMarkers;
////            listToUse = new ArrayList<MarkerData>();
////            listToUse.addAll(nearbyMarkers);
////            for (MarkerData markerData : nearbyMarkers) {
////                if (markerData.marker.isCluster()){
//////                  spiderfiedClusters.add(markerData.marker);
////                    listToUse.remove(markerData);
////                }
////            }
////        }
////        else
////            listToUse=clusteredMarkersData;
//        List<MarkerData> listToUse = new ArrayList<MarkerData>();
//        listToUse.addAll(clusteredMarkersData); listToUse.addAll(nearbyMarkers); //could be terrible... :P
//        spiderfying = true;
//        int numFeet = listToUse.size(); // numFeet is representative of the amount of markers in the cluster
//        // Compute the positions of the clustered markers after spiderfication
//        List<Point> nearbyMarkerPts = new ArrayList<Point>(numFeet);
//        for (MarkerData markerData : listToUse) {
//            nearbyMarkerPts.add(markerData.markerPt);
//        }
//        Point bodyPt = ptAverage(nearbyMarkerPts);
//        List<Point> footPts;
//        if (numFeet >= circleSpiralSwitchover){
//            footPts=generatePtsSpiral(numFeet,bodyPt);
//            Collections.reverse(footPts);
//        }
//        else
//            footPts=generatePtsCircle(numFeet,bodyPt);
//
//        for (int ind =0; ind < numFeet; ind++){
//            Point footPt = footPts.get(ind);
//            LatLng footLl = ptToLl(footPt);
//            MarkerData nearestMarkerData = listToUse.get(ind);
//            Marker clusterNearestMarker = nearestMarkerData.marker;
//            Polyline leg = gm.addPolyline(new PolylineOptions()
//                    .add(clusterNearestMarker.getPosition(), footLl)
//                    .color(usual.getType_normal())
//                    .width(legWeight)
//                    .zIndex(usualLegZIndex));
//            omsData.put(clusterNearestMarker,new _omsData()
//                    .leg(leg)
//                    .usualPosition(clusterNearestMarker.getPosition()));
//            // lines 228-233 in original code seem irrelevant in java
//            clusterNearestMarker.setClusterGroup(ClusterGroup.NOT_CLUSTERED);
//            clusterNearestMarker.animatePosition(footLl);
//            // set clusterNearestMarker zIndex is unavailable in android :\
//
//            spiderfiedUnclusteredMarkers.add(clusterNearestMarker);
//
//        }
//        isAnythingSpiderfied=true;
//        spiderfying=false;
//        //  trigger("spiderfy",spiderfiedUnclusteredMarkers,nearbyMarkers); // ?! Todo: find an idea what to do with this.
//    }
//
//    /**
//     * Corresponds to line 241 of original code
//     *
//     * Returns any spiderfied markers to their original positions, and triggers any listeners you may have set for this event.
//     * Unless no markersInCluster are spiderfied, in which case it does nothing.
//     */
//    private Marker unspiderfy(Marker markerToUnspiderfy){ //241
//        // this function has to return everything to its original state
//        if (markerToUnspiderfy!=null){ //Todo: make sure that this "if" is needed at all
//            unspiderfying=true;
//            if(markerToUnspiderfy.isCluster()){
//                List<Marker> unspiderfiedMarkers = new ArrayList<Marker>(), nonNearbyMarkers = new ArrayList<Marker>();
//                for (Marker marker : markersInCluster) {
//                    if(omsData.containsKey(marker)){// ignoring the possibility that (params.markerNotToMove != null)
//                        marker.setPosition(omsData.get(marker).leg(null).getUsualPosition());
//                        marker.setClusterGroup(ClusterGroup.DEFAULT);
//                        //skipped lines 250-254 from original code
//                        unspiderfiedMarkers.add(marker);
//                    } else
//                        nonNearbyMarkers.add(marker);
//                }
//            } else { // if a regular (non-cluster) marker
//                markerToUnspiderfy.setPosition(omsData.get(markerToUnspiderfy).leg(null).getUsualPosition());
//                markerToUnspiderfy.setClusterGroup(ClusterGroup.DEFAULT);
//                    //skipped lines 250-254 from original code
//                // spiderfiedUnclusteredMarkers.remove(markerToUnspiderfy); <== will be done by the calling function
//            }
//            unspiderfying=false;
//        }
//        return markerToUnspiderfy; // return self, for chaining
//    }
//
//    private int ptDistanceSq(Point pt1, Point pt2){ /** Corresponds to line 264 of original code*/
//        int dx = pt1.x - pt2.x;
//        int dy = pt1.y - pt2.y;
//        return (dx * dx + dy * dy);
//    }
//
//    private Point ptAverage(List<Point> pts){ /** Corresponds to line 269 of original code*/
//        int sumX=0, sumY=0, numPts=pts.size();
//        for (Point pt : pts) {
//            sumX += pt.x;
//            sumY += pt.y;
//        }
//        return new Point(sumX / numPts,sumY / numPts);
//    }
//
//
//    private Point llToPt(LatLng ll) { /** Corresponds to line 276 of original code*/
//        proj = gm.getProjection();
//        return proj.toScreenLocation(ll);   // the android maps api equivalent
//    }
//
//    private LatLng ptToLl(Point pt){ /** Corresponds to line 277 of original code*/
//        proj = gm.getProjection();
//        return proj.fromScreenLocation(pt); // the android maps api equivalent
//    }
//
//    /** Corresponds to line 279 of original code *//*
//    private int minDistExtract(List<Integer> distances){
//        if(distances.size()==0)
//            throw new IllegalArgumentException("Passed array is empty.");
//        int bestVal = distances.get(0);
//        int bestValInd = 0;
//        for (int ind=1; ind < distances.size(); ind++) {
//            if(distances.get(ind) < bestVal){
//                bestValInd=ind;
//            }
//        }
//        distances.remove(bestValInd);
//        return bestValInd;
//    }
//*/
//    /* arrIndexOf is irrelevant in java -  Corresponds to line 287 of original code */
//
//    /** ////////// BELOW ARE HELPER FUNCTION NOT FOUND IN THE ORIGINAL CODE /////////// */
//
//    private void waitForMapIdle() throws InterruptedException{
//        while (proj==null){  // check for "idle" event on map (i.e. no animation is playing)
//            Thread.sleep(50);// "Must wait for 'idle' event on map before calling whatever's next"
//        }
//    }
//
//    private void setSpiderfyalbe(Marker marker, boolean mode){
//        spiderfyable.put(marker,mode);
//    }
//
//    private boolean isSpiderfyalbe(Marker marker){
//        return spiderfyable.containsKey(marker) ? spiderfyable.get(marker) : false;
//    }
//
//    public boolean isAnythingSpiderfied() {
//        return spiderfiedClusters !=null;
//    }
//
//    //Right now method only checks the structure + names
//    private boolean assignVarArgs(Object[] varArgs){
//        int varLen=varArgs.length;
//        if(varLen % 2 != 0){
//            throw new IllegalArgumentException("Number of args is uneven.");
//        }
//        for(int ind=0; ind<varLen; ind=+2){
//            String key = (String) varArgs[ind];
//            if(key.equals(ARG_KEEP_SPIDERFIED)){}
//            else if(key.equals(ARG_MARK_WONT_HIDE)){}
//            else if(key.equals(ARG_MARK_WONT_MOVE)){}
//            else if(key.equals(ARG_NEARBY_DISTANCE)){}
//            else if(key.equals(ARG_CS_SWITCHOVER)){}
//            else if(key.equals(ARG_LEG_WEIGHT)){}
//            else throw new IllegalArgumentException("Invalid argument name.");
//        }
//        return true;
//    }
//
//    private void unspiderfyAll() {
//        for (Marker lastSpiderfiedCluster : spiderfiedClusters) {
//            unspiderfy(lastSpiderfiedCluster);
//        }
//        for (Marker marker : spiderfiedUnclusteredMarkers) {
//            unspiderfy(marker);
//        }
//        initMarkerArrays(); //hopefully, return to the initial state
//        isAnythingSpiderfied=false;
//    }
//
//    boolean tryAddMarker(Collection<Marker> collection, Marker obj ){
//        if (collection.contains(obj))
//            return false;
//        else {
//            collection.add(obj);
//            return true;
//        }
//
//    }
//}