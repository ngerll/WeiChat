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
		
		String getjson = new GetInfo().getInfo(getContent);
		
		JSONObject jo = new JSONObject(getjson);
		
		if(jo.getString("respcode").equalsIgnoreCase("0000")){
		
		
			JSONArray ja = jo.getJSONArray("feepolicyaddupinfo");
			
			
		
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("您的套餐使用情况如下：" + "\n");
			
			for(int i = 0;i<ja.length();i++){
				JSONObject feep = new JSONObject(ja.get(i).toString());
			
				String feepolicyname = feep.getString("feepolicyname");
				String xusedvalue = feep.getString("xusedvalue");
				String xcanusevalue = feep.getString("xcanusevalue");
				String xexceedvalue = feep.getString("xexceedvalue");
				sBuffer.append(feepolicyname + "已使用" + xusedvalue + "，还剩余" + xcanusevalue + "，超出套餐" + xexceedvalue + "。\n");
			}
			sendContent = sBuffer.toString();
		}else{
			sendContent = jo.getString("respdesc");
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