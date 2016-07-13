<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>日記検索</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>カテゴリ一覧</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<table>
				<s:iterator value="ranking">
					<tr>
						<td><s:property value="key" /></td>
						<td><s:property value="value" /></td>
					</tr>
				</s:iterator>
			</table>

		</div>
		</div>
		</div>

	</body>
</html>
