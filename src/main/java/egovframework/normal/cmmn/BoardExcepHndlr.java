package egovframework.normal.cmmn;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BoardExcepHndlr implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoardExcepHndlr.class);

	/**
	* @param ex
	* @param packageName
	*/
	@Override
	public void occur(Exception ex, String packageName) {
		LOGGER.debug(" ExceptionHandler run...............");
	}
}
