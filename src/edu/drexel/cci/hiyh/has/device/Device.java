package edu.drexel.cci.hiyh.has.device;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Device {
    public final String name;

    protected Device(String name) {
        this.name = name;
    }

    public class Action {
        public final String name;
        private final Method method;

        private Action(Method method) {
            this.name = method.getAnnotation(ActionMethod.class).name();
            this.method = method;
        }

        public Class<?>[] getParameterTypes() {
            return method.getParameterTypes();
        }

        public Object invoke(Object... args) {
            // FIXME There are probably some exceptions that should be passed
            // through here.
            try {
                return method.invoke(Device.this, args);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // FIXME Is there any way to make this list be built statically per
    // subclass of Device?
    public List<Action> getActions() {
        return Arrays.stream(getClass().getMethods())
               .filter(m -> m.isAnnotationPresent(ActionMethod.class))
               .map(Action::new)
               .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name;
    }
}
