package service3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Unloading implements Callable<Object> {
    public static int totalFine = 0;
    public static int averageDelay = 0;
    public static int delay = 0;
    private int fine = 0;
    private List<Ship> ships;
    private List<Crane> cranes;
    private Cargo.CargoType cargoType;
    private static int amountOfShips = 0;
    private int Avg = 0;
    private static int signal = 0;
//    public int sizeOfQueue = 0;
    private int cranesQuantity = 0;

    public int getTotalFine() {
        return totalFine;
    }

    public Unloading(List<Ship> ships) {
        this.ships = ships;
        amountOfShips += ships.size();
    }

    public int getCranesQuantity() {
        return cranesQuantity;
    }

    public int getFine() {
        return fine;
    }

    @Override
    public Crane call() {
        int cranePrice = 30000;
        while (fine >= cranePrice * cranesQuantity) {
//            if (cranesQuantity == 1) break;
            ConcurrentLinkedQueue<Ship> queueOfShips = new ConcurrentLinkedQueue<>(ships);
            fine = 0;
            cranesQuantity++;

            Ship ship = queueOfShips.peek();
            if (ship != null) {
                cargoType = ship.cargo.getCargoType();
                cranes = new ArrayList<>(cranesQuantity);

                ExecutorService executor = Executors.newFixedThreadPool(cranesQuantity);
                for (int i = 0; i < cranesQuantity; i++) {
                    Crane crane = new Crane(queueOfShips);
                    cranes.add(crane);
                }
                try {
                    List<Future<Object>> result = executor.invokeAll(cranes);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executor.shutdown();

                for (Crane crane : cranes) {
                    fine += crane.getCraneFine();
//                    sizeOfQueue += crane.getSize();
                }
//                signal++;
//                if ((cranesQuantity == 1) && (Crane.sig == 1)) {
//                for (Crane crane : cranes) {
//                    fineWith1 += fine;
//                    sizeOfQueue += crane.getSize();
                    Avg = Crane.staticSizeOfQueue / cranes.size();
//                }
//                }
            }
        }
//        Ship nextShip = ships.peek()
//        ConcurrentLinkedQueue<Ship> queueOfShips = new ConcurrentLinkedQueue<>(ships);
        totalFine += fine;


        System.out.println("====================\n" + cargoType + "\n" + fine + "\nAmount of cranes: " + cranesQuantity
                + "\nTotal fine: " + totalFine +  "\nAverage Size Of Queue: " + Crane.staticSizeOfQueue / cranes.size()
                + "\nAmount Of Ships: " + amountOfShips +  "\nMax delay: " + Crane.staticDelay + "\nAverage delay " + Crane.staticAverageDelay+   "\n====================");
        return (Crane) cranes;
    }
}