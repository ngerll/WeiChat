package net.cisotec;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.n3r.ecaop.client.EcAopClient;
import org.n3r.ecaop.client.EcAopMethod;
import org.n3r.ecaop.client.EcAopResult;
import org.n3r.ecaop.client.SignAlgorithm;

public class Test {
	public void getInfo(String number,String provincecode,String citycode,String nettype,String paytype){
		
		try {
			String url = "http://211.94.67.94:8001/openservlet";
			String appkey = "com.aop.app.hbmicrochannel";
			String signSecurity = "swUiuzms+bAqgNOhTaJAyaTFVdAhGaG2aPCUJ0o72P/dSFj8aRbFDOXjPTBB5oja9plfRNWgTKP1OFWBF2F85A==";
			EcAopClient aopClient = new EcAopClient(url, appkey, signSecurity);
			aopClient.setSignAlgorithm(SignAlgorithm.HmacMD5);
			aopClient.setTimeoutMillis(1000000);
			String method = "com.aop.method.contractperiodqry";
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
			
			//System.out.println(responText);
			
			JSONObject jo = new JSONObject(responText);
			
			if(jo.getString("respcode").equalsIgnoreCase("0000")){
				JSONArray ja = jo.getJSONArray("activityinfo");
				for(int i = 0;i<ja.length();i++){
					JSONObject joa = ja.getJSONObject(i);
					String activityname = joa.getString("activityname");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					Date effecttime = sdf.parse(joa.getString("effecttime"));
					Date endtime = sdf.parse(joa.getString("endtime"));
					System.out.println(activityname);
					System.out.println(effecttime.toLocaleString());
					System.out.println(endtime.toLocaleString());
				}
			}else{
				System.out.println(jo.getString("respdesc"));
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		 String number = "18696092499";
		 GetNumberInfo gni = new GetNumberInfo();
		 gni.getNuminfo(number);
		 String provincecode = gni.getProvincecode();
		 String citycode = gni.getCitycode();
		 String nettype = gni.getNettype();
		 String paytype = gni.getPaytype();
		/* 
		 System.out.println(provincecode);
		 System.out.println(citycode);
		 System.out.println(nettype);
		 System.out.println(paytype);*/
		 
		 new Test().getInfo(number, provincecode, citycode, nettype, paytype);

	}

}
