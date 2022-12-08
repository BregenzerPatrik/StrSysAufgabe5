package org.example;
import java.util.*;
import java.time.LocalDateTime;

public class PathDataStore {
    private static final Set<Route> usedCells = new TreeSet<>();
    private static long aktTimestamp = 0;
    private static final long intervallInMillis = 30 * 60 * 1000;
    private static LinkedList<PathData> currentlySavedCells = new LinkedList<>();
    private static SortedSet<PathData> currentlySavedCells2 = new TreeSet<>();
    private static Route[] currentlyBestRoutes =getMostUsedRoute();

    private static boolean checkIfBestRouteIsOutDated(Route[] newBest) {
        for(int i = 0; i<10;++i){
            if (newBest[i] != null)
                if ( ! newBest[i].equals(currentlyBestRoutes[i]))
                    return true;
        }
        return false;
    }

    private static LinkedList<PathData> popList(long expierationTimestamp) {
        //removes elements that are older then 30 minutes form the Linked lists
        //that tracks age and returns them.
        LinkedList<PathData> result = new LinkedList<>();
        if (currentlySavedCells.size() == 0) {
            return result;
        }
        while (currentlySavedCells.getFirst().dropoff_datetime() < expierationTimestamp) {
            //System.out.println("removing");
            result.add(currentlySavedCells.removeFirst());
        }
        return result;
    }

    private static void removeOldpathData() {
        LinkedList<PathData> popList = popList(aktTimestamp - intervallInMillis);
        for (PathData pathData : popList) {
            Route route = new Route(
                    pathData.pickup_longitude(),
                    pathData.pickup_latitude(),
                    pathData.dropoff_longitude(),
                    pathData.dropoff_latitude());
            decrementIndivdualRoute(route);
        }
    }

    private static void decrementIndivdualRoute(Route route) {
        int frequency=0;
        for(Route r:usedCells) {
            if(r.equals(route)){
                frequency=r.getFrequency();
                usedCells.remove(r);
                break;
            }
        }
        if(frequency>1){
            route.setFrequency(frequency-1);
            usedCells.add(route);
        }
    }


    private static void addElementToCurrentlySavedItemList(PathData pathData) {
        if (currentlySavedCells.size() == 0) {
            currentlySavedCells = new LinkedList<PathData>();
            currentlySavedCells.add(pathData);
            return;
        }
        int i = 0;
        for (PathData savedData : currentlySavedCells) {
            if (savedData.dropoff_datetime() > pathData.dropoff_datetime()) {
                break;
            }
            i += 1;
        }
        currentlySavedCells.add(i, pathData);
    }

    public static void add(PathData pathData){
        long timeOfArrival= LocalDateTime.now().getNano();
        if (pathData.dropoff_datetime() > aktTimestamp) {
            //if has newest timestamp
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
            if (route.isInsideGrid()) {
                addElementToCurrentlySavedItemList(pathData);
                addRoute(route);
                Route[] newMostUsedRoutes = getMostUsedRoute();

                boolean needToReplaceBestList = checkIfBestRouteIsOutDated(newMostUsedRoutes);
                if (needToReplaceBestList) {

                   /* for(long i =0;i<1000L;++i){
                        //int r = 100;
                        System.out.print("_");
                    }
                    System.out.print("\n");*/

                    long timeOfProcessingFinished=LocalDateTime.now().getNano();

                    currentlyBestRoutes = newMostUsedRoutes;
                    long timeNeeded=(timeOfProcessingFinished -timeOfArrival);//Kann 0 sein, weil sich LocalDateTime.now().getNano() sehr unregelmäßig aktualisiert
                    printBestRouteString(pathData,timeNeeded);
                }
            }
        }
    }

    private static void printBestRouteString(PathData pathData, long duration) {
        String resultString = pathData.pickup_datetime() + " " +
                pathData.dropoff_datetime() + " ";
        int lenOfArray = currentlyBestRoutes.length;

        for (int i = 0; i < lenOfArray; ++i) {
            Route currentRoute = currentlyBestRoutes[i];
            if (currentRoute == null)
                resultString = resultString + " NULL NULL ";
            else
                resultString = resultString + currentRoute.getFrom() + " " + currentRoute.getTo() + " ";
        }
        System.out.println(resultString+ duration/1000000.0);//1000000.0
    }

    private static void addRoute(Route route) {
        int frequency=0;
        for(Route r:usedCells){
            if(r.equals(route)){
                frequency=r.getFrequency();
                break;
            }
        }
        if(frequency!=0){
            usedCells.remove(route);
        }
        route.setFrequency(frequency + 1);
        usedCells.add(route);
    }

    public static Route[] getMostUsedRoute() {
        Route[] result = new Route[10];
        int i = 0;
        for (Route route : usedCells) {
            if (i < 10) {
                result[i] = route;
                ++i;
            }
            else {
                break;
            }
        }
        while (i < 10) {
            result[i] = null;
            ++i;
        }
        return result;
    }
}

