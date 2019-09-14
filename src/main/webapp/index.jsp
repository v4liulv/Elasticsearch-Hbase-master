<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta charset="utf-8">
  <base href="<%=basePath%>">

  <title>ES starting page</title>
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
  <meta http-equiv="description" content="This is my page">

  <script type="text/javascript" charset="utf-8" src="<%=basePath%>/static/plugs/jquery-3.3.1.min.js"></script>
  <script type="text/javascript" charset="utf-8" src="<%=basePath%>/static/plugs/jquery.spin.merge.js"></script>

  <script type="text/javascript" charset="utf-8" src="<%=basePath%>/static/js/index.js"></script>
  <script type="text/javascript" charset="utf-8" src="<%=basePath%>/static/js/detail.js"></script>
  <script type="text/javascript" charset="utf-8" src="<%=basePath%>/static/js/page.js"></script>

</head>
<body>

<div style="text-align: center; margin-top: 100px" class="center">
<div id="">
  <label>
    <input type="text" style="height: 34px; width: 400px" name="keyWords" id="keyWords"/>
  </label>
  <input id="apply_link_form" type="submit" style="height: 34px; width: 100px" value="ES搜索一下">
  <input type="hidden" value="1" name="pageNum" id="pageNum">
</div>

  <div class="search_result">
  </div>
</div>
</body>
</html>
