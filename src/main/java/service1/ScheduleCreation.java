package service1;

import service2.JSON;
import service3.Ship;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ScheduleCreation
{
    public static String month = "June";
    List<Ship> schedule;
    Random rand = new Random();
    public int iterationsNumber = 30;
    public void createSchedule()
    {
        schedule = new ArrayList<Ship>();
        for (int i = 0; i < iterationsNumber; i++)
        {
            Ship ship = Ship.createShip();
            int maxDelay = 10080;
            int minDelay = -10080;
            int day = 0;
            int hoursInADay = 0;
            int minutesInADay = 0;
            String month = "";
            int arrDelay = rand.nextInt(maxDelay - minDelay) + minDelay;
            int arrivalTime = ship.getArrivalTime() + arrDelay;
            int delayInHours = (ship.getArrivalTime() + arrDelay)/60;
            ship.setArrivalTimeInHours(delayInHours);
            ship.setArrivalTime(arrivalTime);
            if (arrivalTime < 0)
            {
                month = "May";
                int totalMonthMinutes = 43200;
                day = ((totalMonthMinutes + arrivalTime)/60)/24+1;
            }
            if ((arrivalTime >= 0) && (arrivalTime <= 43200))
            {
                month = "June";
                day = (int)Math.floor(((arrivalTime)/60)/24)+1;
            }
            if ((arrivalTime > 43200))
            {
                month = "July";
                day = (int)Math.floor(((arrivalTime)/60)/24)-29;
            }
            if (delayInHours > 0)
            {
                while (delayInHours > 24) {
                    delayInHours = delayInHours - 24;
                }
                while ((arrivalTime > 60))
                {
                    arrivalTime = arrivalTime - 60;
                }
            }
            else
            {
                while (delayInHours < 0) {
                    delayInHours = delayInHours + 24;
                }
                while ((arrivalTime < 0))
                {
                    arrivalTime = arrivalTime + 60;
                }
            }
            if (delayInHours % 24 == 0)
            {
                delayInHours = 23;
            }
            ship.setMonth(month);
            hoursInADay = delayInHours;
            minutesInADay = arrivalTime;
            ship.setDay(day);
            ship.setH(hoursInADay);
            ship.setM(minutesInADay);
            int unloadingDelay = 1440;
            ship.setUnloadingTime(rand.nextInt(unloadingDelay) + ship.getUnloadingTime());
            schedule.add(ship);
        }
        System.out.println("Iterations number: " + iterationsNumber);
        schedule.sort(Comparator.comparingInt(Ship::getArrivalTime));
        JSON jsonWriter = new JSON(schedule);
        jsonWriter.addManually(schedule);
        jsonWriter.writeSchedule();
    }
}
