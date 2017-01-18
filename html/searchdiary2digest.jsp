<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>

	<head>
		<title>日記 - 検索</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		日記<br>
		<div class=hatena-body>
		<div class=main>

		分類：<s:property value="category" /><br>
		フレーズ：<s:property value="phrase" /><br>
		件数：<s:property value="size" /><br>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

		<table>
			<tr>
				<th align="left">年</th>
				<s:iterator begin="1" end="12">
					<th><s:property />月</th>
				</s:iterator>
				<th>合計</th>
			</tr>
			<s:iterator value="monthlyCountTable">
				<tr>
					<th><s:property value="year" /></th>
					<s:iterator value="count" var="c">
						<td align="right">
							<s:if test="#c>0">
								<s:property />
							</s:if>
						</td>
					</s:iterator>
					<td align="right"><s:property value="totalCount" /></th>
				</tr>
			</s:iterator>
			<tr>
				<th>計</th>
				<s:iterator value="monthlyCountTable.monthlySum">
					<th><s:property /></th>
				</s:iterator>
				<th><s:property value="size" /></th>
			</tr>
		</table>

		<table>
			<tr>
				<th>日付</th>
				<th>カテゴリ</th>
				<th>１行目</th>
				<th>２行目</th>
			</tr>
			<s:iterator value="results">
				<s:iterator>
					<tr>
						<td><s:property value="date" /></td>
						<td><s:property value="category" /></td>
						<td><s:property value="line1" escape="false" /></td>
						<td><s:property value="line2" escape="false" /></td>
					</tr>
				</s:iterator>
			</s:iterator>
		</table>

		</div>
		</div>
		</div>

		</div>
		</div>

	</body>

</html>
