<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>

	<head>
		<title>日記 - 検索</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>日記</h1>
		<div class=hatena-body>
		<div class=main>

		分類：<s:property value="category" /><br>
		フレーズ：<s:property value="phrase" /><br>
		件数：<s:property value="size" /><br>

		<s:iterator value="results">
			<div class=day>
			<h2><span class=title><s:property value="date" /></span></h2>
			<div class=body>
			<div class=section>

			<s:form action="editdiary1" theme="simple">
				<input type="hidden" name="date" value="<s:property value="date1" />">
				<input type="hidden" name="password" value="<s:property value="password" />">
				<s:submit value="編集" />
			</s:form>

			<s:iterator>
				<h3><s:property value="category" /></h3>

				<s:iterator value="lines">
					<s:property escape="false" />
				</s:iterator>
			</s:iterator>

			</div>
			</div>
			</div>
		</s:iterator>

		</div>
		</div>

	</body>

</html>
