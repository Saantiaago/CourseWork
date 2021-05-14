package service3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Unloading implements Callable<Object>
{
    private int fine = 0;
    private List<Ship> ships;
    private List<Crane> cranes;
    private CargoType cargoType;
    private List<Ship> looseCargos;
    private List<Ship> liquidCargos;
    private List<Ship> containerCargos;
    private int cranesQuantity = 0;

    public List<Ship> getLooseCargos()
    {
        return looseCargos;
    }

    public List<Ship> getLiquidCargos()
    {
        return liquidCargos;
    }

    public List<Ship> getContainerCargos()
    {
        return containerCargos;
    }

    public void setLooseCargos(List<Ship> looseCargos)
    {
        this.looseCargos = looseCargos;
    }

    public void setLiquidCargos(List<Ship> liquidCargos)
    {
        this.liquidCargos = liquidCargos;
    }

    public void setContainerCargos(List<Ship> containerCargos)
    {
        this.containerCargos = containerCargos;
    }

    public Unloading(List<Ship> ships)
    {
        this.ships = ships;
    }

    public int getСranesQuantity()
    {
        return cranesQuantity;
    }

    public int getFine()
    {
        return fine;
    }

    @Override
    public Crane call(){
        int CRANE_PRICE = 30000;
        while (fine >= CRANE_PRICE * cranesQuantity) {
//            if (cranesQuantity == 1) break;
            ConcurrentLinkedQueue<Ship> queueOfShips = new ConcurrentLinkedQueue<>(ships);
            fine = 0;
            cranesQuantity++;
            Ship ship = queueOfShips.peek();
            cargoType = ship.cargo.getCargoType();
            cranes = new ArrayList<>(cranesQuantity);
            ExecutorService executor = Executors.newFixedThreadPool(cranesQuantity);
            for (int i = 0; i < cranesQuantity; i++) {
                Crane crane = new Crane(queueOfShips);
                cranes.add(crane);
            }
            try
            {
                List<Future<Object>> result = executor.invokeAll(cranes);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            executor.shutdown();
            for (Crane crane : cranes)
            {
                fine += crane.getCraneFine();
            }
        }
        System.out.println("====================\n" + cargoType + "\n" + fine + "\nAmount of cranes: " + cranesQuantity + "\n====================");
        return (Crane) cranes;
    }

}
