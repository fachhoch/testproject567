<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %> 
	
	<html>
		<head>
			<script src="jquery.min.js" type="text/javascript"></script>
			
			<script type="text/javascript"  id="link-script">
				
			$(document).ready(function() { 

				  // Setup the ajax indicator
				  $('body').append('<div id="ajaxBusy"><p><H1>Loading..................................</h1></p></div>');

				  $('#ajaxBusy').css({
				    display:"none",
				    margin:"0px",
				    paddingLeft:"0px",
				    paddingRight:"0px",
				    paddingTop:"0px",
				    paddingBottom:"0px",
				    position:"absolute",
				    right:"3px",
				    top:"3px",
				     width:"auto"
				  });
				});

			$(document).ajaxStart(function(){ 
				  $('#ajaxBusy').show(); 
				}).ajaxStop(function(){ 
				  $('#ajaxBusy').hide();
				});

			
				$(document).ready(function() {
					$("a.link").live('click',function(event){
						var $hiddenInput= $(this).next("input");
						var $p=$hiddenInput.next("p");
						$.ajax({
							type: 		"GET",
							url: 		"/logfile",
							data: 		"id=" + $hiddenInput.attr("value"),
							success:     function(msg) {
								           $p.html(msg);
										 }
							});
							$(this).attr("class","hide");
							event.preventDefault();
						});
					});
				$(document).ready(function() {
					$("a.hide").live('click',function(event){
						var $p=$(this).next("log");
						var $hiddenInput= $(this).next("input");
						var $p=$hiddenInput.next("p");
						$p.toggle();
						event.preventDefault();
						});
					});
			</script>
		</head>
		<body>
				<c:forEach var="map" items="${requestScope.results}">
					<table>
						<c:forEach items="${map}" var="entry">
							<tr>
								<td>
									<a href="#" class="link"  >${entry.value}</a>
									<input type="hidden" value="${entry.key}" >	
									<p class="log" ></p>	
								</td>
							</tr>
				    	</c:forEach>
				    </table>	
				</c:forEach>
		</body>
	</html>	