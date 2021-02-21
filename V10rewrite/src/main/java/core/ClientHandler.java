package core;

import http.EmptyRequestException;
import http.HttpRequest;
import http.HttpResponse;

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
            //1.解析请求
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            //处理请求
            String path = request.getUri();
            System.out.println("uri:"+path);
            File file = new File("./webapps"+path);
            //若该资源存在并且是一个文件，则响应正常
            if(file.exists() && file.isFile()){
                System.out.println("该资源已找到:"+file.getName());
                Map<String,String> map = new HashMap<>();
                map.put("html","text/html");
                map.put("css","text/css");
                map.put("js","application/javascript");
                map.put("png","image/png");
                map.put("gif","image/gif");
                map.put("jpg","image/jpeg");

                String filename = file.getName();
                String last = filename.substring(filename.lastIndexOf(".")+1);
                String type = map.get(last);
                response.putHeader("Content-Type",type);
                response.putHeader("Content-Length",file.length()+"");
                response.setEntity(file);
            }else{
                System.out.println("该资源不存在！");
                File notFoundPage = new File("./webapps/root/404.html");
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                response.putHeader("Content-Type","text/html");
                response.putHeader("Coontent-Length",notFoundPage.length()+"");
                response.setEntity(notFoundPage);
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