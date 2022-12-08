package org.example;

public class GridCell implements Comparable<GridCell>{

    private static final double latitudeDegreeOfCenterCell = -74.913585; //east-west axis
    private static final double longitudeDegreeOfCenterCell = 41.474937; //north-south axis
    private static final double degreeOffsetPerLongitudeCell = 0.004491556;
    private static final double degreeOffsetPerLatitudeCell = 0.005986;

    private final int longitudeIndex;
    private final int latitudeIndex;

    public int getLongitudeIndex() {
        return longitudeIndex;
    }

    public int getLatitudeIndex() {
        return latitudeIndex;
    }

    public GridCell(double latitude , double longitude) {
        // latitude=(latitudeIndex-1)*0.005986 +41.474937 east-west
        // latitude - 41.474937 = (latitudeIndex-1)*0.005986
        // (latitude - 41.474937)/0.005986 = latitudeIndex-1
        // latitudeIndex = (latitude - 41.474937)/0.005986 +1
        double latitudeOffset = latitude - latitudeDegreeOfCenterCell;
        this.latitudeIndex = ((int) Math.round(latitudeOffset / degreeOffsetPerLatitudeCell)) + 1;
        // longitude=(longitudeIndex-1)*0.004491556 -74.913585 north-south
        double longitudeOffset = longitude - longitudeDegreeOfCenterCell;
        this.longitudeIndex = -1*((int) Math.round(longitudeOffset / degreeOffsetPerLongitudeCell)) + 1;
    }

    @Override
    public String toString() {
        return "("+longitudeIndex+"|"+latitudeIndex+")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GridCell cell)) {
            return false;
        }
        return this.longitudeIndex == cell.longitudeIndex && this.latitudeIndex == cell.latitudeIndex;
    }
    @Override
    public int compareTo(GridCell cell){
        if(this.longitudeIndex>cell.getLongitudeIndex()){
            return 1;
        } else if (this.longitudeIndex<cell.getLongitudeIndex()) {
            return -1;
        }
        else return Integer.compare(this.latitudeIndex, cell.getLatitudeIndex());

    }
}
