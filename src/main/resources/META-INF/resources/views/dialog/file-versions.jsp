<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../common/taglibs.jsp" %>
<ul class="list-group list-group-unbordered m-b-none">
<c:forEach var="version" items="${data}">
	<li class="list-group-item ">
		<div class="version-item">
			<span class="label label-default ">${version.version}</span> <span>${version.author}</span> - <span style="font-style:italic;">${version.description}</span>
		</div>
	</li>
</c:forEach>
<c:if test="${empty data}">
	<li class="list-group-item ">
		<div class="version-item">
			<span style="font-style:italic;">No Version information</span>
		</div>
	</li>
</c:if>
</ul>
