<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>日記</title>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<div align="center">
		<h3>同じ日：</h3>
		<s:form action="searchsameday2" theme="simple">
			<table cellpadding="3">

				<tr bgcolor="#eeeeff">
					<td>範囲指定：</td>
					<td>
						<s:select name="startYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.birthYear}" />
						～
						<s:select name="endYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.todayYear}" />
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>調整：</td>
					<td>
						<s:select name="plusminus" list="#{ -2:-2, -1:-1, 0:0, 1:1, 2:2 }" value="0" />
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>パスワード：</td>
					<td>
						<input type="password" name="password" size="10">
					</td>
				</tr>

			</table>
			<s:submit value="検索" />
		</s:form>
		</div>

	</body>
</html>
