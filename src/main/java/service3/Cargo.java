package service3;

import java.util.Random;

public class Cargo
{
    private CargoType cargoType;
    private int weightAmount;

    public Cargo(CargoType cargoType, int weightAmount)
    {
        this.cargoType = cargoType;
        this.weightAmount = weightAmount;
    }

    public CargoType getCargoType()
    {
        return cargoType;
    }

    public int getWeightAmount()
    {
        return weightAmount;
    }

    Cargo()
    {}
    public static Cargo createCargo()
    {
        Random rand = new Random();
        int maxWeightAmount = 100;
        CargoType[] types = CargoType.values();
        CargoType type = types[rand.nextInt(types.length)];
        int weightAmount = rand.nextInt(maxWeightAmount);
        Cargo cargo = new Cargo(type, weightAmount);
        return cargo;
    }
}