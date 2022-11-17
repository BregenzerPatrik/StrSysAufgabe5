package org.example;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PathDataStore {
    private static final Map<Route, Integer> usedCells = new HashMap<>();
    private static long aktTimestamp = 0;
    private static final long intervallInMillis = 30 * 60 * 1000;
    private static LinkedList<PathData> currentlySavedCells = new LinkedList<>();

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
        }
        if (!(pathData.dropoff_datetime() < aktTimestamp - intervallInMillis)) {
            currentlySavedCells.add(pathData);
            Route route = new Route(
                    pathData.pickup_longitude(),
                    pathData.pickup_latitude(),
                    pathData.dropoff_longitude(),
                    pathData.dropoff_latitude());
            addRoute(route);


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
}
