<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %> 
	
	<table>	
		<c:forEach var="line" items="${requestScope.results}">
			<tr>
				<td>
					${line} 
				</td>
			</tr>
		</c:forEach>
	</table>