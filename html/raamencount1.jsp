<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>日記 - ラーメン屋カウント</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>日記 - ラーメン屋カウント</h1>
		<div class=hatena-body>
		<div class=main>

		<s:form action="raamencount2" theme="simple">

			<table>
				<tr>
				<td bgcolor="#eeeeff">
					<s:select name="startYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.todayYear}" />
					～
					<s:select name="endYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.todayYear}" />
				</td>
				</tr>
				<tr>
				<td bgcolor="#eeeeff">
					パスワード：
					<input type="password" name="password" size="10">
				</td>
				</tr>
				<tr>
				<td bgcolor="#eeeeff">
					<s:submit value="送信" />
				</td>
				</tr>
			</table>

		</s:form>

		</div>
		</div>

	</body>

</html>
