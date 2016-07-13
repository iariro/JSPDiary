<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>日記 - は行検索</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>日記検索</h1>
		<div class=hatena-body>
		<div class=main>

		<s:iterator value="results">
			<div class=day>
			<h2><span class=title><s:property value="date" /></span></h2>
			<div class=body>
			<div class=section>

			<s:iterator>
				<h3><li><s:property value="category" /></h3>

				<s:iterator value="lines">
					<s:property escape="false" /><br>
				</s:iterator>
				<br>
			</s:iterator>

			</div>
			</div>
			</div>
		</s:iterator>

		検索結果：<s:property value="size" /><br>

		</div>
		</div>

	</body>
</html>
