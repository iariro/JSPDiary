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
		<h3>は行検索：</h3>
		<s:form action="searchhagyou2" theme="simple">
			<table cellpadding="3">
				<tr bgcolor="#eeeeff">
					<td>範囲指定：</td>
					<td>
						<s:select name="startYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.beforeYear}" />

						<s:select name="startMonth" list="yearMonthSelection.months" listKey="value" listValue="displayString" value="%{yearMonthSelection.beforeMonth}" />

						から

						<s:select name="endYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.todayYear}" />

						<s:select name="endMonth" list="yearMonthSelection.months" listKey="value" listValue="displayString" value="%{yearMonthSelection.todayMonth}" />

						まで
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>パスワード：</td>
					<td>
						<input type="password" name="password" size="10">
					</td>
				</tr>
			</table>
			<s:submit value="集計"/>
		</s:form>
		</div>
	</body>
</html>
