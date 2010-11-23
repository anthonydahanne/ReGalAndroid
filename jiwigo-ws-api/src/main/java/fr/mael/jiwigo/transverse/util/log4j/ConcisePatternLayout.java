package fr.mael.jiwigo.transverse.util.log4j;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

/**
 * @author mael
 *
 */
public class ConcisePatternLayout extends PatternLayout {

    /**.
     * {@inheritDoc}
     */
    protected final PatternParser createPatternParser(final String pattern) {
	return new ConcisePatternParser(pattern);
    }
}