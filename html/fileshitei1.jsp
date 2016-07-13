<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>日記 - ファイル指定</title>
		<meta http-equiv=Content-Style-Type content=text/css>
		<link media=all href="hatena.css" type=text/css rel=stylesheet>
	</head>

	<body>
		<h1>日記</h1>
		<div class=hatena-body>
		<div class=main>

		ファイル指定して表示します<br>

		<s:form action="fileshitei1" theme="simple">
			<table>

				<tr>
					<td bgcolor="#eeeeff">
						ファイルパス：
					</td>
					<td bgcolor="#eeeeff">
						<input type="text" name="filepath" size="100" value="C:\Users\kumagai\Documents\Nefertiti の文書\Private\log\"><br>
					</td>
				</tr>

				<tr>
					<td bgcolor="#eeeeff">
						パスワード：
					</td>
					<td bgcolor="#eeeeff">
						<input type="password" name="password" size="10"><br>
					</td>
				</tr>

				<tr>
					<td bgcolor="#eeeeff">
					</td>
					<td bgcolor="#eeeeff">
						<s:submit value="表示" />
					</td>
				</tr>

				<tr>
					<td bgcolor="#eeeeff">
					</td>
					<td bgcolor="#eeeeff">
						<input type="checkbox" name="usetag" value="on" checked>タグ使用
					</td>
				</tr>

			</table>
		</s:form>

		</div>
		</div>

	</body>
</html>
