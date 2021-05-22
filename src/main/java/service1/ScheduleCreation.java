package service1;

import service2.JSON;
import service3.Ship;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ScheduleCreation
{
//    public static String month = "June";
    List<Ship> schedule;
    Random rand = new Random();
    public int iterationsNumber = 200;
    public void createSchedule()
    {
        schedule = new ArrayList<Ship>();
        for (int i = 0; i < iterationsNumber; i++)
        {
            Ship ship = Ship.createShip();
            int maxDelay = 10080;
            int minDelay = -10080;
            int day = 0;
            int hours, minutes, hoursInADay = 24, minutesInAnHour = 60;
            String month = "";
            int arrDelay = rand.nextInt(maxDelay - minDelay) + minDelay;
            int arrivalTime = ship.getArrivalTime() + arrDelay;
            int delayInHours = (ship.getArrivalTime() + arrDelay)/minutesInAnHour;
            ship.setArrivalTimeInHours(delayInHours);
            ship.setArrivalTime(arrivalTime);
            int totalMonthMinutes = 43200;
            if (arrivalTime < 0)
            {
                month = "May";
                day = ((totalMonthMinutes + arrivalTime)/minutesInAnHour)/hoursInADay+1;
            }
            if ((arrivalTime >= 0) && (arrivalTime <= totalMonthMinutes))
            {
                month = "June";
                day = (int)Math.floor(((arrivalTime)/minutesInAnHour)/hoursInADay)+1;
            }
            if ((arrivalTime > totalMonthMinutes))
            {
                month = "July";
                day = (int)Math.floor(((arrivalTime)/minutesInAnHour)/hoursInADay)-29;
            }
            if (delayInHours > 0)
            {
                while (delayInHours > hoursInADay) {
                    delayInHours = delayInHours - hoursInADay;
                }
                while ((arrivalTime > minutesInAnHour))
                {
                    arrivalTime = arrivalTime - minutesInAnHour;
                }
            }
            else
            {
                while (delayInHours < 0) {
                    delayInHours = delayInHours + hoursInADay;
                }
                while ((arrivalTime < 0))
                {
                    arrivalTime = arrivalTime + hoursInADay;
                }
            }
            if (delayInHours % hoursInADay == 0)
            {

                delayInHours = hoursInADay - 1;
            }
            ship.setMonth(month);
            hours = delayInHours;
            minutes = arrivalTime;
            ship.setDay(day);
            ship.setH(hours);
            ship.setM(minutes);
            int unloadingDelay = 1440;
            ship.setUnloadingTime(rand.nextInt(unloadingDelay) + ship.getUnloadingTime());
            schedule.add(ship);
        }
        System.out.println("Ships number: " + iterationsNumber);
        schedule.sort(Comparator.comparingInt(Ship::getArrivalTime));
        JSON jsonWriter = new JSON(schedule);
        jsonWriter.addShip(schedule);
        jsonWriter.writeSchedule();
    }
}
