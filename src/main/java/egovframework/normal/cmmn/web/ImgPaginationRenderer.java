package egovframework.normal.cmmn.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;


public class ImgPaginationRenderer extends AbstractPaginationRenderer implements ServletContextAware{

	private ServletContext servletContext;

	public ImgPaginationRenderer() {
		// no-op
	}

	/**
	* PaginationRenderer
	*/
	public void initVariables() {

		firstPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_pre10.gif' border=0/></a>&#160;";
		previousPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_pre1.gif' border=0/></a>&#160;";
		currentPageLabel = "<strong>{0}</strong>&#160;";
		otherPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">{2}</a>&#160;";
		nextPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_next1.gif' border=0/></a>&#160;";
		lastPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_next10.gif' border=0/></a>&#160;";
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		initVariables();
	}
}
