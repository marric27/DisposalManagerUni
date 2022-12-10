package it.unisannio.filters;

import it.unisannio.model.Sample;

public class FilterByTag extends Filter {

    final String PREFIX = "57434F4D50";

    public FilterByTag(String name, Filter next) {
        super(name, next);
    }

    @Override
    public Sample filter(Sample s) {
        if (s.getTagID().startsWith(PREFIX)) {
            System.out.println("filtrato per tag");
            return s;
        }
        return null;
    }
}
