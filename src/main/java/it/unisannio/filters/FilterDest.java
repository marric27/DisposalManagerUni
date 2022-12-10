package it.unisannio.filters;

import it.unisannio.model.Sample;

public class FilterDest extends Filter {

    public FilterDest(String name, Filter next) {
        super(name, next);
    }

    @Override
    public Sample filter(Sample s) {
        System.out.println("########################SCRITTO IN DB");
        return null;
    }
}
