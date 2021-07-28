package semestralniPrace;

import waterflowsim.Cell;
import waterflowsim.Simulator;

public class Map {
    private final Cell[] cells;
    public static int numberOfCells;

    public Map(){
        this.cells = Simulator.getData();
        numberOfCells = cells.length;
    }

    public double waterPoint(int index){
        return cells[index].getWaterLevel();
    }
    public double currPoint(int index){
        return cells[index].getTerrainLevel();
    }
    public double highestPoint(){
        double tmp = Double.MIN_VALUE;
        double lvl;
        for(Cell c: cells){
            lvl = c.getTerrainLevel();
            if(c.getTerrainLevel() > tmp)
                tmp = lvl;
        }
        return tmp;
    }
    public double lowestPoint(){
        double tmp = Double.MAX_VALUE;
        double lvl;
        for(Cell c: cells){
            lvl = c.getTerrainLevel();
            if(c.getTerrainLevel() < tmp)
                tmp = lvl;
        }
        return tmp;
    }

    public Cell[] getCells() {
        return cells;
    }
}
