package it.unisannio.filters;

import java.util.concurrent.ConcurrentLinkedQueue;

import it.unisannio.model.Sample;

public abstract class Filter implements Runnable {

    ConcurrentLinkedQueue<Sample> samples;
    Filter next;
    String name;

    public Filter(String name, Filter next) {
        this.name = name;
        samples = new ConcurrentLinkedQueue<>();
        this.next = next;
    }

    public void process(Sample s){
        samples.add(s);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Sample s = samples.poll();
                if (s != null) {
                    //System.out.println("name = " + name +" s = " + s);
                    //aggiustare comportamento con il successivo
                    //if (next != null) {
                        Sample sampFilt = filter(s);
                        
                        if(sampFilt != null) { 
                            //System.out.println("filtrato "+ sampFilt);
                            if (next != null) next.process(sampFilt);
                        }
                   // }

                } else {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Sample filter(Sample s);
}
