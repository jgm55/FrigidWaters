package edu.drexel.cci.hiyh.has.device;

import java.io.IOException;

public class DummyDevice implements Device {
    @Action
    public void print(byte b) throws IOException {
        System.out.println("DummyDevice prints " + b);
    }
}
