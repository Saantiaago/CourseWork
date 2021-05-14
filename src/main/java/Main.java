import service3.Ship;
import service2.JSON;
import service1.ScheduleCreation;
import service3.Unloading;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        String date = "Today is the 1st of ";
//        System.out.println(date + ScheduleCreation.month);
        ScheduleCreation schedule = new ScheduleCreation();
        List<Ship> looseCargos = new ArrayList<>();
        List<Ship> liquidCargos = new ArrayList<>();
        List<Ship> containerCargos = new ArrayList<>();
        schedule.createSchedule();

        JSON jsonReader = new JSON();
        List<Ship> shipListFromSchedule = jsonReader.readSchedule();

        for (Ship ship : shipListFromSchedule)
        {
            switch (ship.getCargo().getCargoType())
            {
                case LOOSE -> looseCargos.add(ship);
                case LIQUID -> liquidCargos.add(ship);
                case CONTAINER -> containerCargos.add(ship);
            }
        }

        List<Unloading> unloadings = new ArrayList<>(3);

        Unloading looseShipsUnloading = new Unloading(looseCargos);
        Unloading liquidShipsUnloading = new Unloading(liquidCargos);
        Unloading containerShipsUnloading = new Unloading(containerCargos);
        unloadings.add(looseShipsUnloading);
        unloadings.add(liquidShipsUnloading);
        unloadings.add(containerShipsUnloading);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        try
        {
            List<Future<Object>> stat = executor.invokeAll(unloadings);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
