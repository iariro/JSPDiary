<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>日記</title>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>日記 - エラー</h1>
		<div class=hatena-body>
		<div class=main>

		<s:property value="message" /><br>
		<br>
		例外：<br>
		<li><s:property value="exception" /><br>

		</div>
		</div>

	</body>
</html>
