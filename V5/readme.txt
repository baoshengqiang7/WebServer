本版本对代码进行重构，完成功能拆分。
上一个版本我们在CilentHandler中完成了请求的解析。现在要将该操作拆分出去，使得ClientHandler只负责
流程控制，而具体的解析工作交给HttpRrquest来完成。

设计一个类:HttpRequest,使该类的每一个实例表示客户端发过来的一个请求内容，并且解析请求的工作也放在这
个类中完成。

实现:
1.新建一个包:com.webserver.http
  这个包存放所有和HTTP协议相关的类
2.在http包中新建一个类:HttpRequest，请求对象。
  在这个类上我们将请求的相关信息设计为属性来分别保存。
  并在构造方法中完成解析请求的工作
3.ClientHandler第一步只需要实现HttpRequest就完成了解析请求工作。