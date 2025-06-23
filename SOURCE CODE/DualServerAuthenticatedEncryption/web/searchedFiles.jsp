<%-- 
    Document   : searchedFiles
    Created on : Jan 24, 2023, 11:32:46 AM
    Author     : Murthi
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="DualServer.SQLconnection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Dual-Server Public-Key Authenticated Encryption with Keyword Search</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="Free HTML Templates" name="keywords">
        <meta content="Free HTML Templates" name="description">

        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Roboto:wght@500;700&display=swap" rel="stylesheet"> 

        <!-- Icon Font Stylesheet -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet" />
        <link href="lib/lightbox/css/lightbox.min.css" rel="stylesheet">

        <!-- Customized Bootstrap Stylesheet -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="css/style.css" rel="stylesheet">
        <link href="css/table.css" rel="stylesheet">
    </head>
    <%
        if (request.getParameter("Success") != null) {%>
    <script>alert('File uploaded to server');</script>  
    <%}
    %>
    <body>

        <!-- Navbar Start -->
        <div class="container-fluid sticky-top bg-dark bg-light-radial shadow-sm px-5 pe-lg-0">
            <nav class="navbar navbar-expand-lg bg-dark bg-light-radial navbar-dark py-3 py-lg-0">
                <a href="#" class="navbar-brand">
                    <h1 class="m-0 text-uppercase text-white" style="font-size: 22px"><i class="bi bi-phone-vibrate text-primary me-2"></i>Dual-Server</h1>
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarCollapse">
                    <div class="navbar-nav ms-auto py-0">
                        <a href="DRHome.jsp" class="nav-item nav-link">Home</a>
                        <a href="Search.jsp" class="nav-item nav-link active">Search Files</a>
                        <a href="RequestStatus.jsp" class="nav-item nav-link">Request Status</a>
                        <a href="DRFileTransactions.jsp" class="nav-item nav-link">File Transactions</a>
                        <a href="logout.jsp" class="nav-item nav-link btn btn-danger">Logout</a>
                    </div>
                </div>
            </nav>
        </div>
        <!-- Navbar End -->



        <!-- Page Header Start -->
        <div class="container-fluid page-header">
        </div>
        <!-- Page Header Start -->


        <!-- Contact Start -->
        <div class="container-fluid py-6 px-5">
            <div class="text-center mx-auto mb-5" style="max-width: 600px;">
                <h1 class="display-5 text-uppercase mb-4"><span class="text-primary">Searched Files</span></h1>
            </div>
            <div class="row gx-0 align-items-center">
                <center><table id="naresh">
                        <tr>
                            <th>Id</th>
                            <th>Keyword</th>
                            <th>File Name</th>
                            <th>Action</th>
                        </tr>
                        <%
                            String word = request.getParameter("sword");
                            String kword = word.replace(" ", "+");
                            Connection con = SQLconnection.getconnection();
                            Statement st = con.createStatement();
                            try {
                                ResultSet rs = st.executeQuery("select * from encindex where kword LIKE '%" + kword + "%' ");
                                while (rs.next()) {
                        %>
                        <tr>
                            <td><%=rs.getString("id")%></td>
                            <td><%=rs.getString("kword")%></td>
                            <td><%=rs.getString("fname")%></td>
                            <td><a href="SearchReq?fid=<%=rs.getString("fid")%>" class="btn btn-success" style="color: white">&nbsp;Request</a></td>
                            
                        </tr>
                        <%                                        }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        %>
                    </table></center> 

            </div>
        </div>
        <br><br><br><br><br><br><br><br><br><br><br>
        <!-- Contact End -->


        <!-- Footer Start -->
        <div class="container-fluid page-header">
        </div>
        <div class="footer container-fluid position-relative bg-dark bg-light-radial text-white-50 px-5">
        </div>
        <div class="container-fluid bg-dark bg-light-radial text-white border-top border-primary px-0">
            <div class="d-flex flex-column flex-md-row justify-content-between">
                <div class="py-4 px-5 text-center text-md-start">
                    <p class="mb-0">&copy; <a class="text-primary" href="#">Dual-Server Public-Key Authenticated Encryption</a>. All Rights Reserved.</p>
                </div>
                <div class="py-4 px-5 bg-primary footer-shape position-relative text-center text-md-end">
                    <p class="mb-0"><a class="text-dark" href="#">Cloud Computing</a></p>
                </div>
            </div>
        </div>
        <!-- Footer End -->


        <!-- Back to Top -->
        <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>


        <!-- JavaScript Libraries -->
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="lib/easing/easing.min.js"></script>
        <script src="lib/waypoints/waypoints.min.js"></script>
        <script src="lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="lib/tempusdominus/js/moment.min.js"></script>
        <script src="lib/tempusdominus/js/moment-timezone.min.js"></script>
        <script src="lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>
        <script src="lib/isotope/isotope.pkgd.min.js"></script>
        <script src="lib/lightbox/js/lightbox.min.js"></script>

        <!-- Template Javascript -->
        <script src="js/main.js"></script>
    </body>

</html>


