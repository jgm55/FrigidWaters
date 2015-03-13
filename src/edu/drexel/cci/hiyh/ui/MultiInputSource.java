package edu.drexel.cci.hiyh.ui;

/**
 * Multiplexes multiple BooleanInput Sources.
 *
 * Sorry, guys... going for ease of use here.
 */
public class MultiInputSource extends AbstractBooleanInputSource
                              implements BooleanInputSource.Listener {
    private BooleanInputSource[] sources;

    public MultiInputSource(BooleanInputSource... sources) {
        this.sources = sources;
    }

    public void initAndStart(InputUI ui) {
        for (BooleanInputSource s : sources) {
            s.initAndStart(ui);
            s.addListener(this);
        }
    }

    public void onBooleanInput() {
        notifyListeners();
    }
}
