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
		日記検索<br>
		</div>

		<div align="center">
		<h3>今月：</h3>
		<s:form action="currentmonthdiaryview" theme="simple">
			<table cellpadding="3">
				<tr>
				<td bgcolor="#eeeeff">
					パスワード：
					<input type="password" name="password" size="10">
					<input type="submit" value="検索">
					<input type="checkbox" name="useTag" value="on" checked>タグ使用
				</td>
				</tr>
			</table>
		</s:form>
		</div>

		<div align="center">
		<h3>検索：</h3>
		<s:form action="searchdiary2" theme="simple">
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

				<s:select name="startYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.beforeYear}" />

				<s:select name="startMonth" list="yearMonthSelection.months" listKey="value" listValue="displayString" value="%{yearMonthSelection.beforeMonth}" />

				から

				<s:select name="endYear" list="yearMonthSelection.years" listKey="value" listValue="displayString" value="%{yearMonthSelection.todayYear}" />

				<s:select name="endMonth" list="yearMonthSelection.months" listKey="value" listValue="displayString" value="%{yearMonthSelection.todayMonth}" />

				まで
				</td>
				</tr>

				<tr bgcolor="#eeeeff">
				<td>オプション</td>
				<td>
				<input type="hidden" name="useTag" value="on">タグ使用<br>
				<input type="checkbox" name="digest">ダイジェスト表示<br>
				<input type="checkbox" name="topicOnly" checked>該当トピックのみ絞り込み<br>
				</td>
				</tr>

				<tr bgcolor="#eeeeff">
				<td>パスワード：</td>
				<td>
				<input type="password" name="password" size="10">
				</td>
				</tr>

			</table>
			<input type="submit" value="検索">
		</s:form>
		</div>

		<ul>
			<li><a href="searchinterval1.action">間隔検索</a><br>
			<li><a href="searchsameday1.action">同じ日検索</a><br>
			<li><a href="densityview1.action">密度グラフ</a><br>
			<li><a href="monthlygraph1.action">月毎グラフ</a><br>
			<li><a href="categorylist1.action">カテゴリ一覧</a><br>
			<li><a href="searchempty1.action">空データ検索</a><br>
			<li><a href="searchhagyou1.action">は行検索</a><br>
			<li><a href="fileshitei1.jsp">ファイル指定</a>
			<li><a href="raamencount1.action">ラーメン屋カウント</a>
		</ul>

	</body>
</html>
