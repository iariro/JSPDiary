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
		<h3>グラフ：</h3>
		<s:form action="densityview2" theme="simple">
			<table cellpadding="3">
				<tr bgcolor="#eeeeff">
					<td>分類：</td>
					<td>
						<input type="text" name="category"><br>
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>フレーズ：</td>
					<td>
						<input type="text" name="phrase"><br>
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>範囲指定：</td>
					<td>
						<s:select name="startYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.birthYear}" />
						～
						<s:select name="endYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.todayYear}" />
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>パスワード：</td>
					<td>
					<input type="password" name="password" size="10">
					</td>
				</tr>

			</table>
			<input type="submit" value="計算">
		</s:form>
		</div>

	</body>
</html>
