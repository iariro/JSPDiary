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

			<s:if test="%{results.size()>0}">
				<s:iterator value="results">

					<div class=day>
					<h2><span class=title><s:property value="date" /></span></h2>
					<div class=body>
					<div class=section>

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
			</s:if>
			<s:else>
				<font color="gray">なし</font>
			</s:else>

		</div>
		</div>
	</body>
</html>
