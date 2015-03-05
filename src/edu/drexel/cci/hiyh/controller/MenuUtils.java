package edu.drexel.cci.hiyh.controller;

import java.util.Arrays;
import java.util.function.Consumer;

import edu.drexel.cci.hiyh.ui.InputUI;

public class MenuUtils {
    // TODO See if there's any way to get around having to do this
    @SuppressWarnings("unchecked")
    public static void getMenuNodeChain(final InputUI ui, final MenuNode<?> parent, final Consumer<Object[]> proc, final Class<?> cs[], final Object ps[]) {
        if (cs.length == 0)
            proc.accept(ps);
        else {
            new GetMenuNode(ui, parent, cs[0]) {
                public void success(Object o) {
                    Object newPs[] = Arrays.copyOf(ps, ps.length+1);
                    newPs[newPs.length-1] = o;
                    getMenuNodeChain(ui, this, proc, Arrays.copyOfRange(cs, 1, cs.length), newPs);
                }
            }.run();
        }
    }
    public static void getMenuNodeChain(final InputUI ui, final MenuNode<?> parent, final Consumer<Object[]> proc, final Class<?> cs[]) {
        getMenuNodeChain(ui, parent, proc, cs, new Object[] {});
    }
}
