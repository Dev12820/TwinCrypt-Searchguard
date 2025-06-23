/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package DualServer;

import static DualServer.CipherData.decrypt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Murthi
 */
public class Download extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String fileid = request.getParameter("fid");
            String dkey = request.getParameter("dkey");
            String id = request.getParameter("id");
            String pathf = "D:\\";
            Connection con = SQLconnection.getconnection();
            Statement st = con.createStatement();
            Statement st1 = con.createStatement();
            Statement sto = con.createStatement();
            HttpSession session = request.getSession(true);
            String drname = (String) session.getAttribute("drname");
            String drid = (String) session.getAttribute("drid");
            String drmail = (String) session.getAttribute("drmail");
            ResultSet rt = st.executeQuery("select * from uploads where id='" + fileid + "' and enkey ='"+dkey+"'");

            if (rt.next()) {
                String fname = rt.getString("fname");
                String enkey = rt.getString("enkey");
                String oid = rt.getString("doid");
                String oname = rt.getString("doname");
                String ftype = rt.getString("ftype");
                Blob blob = rt.getBlob("upfile");
                InputStream in = blob.getBinaryStream();
                File newFolder = new File(pathf + drmail);

                boolean created = newFolder.mkdirs();
                FileOutputStream fos2 = new FileOutputStream(pathf + drmail + "\\" + fname);
                String p = pathf + drmail + "\\" + fname;
                decrypt(enkey, in, fos2);
                FileInputStream fis = new FileInputStream(new File(pathf + drmail + "\\" + fname));

                File f = new File(pathf + drmail + "\\" + fname);
                InputStream is = new FileInputStream(f);

                Calendar cal = Calendar.getInstance();
                DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String time = dateformat.format(cal.getTime());
                System.out.println("download time:" + time);

               int k= st1.executeUpdate("insert into downloads (drid, drname, doid, doname, fid, fname, time)values('" + drid + "','" + drname + "','" + oid + "','" + oname + "','" + fileid + "','" + fname + "','" + time + "')");
                
                BufferedReader br = new BufferedReader(new FileReader(f));
                StringBuilder sb1 = new StringBuilder();
                String temp = null;

                while ((temp = br.readLine()) != null) {
                    sb1.append(temp + "\n");
                }

                String text = sb1.toString();
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fname + "" + "\"");
                out.write(text);
                out.close();
                rt.close();
                con.close();

                response.sendRedirect("RequestStatus.jsp?path");
            } else {
               response.sendRedirect("RequestStatus.jsp?InvalidKey");

            }

        } catch (SQLException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
/**
 * Handles the HTTP <code>GET</code> method.
 *
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
