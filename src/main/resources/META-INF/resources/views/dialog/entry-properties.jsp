<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../common/taglibs.jsp" %>
<table class="table table-condensed m-b-none">
	<thead>
		<tr>
			<th>Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="prop" items="${propmap}">
		<tr>
			<td>${prop.key}</td>
			<td>${prop.value}</td>
		</tr>
	</c:forEach>
	<c:if test="${empty propmap}">
		<tr>
			<td colspan="2">No Property information</td>
		</tr>
	</c:if>
	</tbody>
</table>
