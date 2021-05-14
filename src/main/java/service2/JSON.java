package service2;

import com.fasterxml.jackson.databind.ObjectMapper;
import service3.Cargo;
import service3.CargoType;
import service3.Ship;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class JSON {
    List<Ship> ships;

    public JSON() {
    }

    public JSON(List<Ship> ships) {
        this.ships = ships;
    }

    public List<Ship> readSchedule() {
        ships = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String filename = "ships.json";
            ships = Arrays.asList(mapper.readValue(Paths.get(filename).toFile(), Ship[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ships;
    }

    public void writeSchedule() {
        final String filename = "ships.json";
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(Paths.get(filename).toFile(), ships);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addManually(List<Ship> schedule) {
        Scanner in = new Scanner(System.in);
        String userAnswer = "n";
        System.out.println("Add ship? y/n: ");
        userAnswer = in.next();
        while (!userAnswer.equals("n")) {
            if (userAnswer.equals("y")) {
                System.out.println("Ship's name: ");
                String name = in.next();
                System.out.println("0->loose 1->liquid 2->containers: ");
                int cargoTypeAsInt = in.nextInt();
                System.out.println("Cargo's amount: ");
                int weightOrQuantity = in.nextInt();
                System.out.println("Arrival time: ");
                int arrivalTime = in.nextInt();
                CargoType cargoType = switch (cargoTypeAsInt) {
                    case 0 -> CargoType.LOOSE;
                    case 1 -> CargoType.LIQUID;
                    case 2 -> CargoType.CONTAINER;
                    default -> throw new IllegalStateException("Unexpected value: " + cargoTypeAsInt);
                };
                int workingCranesPerformance = switch (cargoTypeAsInt) {
                    case 0 -> 1;
                    case 1 -> 2;
                    case 2 -> 3;
                    default -> 0;
                };
                //  int hours = arrivalTime;
                int unloadingTime = weightOrQuantity / workingCranesPerformance;
                String month = "June";
                Cargo cargo = new Cargo(cargoType, weightOrQuantity);
                int day = 0;
                int hoursInADay = arrivalTime / 60;
                int minutesInADay = arrivalTime;

                if (arrivalTime < 0) {
                    month = "May";
                    int TOTAL_MONTH_MINUTES = 43200;
                    day = ((TOTAL_MONTH_MINUTES + arrivalTime) / 60) / 24 + 1;
                }
                if (arrivalTime >= 0) {
                    month = "June";
                    day = (int) Math.floor(((arrivalTime) / 60) / 24) + 1;
                }
                if ((arrivalTime > 43200)) {
                    month = "July";
                    day = (int) Math.floor(((arrivalTime) / 60) / 24) - 29;
                }
                if (minutesInADay / 60 > 0) {
                    while ((hoursInADay) > 24) {
                        hoursInADay = hoursInADay - 24;
                    }
                    while ((minutesInADay > 60)) {
                        minutesInADay = minutesInADay - 60;
                    }
                } else {
                    while (hoursInADay < 0) {
                        hoursInADay = hoursInADay + 24;
                    }
                    while ((minutesInADay < 0)) {
                        minutesInADay = minutesInADay + 60;
                    }
                }
                if (hoursInADay % 24 == 0) {
                    hoursInADay = 23;
                }
                Ship ship = new Ship(name, cargo, arrivalTime / 60, arrivalTime, unloadingTime, workingCranesPerformance, month, day, hoursInADay, minutesInADay);
                ships.add(ship);
                schedule.sort(Comparator.comparingInt(Ship::getArrivalTime));
                System.out.println("Add another ship? y/n: ");
                userAnswer = in.next();
                if (userAnswer.equals("y"))
                {
                    continue;
                }
            }
        }
    }
}

