package fr.mael.jiwigo.transverse.util.log4j;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;

/**
 * @author mael
 *
 */
public class ConcisePatternParser extends PatternParser {

    /**
     * Constructeur de ConcisePatternParser.
     * @param pattern le pattern a appliquer
     */
    public ConcisePatternParser(final String pattern) {
	super(pattern);
    }

    /**.
     * {@inheritDoc}
     */
    protected final void finalizeConverter(final char c) {
	PatternConverter patternConverter;
	if (c == 'C') {
	    patternConverter = new ClassNameConcisePatternConverter(this.formattingInfo, extractPrecisionOption());

	    this.currentLiteral.setLength(0);

	    addConverter(patternConverter);
	} else if (c == 'l') {
	    patternConverter = new CategoryConcisePatternConverter(this.formattingInfo, extractPrecisionOption());

	    this.currentLiteral.setLength(0);

	    addConverter(patternConverter);
	} else {
	    super.finalizeConverter(c);
	}
    }
}