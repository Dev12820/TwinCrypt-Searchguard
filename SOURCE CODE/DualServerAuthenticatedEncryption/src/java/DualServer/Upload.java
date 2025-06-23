/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package DualServer;

import com.oreilly.servlet.MultipartRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Murthi
 */
@MultipartConfig(maxFileSize = 1048576)
public class Upload extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    File file;
    final String filepath = "D:/";
    File file1;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            MultipartRequest m = new MultipartRequest(request, filepath);
            File file = m.getFile("upfile");

            HttpSession user = request.getSession(true);
            String oid = user.getAttribute("doid").toString();
            String oname = user.getAttribute("doname").toString();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);
            System.out.println("current Date " + time);
            String kword = m.getParameter("kword");
            String filename = file.getName();
            String path = file.getPath();
            String extension = "";

            int f = filename.lastIndexOf('.');
            if (f > 0) {
                extension = filename.substring(f + 1);
            }
            String ftype = extension;
            System.out.println("path " + path);
            InputStream inputStream = new FileInputStream(file);

            Random RANDOM = new SecureRandom();
            int PASSWORD_LENGTH = 8;
            String letters = "ABCD12EFGHIJ34KLMN89OPQRST67UVXYZ5";
            String ENKEY1 = "";
            for (int i = 0; i < PASSWORD_LENGTH; i++) {
                int index = (int) (RANDOM.nextDouble() * letters.length());
                ENKEY1 += letters.substring(index, index + 1);
            }
            String ENKEY = ENKEY1;
            FileOutputStream fos = new FileOutputStream("D:\\Enc_" + file.getName());
            FileInputStream fis = new FileInputStream(new File("D:\\Enc_" + file.getName()));
            CipherData s = new CipherData();
            CipherData.encrypt(ENKEY1, inputStream, fos);
            KEYGEN keyen = new KEYGEN();
            Connection conn = SQLconnection.getconnection();
            Connection con = SQLconnection.getconnection();
            Statement st = con.createStatement();
            
             File f1 =new File("D:\\Enc_" + file.getName());
             boolean status1 = new FTPcon().upload(f1);
             System.out.println("file.getName() : "+ f1.getName());
             System.out.println("Cloud Status : "+status1);

            try {
                String sql = "insert into uploads(doid, doname, kword, upfile, time, enkey, fname, ftype) values (?, ?, ?, ?, ?, ?,?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, oid);
                statement.setString(2, oname);
                statement.setString(3, kword);
                if (fos != null) {
                    statement.setBlob(4, fis);
                }
                statement.setString(5, time);
                statement.setString(6, ENKEY);
                statement.setString(7, filename);
                statement.setString(8, ftype);
                int row = statement.executeUpdate();
                System.out.println("AuthorizedSearch.files.Upload.processRequest()" +row );
                if (row > 0) {

                    ResultSet rs = st.executeQuery("Select * from uploads where doid ='" + oid + "' and time='" + time + "'");
                    rs.next();
                    String fid = rs.getString("id");
                    System.out.println("Fid "+fid);
                    String sql1 = "insert into encindex(doid, fid, kword, fname) values (?, ?, ?, ?)";
                    PreparedStatement statement1 = conn.prepareStatement(sql1);
                    statement1.setString(1, oid);
                    statement1.setString(2, fid);
                    statement1.setString(3, keyen.encrypt(kword.toLowerCase()));
                    statement1.setString(4, filename);
                    int row1 = statement1.executeUpdate();

                    response.sendRedirect("FileUpload.jsp?Success");

                } else {
                    response.sendRedirect("FileUpload.jsp?Failed");

                }
            } catch (SQLException ex) {
                Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception e) {
            out.println(e);
        } catch (Throwable ex) {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
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

    public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
    }

    public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }
    }

    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

}
