package it.unisannio.filters;

import it.unisannio.model.Sample;

public class FilterByTimestamp extends Filter {

    final int FROM_TIME = 1, TO_TIME = 20;

    public FilterByTimestamp(String name, Filter next) {
        super(name, next);
    }

    @Override
    public Sample filter(Sample s) {
        if (s.getTimestamp().getHour() < TO_TIME && s.getTimestamp().getHour() > FROM_TIME) {
            System.out.println("filtrato per timestamp");
            return s;
        }
        return null;
    }
}
