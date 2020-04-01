package com.example.test1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class APIcall_Autoscaling extends APIcall_main {

    public APIcall_Autoscaling(){
        this.baseurl = "https://api.ucloudbiz.olleh.com/autoscaling/v2/client/api?";
    }

    /**
     * @throws ParseException
     * @brief 매니지먼트 기능에 해당하는, AutoScaling 출력을 위한 함수
     **/
    public ArrayList<String[]> listAutoscaling() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 13;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());


        String req_message = generateReq(request);
//
//        System.out.println("Request Message is...");
//        System.out.println(req_message);

        JSONObject obj = readJsonFromUrl(req_message);

        JSONObject parse_listautoscalinggroupsresponse = (JSONObject) obj.get("listautoscalinggroupsresponse");

        JSONArray parse_autoscalinggroups = (JSONArray) parse_listautoscalinggroupsresponse.get("autoscalinggroups");

        JSONObject autoscalinggroup;

        ArrayList<String[]> list = new ArrayList<String[]>();
        String zoneid = null;
        for (int i = 0; i < parse_autoscalinggroups.size(); i++) {

            autoscalinggroup = (JSONObject) parse_autoscalinggroups.get(i);

            JSONArray availabilityzones = (JSONArray) autoscalinggroup.get("availabilityzones");

            for (int j = 0; j < availabilityzones.size(); j++) {
                zoneid = String.valueOf(availabilityzones.get(j));

                if (zoneid.equals(Seoul_M_zoneid)) zone = "Seoul-M";
                else if (zoneid.equals(Seoul_M2_zoneid)) zone = "Seoul-M2";
                else if (zoneid.equals(CentralB_zoneid)) zone = "Central-B";
                else if (zoneid.equals(CentralA_zoneid)) zone = "Central-A";
                else if (zoneid.equals(HA_zoneid)) zone = "HA";
                else if (zoneid.equals(West_zoneid)) zone = "US-West";


                System.out.println(" " + zone);
            }

            String autoscalingtype = String.valueOf(autoscalinggroup.get("autoscalingtype"));
            if (autoscalingtype.equals("Run")) autoscalingtype = "VM On/Off";
            else if (autoscalingtype.equals("Deploy")) autoscalingtype = "VM 생성/삭제";

//            System.out.println("동작 옵션: " + autoscalingtype);

            JSONArray instance = (JSONArray) autoscalinggroup.get("instances");

            String desiredcapacity = String.valueOf(autoscalinggroup.get("desiredcapacity"));
            String activationNum = desiredcapacity;


            //이름, 상태, 위치, 현재 vm, 목표 vm, 최소 vm, 최대 vm
            list.add(new String[]{(String) autoscalinggroup.get("autoscalinggroupname"), (String) autoscalinggroup.get("status"),
                    zone, String.valueOf(instance.size()), desiredcapacity, String.valueOf(autoscalinggroup.get("minsize")),
                    String.valueOf(autoscalinggroup.get("maxsize"))});
        }
        return list;
    }


    /**
     * @param autoScalingGroupName 목표 VM 수를 변경할 AutoScaling 그룹 이름
     * @param desiredCapacity      목표 VM 수
     * @throws ParseException
     * @brief 매니지먼트 기능에 해당하는, 오토 스케일링 그룹의 목표 VM 수 조절을 위한 함수
     **/
    public String updateDesiredCapacity(String autoScalingGroupName, String desiredCapacity) throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 14;

        TreeMap<String, String> request = new TreeMap<String, String>();

        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());
        request.put("autoscalinggroupname", autoScalingGroupName);
        request.put("desiredcapacity", desiredCapacity);

        String req_message = generateReq(request);
//
//        System.out.println("Request Message is...");
//        System.out.println(req_message);

        JSONObject obj = readJsonFromUrl(req_message);

        JSONObject parse_setdesiredcapacityresponse = (JSONObject) obj.get("setdesiredcapacityresponse");

        JSONObject parse_responsemetadata = (JSONObject) parse_setdesiredcapacityresponse.get("responsemetadata");
        String result = "";

        if (parse_responsemetadata.size() > 0) {
            result = "목표 VM 수 변경 성공";
        } else result = "목표 VM 수 변경 실패";

        return result;
    }

}
