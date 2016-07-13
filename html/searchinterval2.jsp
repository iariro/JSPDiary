<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>
			日記検索 -
			<s:property value="category" />
			<s:property value="phrase" />
		</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>日記検索</h1>
		<div class=hatena-body>
		<div class=main>
		分類：<s:property value="category" /><br>
		フレーズ：<s:property value="phrase" /><br>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

		<table>
			<tr><th>日</th><th>間隔</th></tr>

			<s:iterator value="dateAndIntervalCollection">
				<tr>
					<td><s:property value="date" /></td>
					<td><s:property value="interval" /></td>
				</tr>
			</s:iterator>

		</table>

		</div>
		</div>
		</div>

		</div>
		</div>

	</body>
</html>
