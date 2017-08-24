package utility;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by SAI on 5/18/2016.
 */
public class InputFilterMinMax implements InputFilter {

    private Double min, max;

    public InputFilterMinMax(Double min, Double max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(int min, int max) {
        this.min = (double) min;
        this.max = (double) max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Double.parseDouble(min);
        this.max = Double.parseDouble(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            Logger.debug((dest.toString() + source.toString()));
            Double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(min, max, input)) {
                String strInput = String.valueOf(input);
                int pointIndex = strInput.indexOf(".");
                if (pointIndex == -1)
                    return null;
                else {
                    int noOfDegitAfterPoint = strInput.length() - pointIndex - 1;
                    if (noOfDegitAfterPoint <= 2)
                        return null;
                    else
                        return "";
                }
            }
        } catch (NumberFormatException nfe) {
            Logger.writeToCrashlytics(nfe);
        }
        return "";
    }

    private boolean isInRange(Double a, Double b, Double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
