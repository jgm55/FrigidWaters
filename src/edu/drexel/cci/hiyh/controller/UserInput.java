package edu.drexel.cci.hiyh.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import edu.drexel.cci.hiyh.ui.Displayable;
import edu.drexel.cci.hiyh.ui.InputUI;

/**
 * Class for obtaining input of type T from the user. They can be chained, so
 * that a single UserInput may represent several choices being made by the
 * user. (For example, a single UserInput<C>, when run, may result in the user
 * asked for an A, and then a B, and then the chosen B is mapped to a C.)
 *
 * The main benefit from using this is the ability for the user to "cancel" a
 * selection and return to the previous one. That is, if one of InputUI's
 * methods returns Optional.empty (indicating that the user has "cancelled" at
 * a particular stage), a UserInput will return to the previous selection. If
 * the first user selection is cancelled, this will result in Optional.empty.
 *
 * Construction of a UserInput follows a monad(ish?) pattern.
 *
 * @param <T> The type of the result of user input
 */
public abstract class UserInput<T> {

    /**
     * Request input from the user and returns the result.
     *
     * @param ui InputUI to use to interact with the user
     * @return An Optional containing the chosen T, or empty if the user
     * cancelled all selections
     */
    public abstract Optional<T> get(InputUI ui);

    /**
     * "Lift" a T to a UserInput<T>.
     *
     * @param t Desired value
     * @return A UserInput<T> that simply returns t, without requiring user
     * input
     */
    public static <T> UserInput<T> of(final T t) {
        return new UserInput<T>() {
            // Only give it once. This way, if the user cancels an input after
            // this, we will return to the one before us instead of being
            // stuck here forever
            private boolean taken = false;
            public Optional<T> get(InputUI ui) {
                if (taken)
                    return Optional.empty();
                taken = true;
                return Optional.of(t);
            }
        };
    }

    /**
     * Have the user select an object from a list.
     *
     * @param items List of objects to choose from
     * @return UserInput representing a choice from items
     */
    public static <T extends Displayable> UserInput<T> fromList(final List<T> items) {
        return new UserInput<T>() {
            public Optional<T> get(InputUI ui) {
                return ui.select(items);
            }
        };
    }

    /**
     * Have the user select an object of a class (for example, an integer).
     *
     * @param c Class of object to choose
     * @return UserInput representing a choice from c
     */
    public static <T> UserInput<T> ofClass(final Class<T> c) {
        return new UserInput<T>() {
            public Optional<T> get(InputUI ui) {
                return ui.get(c);
            }
        };
    }
    
    // TODO move to utility
    private static <T> T[] prepend(T first, T[] rest) {
        LinkedList<T> ll = new LinkedList<T>(Arrays.asList(rest));
        ll.addFirst(first);
        // rest is too small for the new array, so a new one will be allocated
        // it is supplied to maintain type correctness
        return ll.toArray(rest);
    }

    /**
     * Have the user select an object from each of several classes.
     *
     * @param cs Classes of objects to choose
     * @return UserInput representing a choice from each class
     */
    public static UserInput<Object[]> ofClasses(final Class<?>[] cs) {
        if (cs.length == 0)
            return of(new Object[0]);
        final Class<?> first = cs[0];
        final Class<?>[] rest = Arrays.copyOfRange(cs, 1, cs.length);
        return ofClass(first).flatMap(o -> ofClasses(rest).map(os -> prepend(o, os)));
    }

    /**
     * Compose with another UserInput, potentially generated from the result
     * of this one. If the second one is cancelled, the user will get another
     * chance to make a choice for the first one.
     *
     * @param f Function generating the next UserInput from the result of this
     * one
     * @return UserInput representing the two sequential choices
     */
    public <R> UserInput<R> flatMap(final Function<T, UserInput<R>> f) {
        //return new FlatMapUserInput<T,R>(this, f);
        return new UserInput<R>() {
            @Override
            public Optional<R> get(InputUI ui) {
                Optional<T> os;
                while ((os = UserInput.this.get(ui)).isPresent()) {
                    Optional<R> ot = f.apply(os.get()).get(ui);
                    if (ot.isPresent())
                        return ot;
                }
                return Optional.empty();
            }
        };
    }

    /**
     * Compose with a function that transforms the output of this UserInput.
     *
     * @param f Function to apply to the result of this UserInput
     * @return A new UserInput whose result will be the result of f applied to
     * the result of this UserInput
     */
    public <R> UserInput<R> map(final Function<T, R> f) {
        return new UserInput<R>() {
            @Override
            public Optional<R> get(InputUI ui) {
                return UserInput.this.get(ui).map(f);
            }
        };
    }
}
