package net.cisotec;

import java.util.HashMap;
import java.util.Map;

import org.n3r.ecaop.client.EcAopClient;
import org.n3r.ecaop.client.EcAopMethod;
import org.n3r.ecaop.client.EcAopResult;
import org.n3r.ecaop.client.SignAlgorithm;

import sun.net.www.content.image.jpeg;

import org.json.*;

public class GetUser {
	public String getUser(String number,String provincecode,String citycode,String nettype,String paytype){
		String username = null;
		String url = "http://211.94.67.94:8001/openservlet";
		String appkey = "com.aop.app.hbmicrochannel";
		String signSecurity = "swUiuzms+bAqgNOhTaJAyaTFVdAhGaG2aPCUJ0o72P/dSFj8aRbFDOXjPTBB5oja9plfRNWgTKP1OFWBF2F85A==";
		EcAopClient aopClient = new EcAopClient(url, appkey, signSecurity);
		aopClient.setSignAlgorithm(SignAlgorithm.HmacMD5);
		aopClient.setTimeoutMillis(1000000);
		String method = "com.aop.method.custinfo";
		EcAopMethod ecAopMethod = aopClient.createEcAopMethod(method, Map.class);
		HashMap<String, String> reqMap = new HashMap<>();
		reqMap.put("apptx", "test" + System.currentTimeMillis());
		reqMap.put("usernumber", number);	
		reqMap.put("provincecode", provincecode);
		reqMap.put("citycode",citycode);
		reqMap.put("nettype", nettype);
		reqMap.put("paytype", paytype);	
		
		EcAopResult result = ecAopMethod.exec(reqMap);
		String responText = result.getResponse();
		
		JSONObject jo = new JSONObject(responText);
		
		if(jo.getString("respcode").equalsIgnoreCase("0000")){
			username = jo.getString("custname");
		}else{
			username = jo.getString("respcode");
		}
		
		
		
		return username;
	}

	/*public static void main(String[] args) {
		new GetUser().getUser("18607106820");
	}*/

}
