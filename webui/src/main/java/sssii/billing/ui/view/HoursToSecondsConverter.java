package sssii.billing.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.converter.StringToFloatConverter;

public class HoursToSecondsConverter extends StringToFloatConverter {

    private static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(HoursToSecondsConverter.class);

    public HoursToSecondsConverter(String errorMessage) {
        super(errorMessage);
        log.debug("HoursToSecondsConverter 1");
    }

    public HoursToSecondsConverter(Float emptyValue, String errorMessage) {
        super(emptyValue, errorMessage);
        log.debug("HoursToSecondsConverter 2");
    }

}
