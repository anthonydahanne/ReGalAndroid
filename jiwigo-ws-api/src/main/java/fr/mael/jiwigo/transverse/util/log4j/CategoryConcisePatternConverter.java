package fr.mael.jiwigo.transverse.util.log4j;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author mael
 *
 */
public class CategoryConcisePatternConverter extends AbstractConcisePatternConverter {

    /**
     *
     */
    private static final int ROLE_LENGTH = 30;
    /**
     *
     */
    private static final int HINT_LENGTH = 8;

    /**
     *
     */
    private static final int SIZE = 35;

    /**
     * Constructeur de CategoryConcisePatternConverter.
     * @param formattingInfo .
     * @param precision .
     */
    public CategoryConcisePatternConverter(final FormattingInfo formattingInfo, final int precision) {
	super(formattingInfo, precision);
    }

    /**.
     * {@inheritDoc}
     */
    protected final String getConciseName(final LoggingEvent event) {
	final StringBuffer result = new StringBuffer();

	appendConciseCategory(result, event.getLoggerName());

	appendLeftSpaces(result);

	return result.toString();
    }

    /**
     * @param sb la string a modifier
     * @param loggerName le nom du logger
     */
    private void appendConciseCategory(final StringBuffer sb, final String loggerName) {
	final int colonIndex = loggerName.indexOf(58);

	if (colonIndex == -1) {
	    sb.append(simplify(loggerName, ROLE_LENGTH));

	    return;
	}

	final String roleName = loggerName.substring(0, colonIndex);

	final String hintName = loggerName.substring(colonIndex + 1);

	// sb.append(simplify(roleName,
	// 20)).append(":").append(simplify(hintName, 8));
	sb.append(roleName).append(":").append(hintName);
    }

    /**
     * @param sb la string a modifier
     */
    private void appendLeftSpaces(final StringBuffer sb) {
	while (sb.length() < SIZE) {
	    sb.append(" ");
	}
    }
}