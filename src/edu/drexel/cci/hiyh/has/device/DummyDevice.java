package edu.drexel.cci.hiyh.has.device;

import java.io.IOException;

public class DummyDevice extends Device {

    public DummyDevice(String name) {
        super(name);
    }

    @ActionMethod(name="Say Hello")
    public void hello() {
        System.out.println(name + ": Hello world");
    }

    //@ActionMethod(name="Print Byte")
    public void print(Byte b) throws IOException {
        System.out.println(name + ": " + b);
    }
}
