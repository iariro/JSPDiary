<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>日記 - 編集</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>日記 - 編集</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

		<h2><span class=title>
		<s:property value="dateWithYoubi" />
		</span></h2>

		<s:form action="editdiary2" theme="simple">
			<s:textarea name="text"/>
			<br>
			<input type="hidden" name="date" value="<s:property value="date" />">
			<input type="hidden" name="password" value="<s:property value="password" />">
			<s:submit value="上書き" />
		</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
