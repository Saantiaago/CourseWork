import service3.Cargo;
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
        int threadsNumber = 3;
        Cargo.CargoType types[] = Cargo.CargoType.values();
        ScheduleCreation schedule = new ScheduleCreation();
        schedule.createSchedule();
        JSON jsonReader = new JSON();
        List<Ship> shipListFromSchedule = jsonReader.readSchedule();
        List<Unloading> unloadSchedule = new ArrayList<>(3);
        for (int i = 0; i < threadsNumber; i++)
        {
            List<Ship> cargos = new ArrayList<>();
            for (Ship ship : shipListFromSchedule)
            {
                if (ship.getCargo().getCargoType() == types[i])
                {
                    cargos.add(ship);
                }
            }
            Unloading cargosUnloading = new Unloading(cargos);
            unloadSchedule.add(cargosUnloading);
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
        try
        {
            List<Future<Object>> result = executor.invokeAll(unloadSchedule);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}