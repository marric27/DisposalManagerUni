package it.unisannio.filters;

import it.unisannio.model.Sample;

public class FilterByOccurrency extends Filter {

    final int OCCURRENCY = 2000;

    public FilterByOccurrency(String name, Filter next) {
        super(name, next);
    }

    @Override
    public Sample filter(Sample s) {
        if (s.getOccurency() > OCCURRENCY) {
            System.out.println("filtrato per occurrency");
            return s;
        }
        return null;
    }
}