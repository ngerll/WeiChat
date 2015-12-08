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
	public String getUser(String number){
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
		reqMap.put("provincecode", "071");
		reqMap.put("citycode","710");
		reqMap.put("nettype", "11");
		reqMap.put("paytype", "2");
		//reqMap.put("billdate", "201512");
		
		EcAopResult result = ecAopMethod.exec(reqMap);
		String responText = result.getResponse();
		
		/*System.out.println(responText);
		
		JSONObject jObject = new JSONObject(responText);
		
		String custlvl = jObject.getString("custlvl");
		String custname = jObject.getString("custname");
		String certnum = jObject.getString("certnum");*/
		
		
		
		return responText;
	}

	/*public static void main(String[] args) {
		new GetUser().getUser("18607106820");
	}*/

}
