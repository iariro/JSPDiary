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
		<h1>日記検索</h1>
		</div>

		<div align="center">
		<h3>検索：</h3>
		<s:form action="searchdiary2" theme="simple">
			<table cellpadding="3">
				<tr bgcolor="#eeeeff">
					<td>パスワード：</td>
					<td>
					<input type="password" name="password" size="10">
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>分類：</td>
					<td>
					<input type="text" name="category" value="<s:property value="category" />"><br>
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>フレーズ：</td>
					<td>
					<input type="text" name="phrase" value="<s:property value="phrase" />"><br>
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>範囲指定：</td>
					<td>
					<input type="text" name="startYear" size="4" value="<s:property value="startYear" />"> /
					<input type="text" name="startMonth" size="2" value="<s:property value="startMonth" />"> から
					<input type="text" name="endYear" size="4" value="<s:property value="endYear" />"> /
					<input type="text" name="endMonth" size="2" value="<s:property value="endMonth" />"> まで
					</td>
				</tr>

				<tr bgcolor="#eeeeff">
					<td>オプション</td>
					<td>
					<input type="checkbox" name="useTag" value="on" checked>タグ使用<br>
					<input type="checkbox" name="digest">ダイジェスト表示<br>
					</td>
				</tr>

			</table>
			<input type="submit" value="検索">
		</s:form>
		</div>

	</body>
</html>
