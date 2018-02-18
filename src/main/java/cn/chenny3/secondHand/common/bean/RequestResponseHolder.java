package cn.chenny3.secondHand.common.bean;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Component
public class RequestResponseHolder {
    private ThreadLocal<RequestResponseWrap> threadLocal=new ThreadLocal<>();

    public void setRequest(HttpServletRequest request){
        if(threadLocal.get()==null){
            threadLocal.set(new RequestResponseWrap());
        }

        threadLocal.get().setRequest(request);
    }

    public void setResponse(HttpServletResponse response){
        if(threadLocal.get()==null){
            threadLocal.set(new RequestResponseWrap());
        }

        threadLocal.get().setResponse(response);
    }

    public HttpServletResponse getResponse(){
        if(threadLocal.get()==null){
            return null;
        }

        return threadLocal.get().getResponse();
    }

    public HttpServletRequest getRequest(){
        if(threadLocal.get()==null){
            return null;
        }

        return threadLocal.get().getRequest();
    }

    public void clear(){
        threadLocal.remove();
    }



    class RequestResponseWrap extends HashMap<String,Object>{
        public void setRequest(HttpServletRequest request){
            put("request",request);
        }

        public void setResponse(HttpServletResponse response){
            put("response",response);
        }

        public HttpServletResponse getResponse(){
            return (HttpServletResponse) get("response");
        }

        public HttpServletRequest getRequest(){
            return (HttpServletRequest) get("request");
        }
    }
}
