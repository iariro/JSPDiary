<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>日記 - 空検索</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>日記</h1>
		<div class=hatena-body>
		<div class=main>

		<s:iterator value="results">
			<div class=day>
				<h3><span class=title><s:property /></span></h3>
			</div>
		</s:iterator>

		<br>
		検索結果：<s:property value="size" /><br>

		</div>
		</div>
	</body>

</html>
