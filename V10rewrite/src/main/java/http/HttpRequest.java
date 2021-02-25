package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String uri;
    private String protocol;

    private String requestURI;
    private String queryString;
    private Map<String,String> parameter = new HashMap<>();

    private Map<String,String> headers = new HashMap<>();

    private Socket socket;

    public HttpRequest(Socket socket) throws EmptyRequestException {
        this.socket = socket;
        //解析请求行
        parseRequestLine();
        //解析消息头
        parseHeaders();
        //解析消息正文
        parseContent();
    }

    //解析请求行
    private void parseRequestLine() throws EmptyRequestException {
        System.out.println("开始解析请求行。。。");
        try{
            String line = readline();
            if (line.isEmpty()){
                throw new EmptyRequestException();
            }
            System.out.println("请求行:"+line);
            //GET /index.html HTTP/1.1
            String[] data = line.split("\\s");
            method = data[0];
            uri = data[1];
            protocol = data[2];
            parseUri();
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);

        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("HttpRequest:请求行解析完毕！");
    }

    //进一步解析uri
    private void parseUri(){
        try{
            uri = URLDecoder.decode(uri,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        if(uri.contains("?")){
            String[] data = uri.split("\\?");
            requestURI = data[0];
            if(data.length>1){
                queryString = data[1];
                parseParameter(queryString);
            }
        }else{
            requestURI = uri;
        }


        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameter:"+parameter);
    }

    private void parseParameter(String line){
        String[] data = line.split("&");
        for(String para : data){
            String[] paras = para.split("=");
            if(paras.length>1){
                parameter.put(paras[0],paras[1]);
            }else{
                parameter.put(paras[0],null);
            }
        }
    }

    private void parseHeaders(){
        System.out.println("HttpRequest开始解析消息头。。。");
        try{
            while (true) {
                String line = readline();
                if (line.isEmpty()){
                    break;
                }
                System.out.println("消息头:" + line);
                String[] data = line.split("\\s");
                headers.put(data[0], data[1]);
            }
            System.out.println("headers:"+headers);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("HttpRequest:消息头解析完毕！");
    }

    private void parseContent(){
        System.out.println("Httprequest:开始解析消息正文。。。");
        if("post".equalsIgnoreCase(method)){
            String len = headers.get("Content-Length");
            if(len!=null){
                int length =Integer.parseInt(len);
                byte[] data = new byte[length];
                try{
                    InputStream in = socket.getInputStream();
                    in.read(data);
                }catch (IOException e){
                    e.printStackTrace();
                }
                String type = headers.get("Content-Type");
                if(type!=null){
                    if ("application/x-www-form-urlencoded".equals(type)){
                        try{
                            String line = new String(data,"ISO8859-1");
                            line = URLDecoder.decode(line,"UTF-8");
                            System.out.println("");
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


        System.out.println("Httprequest:消息正文解析完毕");
    }

    private String readline() throws IOException {
        InputStream in = socket.getInputStream();
        int d;
        char cur = ' ';
        char pre = ' ';
        StringBuilder builder = new StringBuilder();
        while ((d=in.read())!=-1){
            cur =(char)d;
            if(pre==13 && cur==10){
                break;
            }
            builder.append(cur);
            pre=cur;
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeaders(String name) {
        return headers.get(name);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        return parameter.get(name);
    }
}
