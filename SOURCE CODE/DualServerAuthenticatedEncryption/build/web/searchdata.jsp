<%-- 
    Document   : searchdata
    Created on : Jan 24, 2023, 11:25:41 AM
    Author     : Murthi
--%>
<%@page import="DualServer.KEYGEN"%>
<%@page import="DualServer.SQLconnection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String word = request.getParameter("kword");
    KEYGEN k =new KEYGEN();
    
    String kword=k.encrypt(word.toLowerCase());

    Connection con = SQLconnection.getconnection();
    Statement st = con.createStatement();
    try {

        ResultSet rs = st.executeQuery("select * from encindex where kword LIKE '%" + kword + "%' ");
        if (rs.next()) {
          response.sendRedirect("searchedFiles.jsp?sword="+kword);
        } else {
            response.sendRedirect("Search.jsp?Not");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
%>
