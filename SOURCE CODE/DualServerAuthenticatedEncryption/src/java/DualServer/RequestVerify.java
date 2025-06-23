/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package DualServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Murthi
 */
public class RequestVerify extends HttpServlet {

   public static final String PUBLIC_KEY_FILE = "Public.key";
    public static final String PRIVATE_KEY_FILE = "Private.key";


    //Exceution times Encrypt
    public long encryt_extime = 0;
    public long dcrypt_extime = 0;

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
            String id = request.getParameter("id");
            String fid = request.getParameter("fid");
            String drid = request.getParameter("drid");
            String drmail = request.getParameter("drmail");
            String path = "D://DSReceiver";
            String pathf = path + drid;
            System.out.println("id ---> " + id);
            System.out.println("uid ---> " + drid);
            System.out.println("mail ---> " + drmail);
            System.out.println("pathf ---> " + pathf);
            Connection conn = SQLconnection.getconnection();
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            byte[] d = null;
            ResultSet rs = st.executeQuery("SELECT * from datareceiver where id = '" + drid + "' ");
            if (rs.next()) {
                d = rs.getBytes("vparam");

                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048); //1024 used for normal securities
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                PublicKey publicKey = keyPair.getPublic();
                PrivateKey privateKey = keyPair.getPrivate();

                System.out.println("\n----------------DECRYPTION STARTED------------");
                long sTime = System.nanoTime();
                byte[] descryptedData = null;

                PrivateKey privateKey1 = readPrivateKeyFromFile(PRIVATE_KEY_FILE, pathf);
                PublicKey publicKey1 = readPublicKeyFromFile(PUBLIC_KEY_FILE, pathf);
                byte[] challenge = new byte[10000];
                ThreadLocalRandom.current().nextBytes(challenge);

// sign using the private key
                Signature sig = Signature.getInstance("SHA256withRSA");
                sig.initSign(privateKey1);
                sig.update(challenge);
                byte[] signature = sig.sign();

// verify signature using the public key
                sig.initVerify(publicKey1);
                sig.update(challenge);

                boolean keyPairMatches = sig.verify(signature);
                System.out.println("keyPairMatches  status : " + keyPairMatches);

                if (keyPairMatches == false) {
                    int k = st1.executeUpdate("update searchreq set vstatus='Failed' where id='" + id + "'");
                    response.sendRedirect("ASFileRequests.jsp?Failed");
                    conn.close();

                }

                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey1);
                descryptedData = cipher.doFinal(d);
                dcrypt_extime = (System.nanoTime() - sTime) / 1000000;
                System.out.println("Decrypted Data: " + new String(descryptedData));
                boolean status = new String(descryptedData).isEmpty();
                System.out.println("Dec  status : " + status);

                System.out.println("Dec  Performance : " + dcrypt_extime + " msec");

                System.out.println("----------------DECRYPTION COMPLETED------------");

                if (drmail.equalsIgnoreCase(new String(descryptedData))) {
                    int i = st2.executeUpdate("update searchreq set vstatus='Verified' where id='" + id + "'");

                    response.sendRedirect("ASFileRequests.jsp?Verified");

                } else {
                    int j = st3.executeUpdate("update searchreq set vstatus='Failed' where id='" + id + "'");
                    response.sendRedirect("ASFileRequests.jsp?Failed");
                }
            } else {
                response.sendRedirect("ASFileRequests.jsp?Something");
            }

        } catch (SQLException ex) {
            Logger.getLogger(RequestVerify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RequestVerify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(RequestVerify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(RequestVerify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(RequestVerify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(RequestVerify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(RequestVerify.class.getName()).log(Level.SEVERE, null, ex);
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
    }// </editor-fold>// </editor-fold>

    /**
     * read Public Key From File
     *
     * @param fileName
     * @param pathf
     * @return PublicKey
     * @throws IOException
     */
    public PublicKey readPublicKeyFromFile(String fileName, String pathf) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(pathf + "//" + fileName));
            ois = new ObjectInputStream(fis);

            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();

            //Get Public Key
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);

            return publicKey;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }

    /**
     * read Public Key From File
     *
     * @param fileName
     * @param pathf
     * @return
     * @throws IOException
     */
    public PrivateKey readPrivateKeyFromFile(String fileName, String pathf) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            System.out.println("key Path---> " + pathf + "//" + fileName);
            fis = new FileInputStream(new File(pathf + "//" + fileName));
            System.out.println("key Path---> " + pathf + "//" + fileName);
            ois = new ObjectInputStream(fis);

            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();

            //Get Private Key
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = fact.generatePrivate(rsaPrivateKeySpec);

            System.out.println("Check private key----- " + privateKey);

            return privateKey;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }
}
