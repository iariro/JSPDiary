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
		<div class=day>

		<table>
			<tr>
				<th>年</th>
				<s:iterator value="countCollection[0]">
					<th width="58"><s:property value="mise" /></th>
				</s:iterator>
			</tr>

			<s:iterator value="countCollection">
				<tr>
					<td><s:property value="year" />年</td>
					<s:iterator>
						<td align="right"><s:property value="count" /></td>
					</s:iterator>
				</tr>
			</s:iterator>

			<s:if test="%{yearCount>2}">
				<tr>
					<td>合計</th>
					<s:iterator value="sum">
						<td align="right"><s:property /></td>
					</s:iterator>
				</tr>
			</s:if>
		</table>

		</div>
		</div>
		</div>

	</body>

</html>
