<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Home
  Date: 2/23/2018
  Time: 4:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin</title>
</head>
<body>
All products:
<c:forEach items="${products}" var="product">
    <li>${product.name}</li>
</c:forEach>
Add product
<spring:form action="/addProduct" modelAttribute="product" method="post" enctype="multipart/form-data">
    name: <spring:input path="name"/><br>
    price:<input type="number" name="price"><br>
    brand: <spring:select path="brand" items="${brands}" itemLabel="name"/><br>
    category: <spring:select path="category" items="${categories}" itemLabel="name"/> <br>
    picture: <input type="file" class="file" name="picture"><br>
    <input type="submit" value="Save"/><br>
</spring:form><br>
All Brands:
<c:forEach items="${brands}" var="brand">
    <li>${brand.name}</li>
</c:forEach>

Add Brand:
<spring:form action="/addBrand" modelAttribute="brand" method="post">
    name:<spring:input path="name"></spring:input>
    <input type="submit" value="Save"/><br>
</spring:form>
All Categories
<c:forEach items="${categories}" var="category">
    <li>${category.name}</li>
</c:forEach>

Add Category:
<spring:form action="/addCategory" modelAttribute="category" method="post">
    name:<spring:input path="name"></spring:input>
    <input type="submit" value="Save"/><br>
</spring:form>
</body>
</html>
