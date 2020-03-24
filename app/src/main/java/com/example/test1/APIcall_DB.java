package com.example.test1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.TreeMap;

public class APIcall_DB extends APIcall_main {
    /**
     * @throws ParseException
     * @brief Database 기능에 해당하는, 생성된 mysql DB 정보 출력을 위한 함수
     **/
    public static ArrayList<String[]> listMysqlDB() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 18;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());

        String req_message = generateReq(request);

//        System.out.println("Request Message is...");
//        System.out.println(req_message);

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_listinstancesresponse = (JSONObject) obj.get("listinstancesresponse");

        JSONObject parse_listinstancelist= (JSONObject) parse_listinstancesresponse.get("instancelist");

        JSONArray parse_instance = (JSONArray) parse_listinstancelist.get("instance");

        JSONObject instance;
        ArrayList<String[]> list = new ArrayList<String[]>();

        for(int i = 0 ; i < parse_instance.size(); i++) {
            instance = (JSONObject) parse_instance.get(i);
//
//            System.out.println("DB 서버명: "+ instance.get("instancename"));
//            System.out.println("위치: "+ instance.get("zone"));
//            System.out.println("상태: "+ instance.get("instancestatus"));
//            System.out.println("스토리지: "+ instance.get("storagesize") + " GB");
//            System.out.println("마스터/종속장치: "+ instance.get("instancetype"));
//            System.out.println("생성일시: "+ instance.get("instancecreationtime"));
//            System.out.println();

            String dbstatus =null;
            if(((String)instance.get("instancestatus")).equals("Running"))dbstatus="정상";
            else dbstatus = "알수없음";

            //이름, 상태, DB상태(보류), 용량, 생성일, 종속장치, 위치
            list.add(new String[]{(String) instance.get("instancename"),(String)instance.get("instancestatus"),
                    dbstatus, (String)instance.get("storagesize") + " GB", (String)instance.get("instancecreationtime")
                    , (String) instance.get("instancetype"), (String)instance.get("zone")});
        }

        return list;
    }
}
