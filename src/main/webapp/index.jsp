<<<<<<< HEAD
<html>
<body>
<h2>Hello World!</h2>
=======
<%@page contentType="text/html; charset=UTF-8"  %>
<html>
<body>
<h2>Hello World!</h2>

SpringMVC上传文件
<form name="uploadImg" action="/productManager/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="uploadFile">
    <input type="submit" value="SpringMVC上传文件">
</form>

富文本图片上传文件
<form name="uploadRich" action="/productManager/richImageUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="uploadFile">
    <input type="submit" value="富文本图片上传文件">
</form>
>>>>>>> 8f325f344b2bbf8786a27d4a6eaa28c905c54f55
</body>
</html>
