import Controller.CarController;
import Model.VehicleCreator;
import Model.World;
import View.CarView;
import View.DrawPanel;

public class Simulation{
    private static final int X=800;
    private static final int Y=800;


    public static void main(String[] args) {
        World world = new World(X,Y,6);
        DrawPanel panel = new DrawPanel(X,Y-240);
        CarView frame= new CarView("yeehaw", panel);
        CarController controller = new CarController(world, frame);
        world.AddObserver(panel);
        world.addVehicle(VehicleCreator.getVolvo());
        world.addVehicle(VehicleCreator.getSaab());
        world.addVehicle(VehicleCreator.getScania());
    }
}

