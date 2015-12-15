package net.cisotec;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.n3r.ecaop.client.EcAopClient;
import org.n3r.ecaop.client.EcAopMethod;
import org.n3r.ecaop.client.EcAopResult;
import org.n3r.ecaop.client.SignAlgorithm;

public class GetNumberInfo {
	
	
	private String provincecode;
	private String citycode;
	private String nettype;
	private String paytype;

	public String getProvincecode() {
		return provincecode;
	}

	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getNettype() {
		return nettype;
	}

	public void setNettype(String nettype) {
		this.nettype = nettype;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public void getNuminfo(String number) {

		String url = "http://211.94.67.94:8001/openservlet";
		String appkey = "com.aop.app.hbmicrochannel";
		String signSecurity = "swUiuzms+bAqgNOhTaJAyaTFVdAhGaG2aPCUJ0o72P/dSFj8aRbFDOXjPTBB5oja9plfRNWgTKP1OFWBF2F85A==";
		EcAopClient aopClient = new EcAopClient(url, appkey, signSecurity);
		aopClient.setSignAlgorithm(SignAlgorithm.HmacMD5);
		aopClient.setTimeoutMillis(1000000);
		String method = "com.aop.method.realnamecheckqry";
		EcAopMethod ecAopMethod = aopClient.createEcAopMethod(method, Map.class);
		HashMap<String, String> reqMap = new HashMap<>();
		reqMap.put("apptx", "test" + System.currentTimeMillis());
		reqMap.put("usernumber", number);

		EcAopResult result = ecAopMethod.exec(reqMap);

		String responText = result.getResponse();
		

		JSONObject jObject = new JSONObject(responText);

		setProvincecode(jObject.getString("provincecode"));
		setCitycode(jObject.getString("citycode"));
		setNettype(jObject.getString("nettype"));
		setPaytype(jObject.getString("paytype"));
		

	}

	
	/*	 public static void main(String[] args) { 
	 	 GetNumberInfo gni = new GetNumberInfo(); gni.getNuminfo("18671312527");
		 System.out.println(gni.getProvincecode());
		 System.out.println(gni.getCitycode());
		 System.out.println(gni.getNettype());
		 System.out.println(gni.getPaytype());
	 
	 }*/
	 

}
