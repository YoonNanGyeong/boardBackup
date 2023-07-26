package egovframework.normal.cmmn.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import java.text.MessageFormat;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;


public class ImgPaginationRenderer extends AbstractPaginationRenderer implements ServletContextAware{

	private ServletContext servletContext;

	public ImgPaginationRenderer() {
		// no-op
	}
	
	

	@Override
	public String renderPagination(PaginationInfo paginationInfo, String jsFunction) {
		StringBuffer strBuff = new StringBuffer();

		int firstPageNo = paginationInfo.getFirstPageNo();
		int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList();
		int totalPageCount = paginationInfo.getTotalPageCount();
		int pageSize = paginationInfo.getPageSize();
		int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList();
		int currentPageNo = paginationInfo.getCurrentPageNo();
		int lastPageNo = paginationInfo.getLastPageNo();
		
		if (totalPageCount > pageSize) {
			if (firstPageNoOnPageList > pageSize) {
				strBuff.append(MessageFormat.format(firstPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
				strBuff.append(MessageFormat.format(previousPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList - 1) }));
			}else if(firstPageNoOnPageList == currentPageNo) {
				strBuff.append(MessageFormat.format(firstPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
				strBuff.append(MessageFormat.format(previousPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
			} else {
				strBuff.append(MessageFormat.format(firstPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
				strBuff.append(MessageFormat.format(previousPageLabel, new Object[] { jsFunction, Integer.toString(currentPageNo - 1) }));
			}
		}

		for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
			if (i == currentPageNo) {
				strBuff.append(MessageFormat.format(currentPageLabel, new Object[] { Integer.toString(i) }));
			} else {
				strBuff.append(MessageFormat.format(otherPageLabel, new Object[] { jsFunction, Integer.toString(i), Integer.toString(i) }));
			}
		}

		if (totalPageCount > pageSize) {
			if (lastPageNoOnPageList < totalPageCount) {
				strBuff.append(MessageFormat.format(nextPageLabel, new Object[] { jsFunction, Integer.toString(currentPageNo + 1) }));
				strBuff.append(MessageFormat.format(lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
			} else {
				strBuff.append(MessageFormat.format(nextPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
				strBuff.append(MessageFormat.format(lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
			}
		}

		return  strBuff.toString();
	}



	/**
	* PaginationRenderer
	*/
	public void initVariables() {

		firstPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_pre1.png' border=0/></a>&#160;";
		previousPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_next1.png' border=0/></a>&#160;";
		currentPageLabel = "<span><strong>{0}</strong></span>&#160;";
		otherPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\"><button type=\"button\" class=\"page-btns\">{2}</button></a>&#160;";
		nextPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_next10.png' border=0/></a>&#160;";
		lastPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_pre10.png' border=0/></a>&#160;";
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		initVariables();
	}
}
