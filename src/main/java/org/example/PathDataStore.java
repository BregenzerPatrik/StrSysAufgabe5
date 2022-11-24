package org.example;

import java.util.*;

public class PathDataStore {
    private static final Map<Route, Integer> usedCells = new TreeMap<>();
    private static long aktTimestamp = 0;
    private static final long intervallInMillis = 30 * 60 * 1000;
    private static LinkedList<PathData> currentlySavedCells = new LinkedList<>();

    private static SortedSet<PathData> currentlySavedCells2 = new TreeSet<>();

    private static LinkedList<PathData> popList(long expierationTimestamp) {
        //removes elements that are older then 30 minutes form the Linked lists
        //that tracks age and returns them.
        LinkedList<PathData> result = new LinkedList<>();
        while (currentlySavedCells.getFirst().dropoff_datetime() < expierationTimestamp) {
            result.add(currentlySavedCells.removeFirst());
        }
        return result;
    }

    private static void removeOldpathData(){

        LinkedList<PathData> popList = popList(aktTimestamp - intervallInMillis);
        for(PathData pathData:popList){
            Route route = new Route(
                    pathData.pickup_longitude(),
                    pathData.pickup_latitude(),
                    pathData.dropoff_longitude(),
                    pathData.dropoff_latitude());

        }
    }
    private static void decrementIndivdualRoute(Route route){
        if(usedCells.containsKey(route)){
            if(usedCells.get(route)==1){
                usedCells.remove(route);
            }
            else {
                int frequency= usedCells.get(route);
                usedCells.remove(route);
                usedCells.put(route,frequency-1);
            }
        }
    }

    public static void add(PathData pathData) {
        if (pathData.dropoff_datetime() > aktTimestamp) {
            aktTimestamp = pathData.dropoff_datetime();
            removeOldpathData();
        }
        if (!(pathData.dropoff_datetime() < aktTimestamp - intervallInMillis)) {
            //if data is not to old (last 30 mins)
            Route route = new Route(
                    pathData.pickup_longitude(),
                    pathData.pickup_latitude(),
                    pathData.dropoff_longitude(),
                    pathData.dropoff_latitude());
            if(route.isInsideGrid()) {
                addElementToCurrentlySavedItemList(pathData);
                addRoute(route);
            }
        }
    }

    private static void addRoute(Route route) {
        if(usedCells.containsKey(route)){
            int frequency = usedCells.get(route);
            usedCells.remove(route);
            usedCells.put(route,frequency+1);
        }else {
            usedCells.put(route,1);
        }
    }
    public static Route[] getMostUsedRoute(){

        System.out.println("Correct Routes: "+usedCells.keySet().size());
        for(Route route : usedCells.keySet()){
            int frequency=usedCells.get(route).intValue();
            TenRoutes.tryAddRoute(route,frequency);
        }

        return TenRoutes.getRoutes();
    }
    public static int[] frequencies(){
        return TenRoutes.frequencies;
    }
    private class TenRoutes{


        private static int[] frequencies = new int[10];
        private static Route[] routes= new Route[10];

        public static void tryAddRoute(Route route,int frequency){
            int minFrequency= frequencies[0];
            for(int i = 1; i<10;i++){
                if(frequencies[i]<minFrequency){
                    minFrequency= frequencies[i];
                }
            }
            for(int i = 0; i<10;i++){
                if(frequencies[i]<frequency && frequencies[i]==minFrequency){
                    frequencies[i]=frequency;
                    routes[i]=route;
                    break;
                }
            }
        }
        public static int[] getFrequencies() {
            return frequencies;
        }
        public static Route[] getRoutes() {
            return routes;
        }
    }
}

