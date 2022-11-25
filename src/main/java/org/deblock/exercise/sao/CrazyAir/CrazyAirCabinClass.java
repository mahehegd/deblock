package org.deblock.exercise.sao.CrazyAir;

public enum CrazyAirCabinClass {
    ECONOMY("E"), 
    BUSINESS("B")
    ;

    private String cabinClass;

    CrazyAirCabinClass(final String cabinClass){
        this.cabinClass = cabinClass;
    }

    @Override
    public String toString() {
        return cabinClass;
    }
}
