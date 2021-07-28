package semestralniPrace;

import java.util.ArrayList;
import java.util.List;

public class Storage<E> {
    private final List<E> data;

    public Storage(){
        data = new ArrayList<>();
    }

    public void store(E element){
        data.add(element);
    }

    public E get(int index){
        return data.get(index);
    }
    public int size(){
        return data.size();
    }

}
