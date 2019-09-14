<jsp:useBean id="Doc" scope="request" class="com.sinobest.eshbasetest.domain.Doc"/>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>ES搜索-详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

    <style type="text/css">
        .Content-Main{
            max-width: 1200px;
            margin: auto;
            border: none;
            border-radius: 5px;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
           /* font: 12px "Helvetica Neue", Helvetica, Arial, sans-serif;
            text-shadow: 1px 1px 1px #444;
            color: #D3D3D3;*/
           /* background: #555;*/
        }
        .Content-Main h1{
            padding: 8px 0px 40px 10px;
            display: block;
            border-bottom: 1px solid #444;
        }
        .Content-Main label{
            margin: 0px 0px 5px;
            display: block;
        }
        .Content-Main label>span{
            width: 20%;
            float: left;
            text-align: right;
            padding-right: 10px;
            margin-top: 10px;
            font-weight: bold;
        }
        .Main-sex input[type=checkbox]{
            margin-top:6px;
            vertical-align:middle;
        }
        .Content-Main input[type="text"],.Content-Main input[type="email"],.Content-Main textarea{
            height: 35px;
            width: 70%;
            line-height: 15px;
            padding: 5px 0px 5px 5px;
            margin-bottom: 16px;
            margin-right: 6px;
            margin-top: 2px;
            border: none;
            border-radius:2px;
            -webkit-border-radius:2px;
            -moz-border-radius:2px;
            outline: 0 none;
            background:  #DFDFDF;
            color: #525252;
        }
        .Content-Main textarea{
            height: 100px;
            width: 70%;
            padding: 5px 0px 0px 5px;
        }
    </style>
</head>

<body>

<div class="Content-Main">
        <h1>文章详情</h1>
        <label>
            <span>标题 :</span>
            <input type="text"  name="name" disabled="disabled"  value="${Doc.title}">
        </label>
        <label>
            <span>作者 :</span>
            <input type="email" name="email" disabled="disabled" value=" ${Doc.author}">
        </label>
        <label>
            <span>描述 :</span>
            <input type="text" name="phone" disabled="disabled" value="${Doc.describe}">
        </label>
        <label>
            <span>内容 :</span>
            <textarea id="message" name="message" disabled="disabled" cols="200"> ${Doc.content}</textarea>
        </label>
        <label>
            <span>创建时间 :</span>
            <input type="text" name="phone" disabled="disabled" value=" ${Doc.cjsj}">
        </label>
</div>
</body>
</html>
