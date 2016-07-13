<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>日記 - 指定日</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>日記</h1>
		<div class=hatena-body>
		<div class=main>

		<s:property value="date" /> 一日分を表示します<br>

		<s:form action="oneday2" theme="simple">

			<input type="hidden" name="date" value="<s:property value="date" />">

			<table>
				<tr>
				<td bgcolor="#eeeeff">
					パスワード：
					<input type="password" name="password" size="10">
				</td>
				</tr>

				<tr>
				<td bgcolor="#eeeeff">
					<input type="submit">
				</td>
				</tr>

				<tr>
				<td bgcolor="#eeeeff">
					カテゴリー：
					<input type="text" name="category" value="<s:property value="category" />">
				</td>
				</tr>

				<tr>
				<td bgcolor="#eeeeff">
					タグ使用：
					<input type="checkbox" name="usetag" value="on" checked>
				</td>
				</tr>

			</table>

		</s:form>

		</div>
		</div>
	</body>
</html>
