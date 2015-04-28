package edu.drexel.cci.hiyh.util;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Optional;

/**
 * A LISP-style linked list of "cons" cells. Good for building a list from
 * tail to head.
 */
public final class Cons<T> extends AbstractCollection<T> {
    public final Optional<Pair<T,Cons<T>>> head;

    private final int len;

    public Cons(T car, Cons<T> cdr) {
        head = Optional.of(new Pair<T,Cons<T>>(car, cdr));
        len = cdr.len + 1;
    }

    public Cons() {
        head = Optional.empty();
        len = 0;
    }

    public Cons(T car) {
        this(car, new Cons<T>());
    }

    public Cons(Iterator<T> elements) {
        if (elements.hasNext()) {
            T car = elements.next();
            Cons<T> cdr = new Cons<T>(elements);
            head = Optional.of(new Pair<T,Cons<T>>(car, cdr));
            len = cdr.len + 1;
        } else {
            head = Optional.empty();
            len = 0;
        }
    }

    public Cons(Iterable<T> elements) {
        this(elements.iterator());
    }

    public Optional<T> car() {
        return head.map(cell -> cell.first);
    }

    public Optional<Cons<T>> cdr() {
        return head.map(cell -> cell.second);
    }

    // Implement as static class to avoid lingering reference to "first" cell
    private static class ConsIterator<T> implements Iterator<T> {
        private Cons<T> current;

        public ConsIterator(Cons<T> current) {
            this.current = current;
        }

        @Override
        public boolean hasNext() {
            return current.head.isPresent();
        }

        @Override
        public T next() {
            T rv = current.car().get();
            current = current.cdr().get();
            return rv;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ConsIterator<T>(this);
    }

    @Override
    public int size() {
        return len;
    }
}
