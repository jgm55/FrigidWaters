package edu.drexel.cci.hiyh.has.device;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DummyDevice extends Device {

    private Action hello = new Action("Say Hello") {
        @Override
        public void invoke(List<Object> args) {
            System.out.println(DummyDevice.this.name + ": Hello world");
        }
    };
    private static final List<ParamType<?>> printByteParams =
        Collections.unmodifiableList(Arrays.asList(new ParamType<?>[] {new BoundedInt(0, 255)}));
    private Action print = new Action("Print Byte") {
        @Override
        public List<ParamType<?>> getParameterTypes() {
            return printByteParams;
        }
        @Override
        public void invoke(List<Object> args) {
            // TODO validate?
            System.out.println(DummyDevice.this.name + ": " + args.get(0));
        }
    };

    public DummyDevice(String name) {
        super(name);
        addAction(hello);
        addAction(print);
    }
}
