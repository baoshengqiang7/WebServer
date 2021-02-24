package http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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
        /*
            uri会存在两种情况:含有参数和不含有参数
            不含有参数的样子如:/myweb/index.html
            含有参数的样子如:/myweb/regUser?username=fancq&password=xxx......
            因此我们要对uri进一步拆分,需求如下:
            如果uri不含有参数,则不需要拆分,直接将uri的值赋值给requestURI即可.

            如果uri含有参数,则需要进行拆分:
            1:将uri按照"?"拆分为两部分,左侧赋值给requestURI,右侧赋值给queryString
            2:在将queryString部分按照"&"拆分出每一组参数,然后每一组参数再按照"="拆分为
              参数名和参数值,并将参数名作为key,参数值作为value保存到parameter这个Map中
              完成解析工作.
         */
        //判断uri是否含有参数
        if(uri.contains("?")){
            String[] data = uri.split("\\?");
            requestURI = data[0];
            // http://localhost:8088/myweb/regUser?
            if(data.length>1){
                queryString = data[1];
                //username=fancq&password=123456&nickname=chuanqi&age=22
                //拆分每一组参数
                data = queryString.split("&");
                for(String para : data){
                    //username=fancq
                    //按照=拆分参数名与参数值
                    String[] paras = para.split("=");
                    if(paras.length>1){
                        parameter.put(paras[0],paras[1]);
                    }else{
                        parameter.put(paras[0],null);
                    }
                }
            }
        }else{
            requestURI = uri;
        }


        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameter:"+parameter);
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

    private void parseContent(){}

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
