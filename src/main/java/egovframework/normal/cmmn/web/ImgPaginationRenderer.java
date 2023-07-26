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

		firstPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_next1.png' border=0/></a>&#160;";
		previousPageLabel = "<a href=\"javascript:void(0)\" onclick=\"{0}({1}); return false;\">" + "<image src='" + servletContext.getContextPath() + "/images/board/cmmn/btn_page_pre1.png' border=0/></a>&#160;";
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
