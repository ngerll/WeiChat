package net.cisotec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;

import sun.net.www.content.image.jpeg;

/**
 * Servlet implementation class WeiChat
 */
@WebServlet("/WeiChat")
public class WeiChat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeiChat() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		String token = "cisotec";
		
		PrintWriter pw = response.getWriter();
		
		String[] str = {token,timestamp,nonce};
		
		Arrays.sort(str);
		String bigstr = str[0] + str[1] + str[2];
		
		String digest = new SHA1().getDigestOfString(bigstr.getBytes()).toLowerCase();
		if(digest.equalsIgnoreCase(signature)){
			pw.print(echostr);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		
		String sendMsgtype;
		String fromUsername;
		String toUsername;
		String sendTime = new Date().getTime() + "";
		String sendContent;
		String getContent;
		
		
		SAXReader reader = new SAXReader();
		Document document = null;
		Reader rd = new InputStreamReader(request.getInputStream(), "utf-8");
		
		try {
			document = reader.read(rd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Element element = document.getRootElement();
		fromUsername = element.elementText("FromUserName");
		toUsername = element.elementText("ToUserName");
		getContent = element.elementText("Content");
		
		sendMsgtype = "text";
		
		GetNumberInfo gni = new GetNumberInfo();
		
		gni.getNuminfo(getContent);
		
		String provincecode = gni.getProvincecode();
		String citycode = gni.getCitycode();
		String nettype = gni.getNettype();
		String paytype = gni.getPaytype();
		
		
		
		
		String getinfo = new GetInfo().getInfo(getContent, provincecode, citycode, nettype, paytype);
			
		String custname = new GetUser().getUser(getContent, provincecode, citycode, nettype, paytype);
		
		
		
		if(custname != null && getinfo != null){

			StringBuffer sBuffer = new StringBuffer();
			
			sBuffer.append("尊敬的 " + custname + "\n");
			sBuffer.append(getinfo);		
			
			sendContent = sBuffer.toString();
		}else{
			sendContent = "尊敬的用户，系统异常，请稍后再试，谢谢！";
		}
		
			
		String xmltext = "<xml>"
				+ "<ToUserName><![CDATA[%1$s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%2$s]]></FromUserName>"
				+ "<CreateTime>%3$s</CreateTime>"
				+ "<MsgType><![CDATA[%4$s]]></MsgType>"
				+ "<Content><![CDATA[%5$s]]></Content>"
				+ "<FuncFlag>0</FuncFlag>" + "</xml>";
		
		String sendxml = String.format(xmltext, fromUsername,toUsername,sendTime,sendMsgtype,sendContent);
		pw.write(sendxml);
		rd.close();
		
	}

}