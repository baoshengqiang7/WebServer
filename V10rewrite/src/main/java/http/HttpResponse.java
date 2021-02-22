package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private int statusCode = 200;
    private String statusReason = "OK";

    private Map<String, String> headers = new HashMap<>();

    private File entity;

    private Socket socket;

    public HttpResponse(Socket socket) {
        this.socket = socket;
    }

    public void flush() {
        sendStatusLine();
        sendHeaders();
        sendContent();

    }

    //1.发送状态行
    private void sendStatusLine() {
        System.out.println("HttpResponse:开始发送状态行。。。");
        try {
            OutputStream out = socket.getOutputStream();
            String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
            System.out.println("状态行:" + line);
            println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpResponse:状态行发送完毕！");
    }

    //发送响应头
    private void sendHeaders() {
        System.out.println("HttpResponse:开始发送响应头。。。");
        try {
//            Set<Map.Entry<String,String>> set = headers.entrySet();
//            for (Map.Entry<String,String> e : set){
//                String name = e.getKey();
//                String value = e.getValue();
//                String line = name + ": " + value;
//                System.out.println("响应头:"+line);
//                println(line);
//            }
            headers.forEach(
                    (k, v) -> {
                        try {
                            String line = k + ": " + v;
                            System.out.println("响应头:" + line);
                            println(line);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
            println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpResponse:响应头发送完毕！");
    }

    private void sendContent() {
        System.out.println("HttpResponse:开始发送消息正文。。。");
        try (
                FileInputStream fis = new FileInputStream(entity);
        ) {
            OutputStream out = socket.getOutputStream();
            int len;
            byte[] ops = new byte[1024 * 10];
            while ((len = fis.read(ops)) != -1) {
                out.write(ops, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpResponse:响应正文发送完毕！");
    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes("ISO8859-1");
        out.write(data);
        out.write(13);
        out.write(10);
    }

    public void putHeader(String name, String value) {
        headers.put(name, value);
    }

    public File getEntity() {
        return entity;
    }

    public void setEntity(File entity) {
        this.entity = entity;
        String lin = entity.getName();
        String ext = lin.substring(lin.lastIndexOf(".") + 1);
        String value = HttpContext.getMimeType(ext);
        putHeader("Content-Type",value);
        putHeader("Content-Length",entity.length()+"");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
}


