package egovframework.normal.cmmn;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BoardOthersExcepHndlr implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoardOthersExcepHndlr.class);

	/**
	* @param exception
	* @param packageName
	*/
	@Override
	public void occur(Exception exception, String packageName) {
		LOGGER.debug(" ExceptionHandler run...............");
	}

}
