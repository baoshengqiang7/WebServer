package core;

import http.EmptyRequestException;
import http.HttpRequest;
import http.HttpResponse;
import servlet.RegServlet;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){this.socket = socket;}

    public void run(){
        try{
            //1解析请求
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);

            //2处理请求
            //首先通过request获取请求中的抽象路径中的请求部分
            String path = request.getRequestURI();

            //首先判断本次请求是否为请求某个业务
            if("/myweb/regUser".equals(path)){
                //处理注册业务
                RegServlet servlet = new RegServlet();
                servlet.service(request,response);
            }else {
                File file = new File("./webapps" + path);
                //若该资源存在并且是一个文件，则正常响应
                if (file.exists() && file.isFile()) {
                    System.out.println("该资源已找到:" + file.getName());
                    response.setEntity(file);

                    //若资源不存在则响应404
                } else {
                    System.out.println("该资源不存在!");
                    File notFoundPage = new File("./webapps/root/404.html");
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                    response.setEntity(notFoundPage);
                }
            }

            //统一设置其他响应头
            response.putHeader("Server","WebServer");

            //发送响应
            response.flush();

            System.out.println("响应发送完毕！");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
