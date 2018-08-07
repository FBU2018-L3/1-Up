package com.l3.one_up.formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XAxisDateValueFormatter implements IAxisValueFormatter {

    private List<Date> mValues;

    public XAxisDateValueFormatter(List<Date> values) {
        this.mValues = values;
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
//        if(Math.ceil(value)==value)
            return new SimpleDateFormat("MM-dd-yy").format(mValues.get((int)value));
//        return "";
    }


}
