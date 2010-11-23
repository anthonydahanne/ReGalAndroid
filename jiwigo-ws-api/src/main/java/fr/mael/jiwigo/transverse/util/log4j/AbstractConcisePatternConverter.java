package fr.mael.jiwigo.transverse.util.log4j;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author mael
 *
 */
public abstract class AbstractConcisePatternConverter extends PatternConverter {

    /**
     *
     */
    private int precision;

    /**
     * Constructeur de AbstractConcisePatternConverter.
     * @param formattingInfo .
     * @param precision .
     */
    public AbstractConcisePatternConverter(final FormattingInfo formattingInfo, final int precision) {
	super(formattingInfo);

	this.precision = precision;
    }

    /**
     * @param paramLoggingEvent .
     * @return le nom
     */
    protected abstract String getConciseName(LoggingEvent paramLoggingEvent);

    /**.
     * {@inheritDoc}
     */
    protected final String convert(final LoggingEvent event) {
	final String n = getConciseName(event);

	if (this.precision <= 0) {
	    return n;
	}

	final int len = n.length();

	int end = len - 1;
	final int debut = 46;
	for (int i = this.precision; i > 0; --i) {
	    end = n.lastIndexOf(debut, end - 1);
	    if (end == -1) {
		return n;
	    }
	}
	return n.substring(end + 1, len);
    }

    /**
     * @param className le nom de la class
     * @param maxLength la longueur
     * @return la string
     */
    public static String simplify(final String className, final int maxLength) {
	if (className.length() <= maxLength) {
	    return className;
	}

	final StringBuffer result = new StringBuffer();

	int index = -1;
	while (true) {
	    if (className.indexOf(".", index + 1) == -1) {
		String remainingStr = className.substring(index + 1);

		final int availableLength = maxLength - result.length();

		if (remainingStr.length() > availableLength) {
		    remainingStr = remainingStr.substring(0, availableLength - 1) + '~';
		}

		result.append(remainingStr);

		break;
	    }

	    result.append(className.charAt(index + 1));

	    if (result.length() + 1 == maxLength) {
		result.append('~');

		break;
	    }

	    result.append('.');

	    index = className.indexOf(".", index + 1);
	}

	return result.toString();
    }
}