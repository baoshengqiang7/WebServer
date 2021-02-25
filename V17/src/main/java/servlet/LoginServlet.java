package servlet;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:开始处理用户登录。。。");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //输入为空


        //
        try(
        RandomAccessFile raf = new RandomAccessFile("user.dat","r");
        ) {

            for (int i = 0; i < raf.length() / 100; i++) {
                raf.seek(i * 100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data, "UTF-8").trim();
                if (name.equals(username)) {
                    //读取密码
                    raf.read(data);
                    String pwd = new String(data, "UTF-8").trim();
                    if (pwd.equals(password)) {
                        //登录成功
                        File file = new File("./webapps/myweb/login_success.html");
                        response.setEntity(file);
                        return;
                    }
                    break;//只要找到该用户，就停止循环
                }
                        File file = new File("./webapps/myweb/login_fail.html");
                        response.setEntity(file);
            }
            raf.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("LoginServlet:用户登录处理完毕！");
    }
}
