package com.alumni.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.WebRowSet;

import com.alumni.entity.Alumni;
import com.jufeng.database.DBManage;
import com.jufeng.util.filter.StringFilter;

@WebServlet("/Search")
public class Search extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Search() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String keyWords = (request.getParameter("searchkey")==null? "" : request.getParameter("searchkey").trim());
		
		String sql = "SELECT * FROM TB_ALUMNI WHERE (STUNUM LIKE '%"+keyWords+"%' OR STUNAME LIKE '%"+keyWords+"%' OR WORKUNIT LIKE '%"+keyWords+"%' OR ADDRESS LIKE '%"+keyWords+"%')";
		sql += " ORDER BY STUNUM";
		
		System.out.println("SQL:" + sql);
		List<Alumni> alumniList = new ArrayList<Alumni>();
		
		try {
			WebRowSet rs = DBManage.getWRS(sql);
			while(rs.next()){
				Alumni alumni = new Alumni();
				alumni.setStuNum(rs.getString("STUNUM"));
				alumni.setStuName(StringFilter.cover2htmlBlank(rs.getString("STUNAME")));
				alumni.setWorkUnit(StringFilter.cover2htmlBlank(rs.getString("WORKUNIT")));
				alumni.setTel(rs.getString("TEL"));
				alumni.setPhone(rs.getString("PHONE"));
				alumniList.add(alumni);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		if(keyWords.length()<1){
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.println("<HTML>");
			out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
			out.println("  <BODY>");
			out.println("	<script>alert('请输入查询关键字');history.back();</script>");
			out.println("  </BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
		}else{
			request.setAttribute("alumnis", alumniList);
			request.getRequestDispatcher("searchout.jsp").forward(request, response);
		}
		
		return;
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
