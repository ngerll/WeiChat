package net.cisotec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.*;
import org.n3r.ecaop.client.EcAopClient;
import org.n3r.ecaop.client.EcAopMethod;
import org.n3r.ecaop.client.EcAopResult;
import org.n3r.ecaop.client.SignAlgorithm;

import com.alibaba.fastjson.JSON;




public class GetInfo {
	public String getInfo(String number,String provincecode,String citycode,String nettype,String paytype){
			StringBuffer sbrep = new StringBuffer();
			
			String url = "http://211.94.67.94:8001/openservlet";
			String appkey = "com.aop.app.hbmicrochannel";
			String signSecurity = "swUiuzms+bAqgNOhTaJAyaTFVdAhGaG2aPCUJ0o72P/dSFj8aRbFDOXjPTBB5oja9plfRNWgTKP1OFWBF2F85A==";
			EcAopClient aopClient = new EcAopClient(url, appkey, signSecurity);
			aopClient.setSignAlgorithm(SignAlgorithm.HmacMD5);
			aopClient.setTimeoutMillis(1000000);
			String method = "com.aop.method.balancereport";
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
				String acctbalance = jo.getString("acctbalance"); //�˻����
				String balance = jo.getString("balance"); //�������
				String realfee = jo.getString("realfee");
				
				
				
				JSONArray ja = jo.getJSONArray("acctbalancelist");
				JSONObject job = ja.getJSONObject(0);
				String acctbalanceunavailable = job.getString("acctbalanceunavailable");
				
				
				String sb1 = "�����˻����ͱ�������������£�" + "\n";
				String sb2 = "�˻���" + acctbalance + "Ԫ\n";
				String sb3 = "������" + balance + "Ԫ\n";
				String sb4 = "������" + acctbalanceunavailable + "Ԫ\n";
				String sb5 = "�������ѣ�" + realfee + "Ԫ\n";
				String sb6 = "����������������ʣ��벦��ͻ����߽�����ѯ��лл��";
				
				sbrep.append(sb1);
				sbrep.append(sb2);
				sbrep.append(sb3);
				sbrep.append(sb4);
				sbrep.append(sb5);
				sbrep.append(sb6);
				
				
			}else{
				sbrep.append(jo.get("respcode"));
				
			}
			System.out.println(sbrep.toString());
			
			return sbrep.toString();
						
					
	}
	/* public static void main(String[] args) {
		 String number = "18607106820";
		 GetNumberInfo gni = new GetNumberInfo();
		 gni.getNuminfo(number);
		 String provincecode = gni.getProvincecode();
		 String citycode = gni.getCitycode();
		 String nettype = gni.getNettype();
		 String paytype = gni.getPaytype();
		 
		 new GetInfo().getInfo(number, provincecode, citycode, nettype, paytype);
		 
	}*/
}
