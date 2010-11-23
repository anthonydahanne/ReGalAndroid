package fr.mael.jiwigo.transverse.util.log4j;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author mael
 *
 */
public class ClassNameConcisePatternConverter extends AbstractConcisePatternConverter {

    /**
     *
     */
    private static final int CLASS_LENGTH = 30;

    /**
     * Constructeur de ClassNameConcisePatternConverter.
     * @param formattingInfo .
     * @param precision .
     */
    public ClassNameConcisePatternConverter(final FormattingInfo formattingInfo, final int precision) {
	super(formattingInfo, precision);
    }

    /**.
     * {@inheritDoc}
     */
    protected final String getConciseName(final LoggingEvent event) {
	return simplify(event.getLocationInformation().getClassName(), CLASS_LENGTH);
    }
}