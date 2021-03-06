package com.webserver.core;

import com.webserver.http.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *  负责与指定客户端进行HTTP交互
 *  HTTP协议要求与客户端的交互规则采取一问一答的方式。因此，处理客户端交互以3步形式完成:
 *  1:解析请求(一问)
 *  2:处理请求
 *  3:发送响应(一答)
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run() {
        try{
            //1解析请求
            HttpRequest request = new HttpRequest(socket);

            //2处理请求
            String path = request.getUri();
            System.out.println("uri:"+path);
            File file = new File("./webapps"+path);

            if(file.exists() && file.isFile()){
                System.out.println("该资源已找到");
            /*
               一个响应的大致内容:
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
                OutputStream out = socket.getOutputStream();
                //1.发送状态行
                String line = "HTTP/1.1 200 OK";
                byte[] data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);//单独发送回车符
                out.write(10);//单独发送换行符

                //1.发送状态行
                line = "Content-Type: text/html";
                data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);//单独发送回车符
                out.write(10);//单独发送换行符

                line = "Content-Length: " + file.length();
                data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);//单独发送回车符
                out.write(10);//单独发送换行符

                //单独发送回车换行符
                out.write(13);//单独发送回车符
                out.write(10);//单独发送换行符

                FileInputStream fis = new FileInputStream(file);
                int len;
                byte[] buf = new byte[1024*10];//10kb字节数组
                while ((len = fis.read(buf))!=-1){
                    out.write(buf,0,len);
                }

                //若资源不存在则响应404
            }else{
                System.out.println("该资源不存在！");
                File notFoundPage = new File("./webapps/root/404.html");
                OutputStream out = socket.getOutputStream();
                //1.发送状态行
                String line = "HTTP/1.1 404 NotFound";
                byte[] data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);//单独发送回车符
                out.write(10);//单独发送换行符

                //1.发送状态行
                line = "Content-Type: text/html";
                data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);//单独发送回车符
                out.write(10);//单独发送换行符

                line = "Content-Length: " + notFoundPage.length();
                data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);//单独发送回车符
                out.write(10);//单独发送换行符

                //单独发送回车换行符
                out.write(13);//单独发送回车符
                out.write(10);//单独发送换行符

                FileInputStream fis = new FileInputStream(notFoundPage);
                int len;
                byte[] buf = new byte[1024*10];//10kb字节数组
                while ((len = fis.read(buf))!=-1){
                    out.write(buf,0,len);
                }
            }
            //3发送响应

            System.out.println("响应完毕");

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //处理完毕后与客户端断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
