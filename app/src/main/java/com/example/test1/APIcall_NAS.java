package com.example.test1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.TreeMap;

public class APIcall_NAS extends APIcall_main {
    /**
     * @throws ParseException
     * @brief NAS 기능에 해당하는, 생성된 NAS 출력을 위한 함수
     **/
    public static ArrayList<String[]> listNas() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 21;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());


        String req_message = generateReq(request);
//
//        System.out.println("Request Message is...");
//        System.out.println(req_message);

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_listvolumesresponsee = (JSONObject) obj.get("listvolumesresponse");

        JSONArray parse_response = (JSONArray) parse_listvolumesresponsee.get("response");

        JSONObject response;

        ArrayList<String[]> list = new ArrayList<String[]>();

        for(int i = 0 ; i < parse_response.size(); i++) {
            response = (JSONObject) parse_response.get(i);

            String zone = String.valueOf(response.get("zoneid"));
            if(zone.equals(Seoul_M_zoneid)) zone = "KOR-Seoul M";
            else if(zone.equals(Seoul_M2_zoneid)) zone = "KOR-Seoul M2";
            else if(zone.equals(CentralB_zoneid)) zone = "KOR-Central B";
            else if(zone.equals(CentralA_zoneid)) zone = "KOR-Central A";
            else if(zone.equals(HA_zoneid)) zone = "KOR-HA";
            else if(zone.equals(West_zoneid)) zone = "US-West";

            int totalsize = ((int) ( ((long) response.get("totalsize"))/Math.pow(10, 11))) * 100;
            int usedsize =  (int) ((((long) response.get("usedsize"))/Math.pow(10, 9)));

            //이름, 위치, 신청용량, 현재 사용량, 프로토콜
            list.add(new String[]{(String) response.get("name"),zone,
                    String.valueOf(totalsize), String.valueOf(usedsize), (String) response.get("volumetype")});
        }
        return list;
    }
}
