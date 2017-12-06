<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Channel</title>
<%@ include file="/WEB-INF/partials/headerStyle.jspf"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">

		<!-- Main Header -->
		<%@ include file="/WEB-INF/partials/header.jspf"%>
		<!-- Left side column. contains the logo and sidebar -->
		<%@ include file="/WEB-INF/partials/sidebar.jspf"%>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header">
				<h1>Channel</h1>
			</section>

			<!-- Main content -->
			<section class="content">

				<div class="row">
					<div class="box box-primary">
						<p>Channel attributes</p>
						<p>ID: ${channel.getId()}</p>
						<p>NAME: ${channel.getName()}</p>
					</div>
					<div class="box box-primary">
						<p>Session attributes</p>
						<p>ID: ${session.getId()}</p>
						<p>CH_ID: ${session.getChannelId()}</p>
						<p>START: ${session.getStartDate()}</p>
						<p>END: ${session.getEndDate()}</p>
					</div>
				</div>
				<!-- /.row -->

			</section>
			<!-- /.content -->
		</div>
		<!-- /.content-wrapper -->

		<!-- Main Footer -->
		<%@ include file="/WEB-INF/partials/footer.jspf"%>

	</div>
	<!-- ./wrapper -->

	<!-- Script library -->
	<%@ include file="/WEB-INF/partials/scriptLib.jspf"%>
</body>
</html>