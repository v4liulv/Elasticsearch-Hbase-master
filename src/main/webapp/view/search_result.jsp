<%--
  Created by IntelliJ IDEA.
  User: liulv
  Date: 2019/9/4
  Time: 15:41
  
  Description: 
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.sinobest.eshbasetest.domain.PageUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
<head>
    <title>search_result</title>

    <style type="text/css">
        table.gridtable {
            font-family: verdana,arial,sans-serif;
            font-size:18px;
            color:#333333;
            border-width: 1px;
            border-color: #666666;
            border-collapse: collapse;
            width: 85%;
            margin: auto;
            border: none;
        }
        table.gridtable caption{
            font-family: verdana,arial,sans-serif;
            font-size:22px;
            color:#333333;
            border-width: 1px;
            border-color: #242424;
            border-collapse: collapse;;
        }

        table.gridtable th {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #666666;
            background-color: #dedede;
        }
        table.gridtable td {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #666666;
            background-color: #ffffff;
        }
    </style>
</head>
<body>
<div class="search_result_show">
    <c:if test="${ empty page.list }">
        <h4>ES搜索无结果，请重新输入搜索条件！！！</h4>
    </c:if>

    <c:if test="${! empty page.list }">
        <h3>ES搜索为您找到相关结果约${total}个</h3>

        <table class="gridtable">
            <caption>文章列表</caption>  <!--标题-->
            <tr>
                <th>标题</th>
                <th>简介</th>
                <th>作者</th>
            </tr>
            <c:forEach items="${page.list}" var="bean">
            <tr>
                <td><a href="javascript:void(0);" class="detail" id="${bean.id}" onclick="detail(this)">${bean.title}</a></td>
                <td>${bean.describe}</td>
                <td>${bean.author}</td>
            </tr>
            </c:forEach>
        </table>

        <c:if test="${page.hasPrevious }">
            <a href="javascript:void(0);" id="pageNum=${kw}&${page.previousPageNum }" onclick="page(this)"> 上一页</a>
        </c:if>
        <c:forEach begin="${page.everyPageStart }" end="${page.everyPageEnd }" var="n">
            <a href="javascript:void(0);" id="${kw}&${n }" onclick="page(this)"> ${n }</a>
        </c:forEach>

        <c:if test="${page.hasNext }">
            <a href="javascript:void(0);" id="${kw}&${page.nextPageNum }" onclick="page(this)"> 下一页</a>
        </c:if>
    </c:if>
</div>

</body>
</html>
