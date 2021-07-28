package semestralniPrace;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorizedMap {

    private List<Color> colours;
    private final Map MAP;

    public ColorizedMap(Map map){
        colours = new ArrayList<>(Map.numberOfCells);
        this.MAP = map;
        this.colorizeMap();
    }

    public void colorizeMap(){
        double low = MAP.lowestPoint();
        double high = MAP.highestPoint();
        double delta = high - low;
        double here;

        for(int i = 0; i < Map.numberOfCells; i++){
            here = MAP.currPoint(i);

            if(here < ((delta / 5.0 ) + low)) {
                colours.add(MyColors.terrainColors[0]);
            }
            if(here < ((delta / (5.0 / 2)) + low)) {
                colours.add(MyColors.terrainColors[1]);
            }
            if(here < ((delta / (5.0 / 3)) + low)) {
                colours.add(MyColors.terrainColors[2]);
            }
            if(here < ((delta / (5.0 / 4)) + low)) {
                colours.add(MyColors.terrainColors[3]);
            }
            if(((delta / (5.0 / 4)) + low) <= here) {
                colours.add(MyColors.terrainColors[4]);
            }
            colours.add(Color.black);
        }
    }

    public Color getColor(int index){
        return colours.get(index);
    }


}
