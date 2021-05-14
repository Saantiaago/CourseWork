package service3;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Crane implements Callable<Object>
{

    private int craneFine = 0;
    private final ConcurrentLinkedQueue<Ship> ships;

    public int getCraneFine()
    {
        return craneFine;
    }

    public Crane(ConcurrentLinkedQueue<Ship> ships)
    {
        this.ships = ships;
    }

    @Override
    public Object call() throws InterruptedException
    {
        int currentTime = -43200;
        Ship firstShip = ships.peek();
        System.out.println("-----------------------\nName: " + firstShip.getName() +"\nCargo type: " +
                firstShip.cargo.getCargoType() + "\nArrival time in minutes: " + firstShip.getArrivalTime() + "\nMonth: "
                + firstShip.getMonth() + "\nDay: " + firstShip.getDay()  + "\nHours: " + firstShip.getH() + "\nMinutes: "
                + firstShip.getM() + "\nDelay: " + 0 + "\nReal begin time: " + firstShip.getArrivalTime()+ "\nUnloading time: " + firstShip.getUnloadingTime());

        while (!ships.isEmpty())
        {
            Ship currentShip = ships.poll();
            currentTime = Math.max(currentTime, currentShip.getArrivalTime());
            currentTime += currentShip.getUnloadingTime();
            Ship nextShip = ships.peek();
            if (nextShip == null)
            {
                break;
            }
            if (nextShip.getArrivalTime() < currentTime)
            {
                if ((currentTime - nextShip.getArrivalTime() % 60) == 0)
                {
                    craneFine += 100 * ((currentTime - nextShip.getArrivalTime()) / 60);
                }
                craneFine += 100 * ((currentTime - nextShip.getArrivalTime()) / 60 + 1);
                System.out.println("-----------------------\nName: " + nextShip.getName() +"\nCargo type: " +
                        nextShip.cargo.getCargoType() + "\nArrival time in minutes: " + nextShip.getArrivalTime() +
                        "\nMonth: " + nextShip.getMonth() + "\nDay: " + nextShip.getDay() + "\nHours: " + nextShip.getH()
                        + "\nMinutes: " + nextShip.getM() + "\nDelay: " + ((currentShip.getArrivalTime() + currentShip.getUnloadingTime())
                        - nextShip.getArrivalTime()) + "\nReal begin time: " + (currentShip.getArrivalTime() + currentShip.getUnloadingTime()) +
                        "\nUnloading time: " + nextShip.getUnloadingTime());
            }else
            {
                System.out.println("-----------------------\nName: " + nextShip.getName() + "\nCargo type: " +
                        nextShip.cargo.getCargoType() + "\nArrival time in minutes: " + nextShip.getArrivalTime() +
                        "\nMonth: " + nextShip.getMonth() + "\nDay: " + nextShip.getDay()  + "\nHours: " + nextShip.getH()
                        + "\nMinutes: " + nextShip.getM() + "\nDelay: " + 0 + "\nReal begin time: " + nextShip.getArrivalTime() +
                        "\nUnloading time: " + nextShip.getUnloadingTime());
            }
            Thread.sleep(1);
        }
        return null;
    }
}
