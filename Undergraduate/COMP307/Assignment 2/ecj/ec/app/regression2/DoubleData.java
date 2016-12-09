package ec.app.regression2;

import ec.*;
import ec.gp.*;
import ec.util.*;

/**
 * Created by Daniel Braithwaite on 20/04/2016.
 */
public class DoubleData extends GPData {
    public double x;

    public void copyTo(final GPData gpd) {
        ((DoubleData) gpd).x = x;
    }
}
