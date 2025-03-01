package Model;
import javax.swing.*;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;


public class World implements ActionListener, Observable{
    private static final int delay = 50;
    private Timer timer = new Timer(delay, this);
    ArrayList<Vehicle> cars = new ArrayList<>();
    Workshop<Volvo240> workshop = new Workshop<Volvo240>(500, 0, 2);
    ArrayList<WorldObserver> observers= new ArrayList<WorldObserver>();
    ArrayList<DrawableWithPosition> drawobjects = new ArrayList<DrawableWithPosition>(); //Lite redundant men smidigt
    int X;  //Behövs för att kunna hantera collisions med border
    int Y;
    int carLimit;

    
    public World(int x_size, int y_size, int carLimit){
        timer.start();
        X=x_size;
        Y=y_size;
        this.carLimit = carLimit;
    }

    public void actionPerformed(ActionEvent e) {
        checkForCollisionWithWorkshop();
        checkForCollisionWithBorder(X);
        moveAllCars();
    }

    public void gas(int amount) {
        double gas = ((double) amount) / 100;
        for (Vehicle car : cars)
        {
            try {
                car.gas(gas);
            } catch(Exception IllegalStateException) {
                continue;
            }
        }
    }

    public void brake(int amount) {
        double brake = ((double) amount) / 100;
        for (Vehicle car : cars)
        {
            car.brake(brake);
        }
    }

    public void startEngines() {
        for (Vehicle car : cars)
        {
            car.startEngine();
        }
    }

    public void stopEngines() {
        for (Vehicle car : cars)
        {
            car.stopEngine();
        }
    }

    public void saabTurboOn() {
        for (Vehicle car : cars)
        {
            if (car instanceof Saab95){
                Saab95 saab = (Saab95) car;
                saab.setTurboOn();
            }    
        }
    }

    public void saabTurboOff() {
        for (Vehicle car : cars)
        {
            if (car instanceof Saab95){
                Saab95 saab = (Saab95) car;
                saab.setTurboOff();
            }    
        }
    }

    public void PlatformDown() {
        for (Vehicle car : cars)
        {
            if (car instanceof TiltablePosterior){
                TiltablePosterior tiltable = (TiltablePosterior) car;
                tiltable.lowerTilt();
            }    
        }
    }

    public void PlatformUp() {
        for (Vehicle car : cars)
        {
            if (car instanceof TiltablePosterior){
                TiltablePosterior tiltable = (TiltablePosterior) car;
                tiltable.raiseTilt();
            }    
        }
    }

    public void checkForCollisionWithWorkshop() {
        for (Vehicle car : cars) {
            if (car instanceof Volvo240) {
                workshop.load((Volvo240) car);
                if (workshop.getCurrentLoad().contains(car)) {
                    car.stopEngine();
                }
            }
        }
    }

    public void moveAllCars(){
        for (Vehicle car : cars){
            car.move();
        }
        notifyofchange();   //Låter observers veta att modellen förändrats
    }
    public void checkForCollisionWithBorder(double borderWidth){
        for (Vehicle car: cars){
        if (car.getCurrentPos()[0] + car.getImage().getWidth() >= borderWidth){ // frame.drawPanel.volvoImage volvoImage is very hard coded change!
            car.setDirection(car.getDirection() + 180);
        } else if (car.getCurrentPos()[0] < 0) {
            car.setDirection(car.getDirection() - 180);
        }
    }
    }
    public void notifyofchange(){
        for (WorldObserver o:observers){
            o.update(drawobjects);
        }
    }    

    public void addRandomCar(){        //Borde funka, ej testat dock
        Random random = new Random();
        int number = random.nextInt(4);
        switch (number){
            case 0: 
                addVehicle(VehicleCreator.getVolvo()); 
                break;
            case 1: 
                addVehicle(VehicleCreator.getSaab()); 
                break;
            case 2: 
                addVehicle(VehicleCreator.getScania()); 
                break;
            case 3: 
                addVehicle(VehicleCreator.getVolvoFM9());
                break;
        }
    }

    public void addVehicle(Vehicle vehicle) {
        if (cars.size() < carLimit) {
            cars.add(vehicle);
            setPos();
            updateDrawObjects();
        }
    }

    public void removeCar(){
        if (cars.size() > 0){
        cars.removeLast(); //Tar bort sista bilen
        }
        updateDrawObjects();
    }

    private void setPos(){
        int lastindex=cars.size()-1; //Index of latest added car
        int ypos=lastindex * 70;
        Vehicle newest = cars.get(lastindex);
        newest.setCurrentPos(0, ypos);
    }

    public void AddObserver(WorldObserver obs){    //Smidigt sätt att lägga til observers
        observers.add(obs);
    }

    void updateDrawObjects(){
        drawobjects.clear();
        drawobjects.addAll(cars);
        drawobjects.add(workshop);
    }

}
