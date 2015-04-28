package edu.drexel.cci.hiyh.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import edu.drexel.cci.hiyh.has.device.ParamType;
import edu.drexel.cci.hiyh.ui.Displayable;
import edu.drexel.cci.hiyh.ui.InputUI;
import edu.drexel.cci.hiyh.util.Cons;

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
    public abstract Optional<T> get(InputUI ui) throws InterruptedException;

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
            public Optional<T> get(InputUI ui) throws InterruptedException {
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
    public static <T> UserInput<T> ofParamType(final ParamType<T> p) {
        return new UserInput<T>() {
            public Optional<T> get(InputUI ui) throws InterruptedException {
                return ui.get(p);
            }
        };
    }
    
    /**
     * Have the user select an object from each of several classes.
     *
     * @param cs Classes of objects to choose
     * @return UserInput representing a choice from each class
     */
    public static <T> UserInput<List<Object>> ofParamTypes(final List<ParamType<?>> ps) {
        return ofParamTypes(new Cons<ParamType<?>>(ps)).map(os -> new ArrayList<Object>(os));
    }

    public static <T> UserInput<Cons<Object>> ofParamTypes(final Cons<ParamType<?>> ps) {
        if (ps.isEmpty())
            return of(new Cons<Object>());
        return ofParamType(ps.car().get()).flatMap(o -> ofParamTypes(ps.cdr().get()).map(os -> new Cons<Object>(o, os)));
    }

    /*
    private static class FMUserInput<T,R> extends UserInput<R> {
        private UserInput<T> parent;
        private Function<T, UserInput<R>> f;
        public FMUserInput(UserInput<T> parent, Function<T, UserInput<R>> f) {
            this.parent = parent;
            this.f = f;
        }
        @Override
        public Optional<R> get(InputUI ui) throws InterruptedException {
            Optional<T> os;
            while ((os = parent.get(ui)).isPresent()) {
                Optional<R> ot = f.apply(os.get()).get(ui);
                if (ot.isPresent())
                    return ot;
            }
            return Optional.empty();
        }
        @Override
        public <Q> UserInput<Q> flatMap(final Function<T, UserInput<Q>> g) {
            return new FMUserInput<T,Q>(FMUserInput
        }
    }*/

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
        return new UserInput<R>() {
            @Override
            public Optional<R> get(InputUI ui) throws InterruptedException {
                Optional<T> os;
                while ((os = UserInput.this.get(ui)).isPresent()) {
                    Optional<R> ot = f.apply(os.get()).get(ui);
                    if (ot.isPresent())
                        return ot;
                }
                return Optional.empty();
            }
            @Override
            public <Q> UserInput<Q> flatMap(final Function<R, UserInput<Q>> g) {
                // Unless we do this, we'll get the "tree" built in the wrong
                // order--with "left-associativity."
                return UserInput.this.flatMap(t -> f.apply(t).flatMap(g));
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
            public Optional<R> get(InputUI ui) throws InterruptedException {
                return UserInput.this.get(ui).map(f);
            }
            @Override
            public <Q> UserInput<Q> flatMap(final Function<R, UserInput<Q>> g) {
                return UserInput.this.flatMap(t -> of(f.apply(t)).flatMap(g));
            }
        };
    }
}
