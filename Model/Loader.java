package Model;
import java.util.Deque;
import java.util.LinkedList;

public class Loader<B extends DrawableWithPosition>{ //loading utility class. Takes a generic type B.

    Loadable<B> parent;
    Deque<B> load = new LinkedList<B>();
    int maxLoad;

    public Loader(Loadable<B> parent) { //Parent needs to be a loadable object.
        this.parent = parent;
        maxLoad = parent.getMaxLoad();
    }

    private boolean loadingFeasible(B vehicle) {
        double x = Math.abs(parent.getCurrentPos()[0] - vehicle.getCurrentPos()[0]);
        double y = Math.abs(parent.getCurrentPos()[1] - vehicle.getCurrentPos()[1]);
        double distance = Math.sqrt(x*x+y*y);
        if (distance < 50) { //maybe should be elsewhere??
            return true;
        }
        else {
            return false;
        }
    }

    public void load(B vehicle){
        if (loadingFeasible(vehicle) && load.size() < parent.getMaxLoad() && load.contains(vehicle) == false) {
            load.addLast(vehicle);
            vehicle.setCurrentPos(parent.getCurrentPos()[0], parent.getCurrentPos()[1]);
        }
    }

    public B unloadFirst(){
        B vehicle = load.removeFirst();
        vehicle.setCurrentPos(parent.getCurrentPos()[0] + 5, parent.getCurrentPos()[1] + 5);
        return vehicle;
    }

    public B unloadLast() {
        B vehicle = load.removeLast();
        vehicle.setCurrentPos(parent.getCurrentPos()[0] + 5, parent.getCurrentPos()[1] + 5);
        return vehicle;
    }
}