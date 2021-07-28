package semestralniPrace;

public class MyTime {
    private double MutableTime;
    private final double IMMUTABLE_TIME;
    private final double LOWER_BOUND;
    private final double UPPER_BOUND;
    private int percent = 25;

    public double getSpeed(){
        return Math.round((MutableTime/IMMUTABLE_TIME) * 100);
    }

    public MyTime(double step){
            MutableTime = step * 1000;
            IMMUTABLE_TIME = step * 1000;
            LOWER_BOUND = IMMUTABLE_TIME * 0.25;
            UPPER_BOUND = IMMUTABLE_TIME * 3.0;
    }

    public void slowDown(){
        MutableTime -= IMMUTABLE_TIME * percent * 0.01;
        if(MutableTime < LOWER_BOUND)
            speedUp();
    }

    public void speedUp(){
        MutableTime += IMMUTABLE_TIME * percent * 0.01;
        if(MutableTime > UPPER_BOUND)
            slowDown();
    }
}
