package com.example.test1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.TreeMap;

public class APIcall_Autoscaling extends APIcall_main {
    /**
     * @throws ParseException
     * @brief 매니지먼트 기능에 해당하는, AutoScaling 출력을 위한 함수
     **/
    public static ArrayList<String[]> listAutoscaling() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 19;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());


        String req_message = generateReq(request);

        System.out.println("Request Message is...");
        System.out.println(req_message);

        JSONObject obj = readJsonFromUrl(req_message);

        JSONObject parse_listautoscalinggroupsresponse = (JSONObject) obj.get("listautoscalinggroupsresponse");

        JSONArray parse_autoscalinggroups = (JSONArray) parse_listautoscalinggroupsresponse.get("autoscalinggroups");

        JSONObject autoscalinggroup;

        ArrayList<String[]> list = new ArrayList<String[]>();
        String zone = null;
        for (int i = 0; i < parse_autoscalinggroups.size(); i++) {

            autoscalinggroup = (JSONObject) parse_autoscalinggroups.get(i);

            System.out.println("이름: " + autoscalinggroup.get("autoscalinggroupname"));
            System.out.println("상태: " + autoscalinggroup.get("status"));
            System.out.println("VM 생성 설정: " + autoscalinggroup.get("launchconfigurationname"));


            System.out.print("위치:");

            JSONArray availabilityzones = (JSONArray) autoscalinggroup.get("availabilityzones");

            for (int j = 0; j < availabilityzones.size(); j++) {
                zone = String.valueOf(availabilityzones.get(j));

                if (zone.equals(Seoul_M_zoneid)) zone = "KOR-Seoul M";
                else if (zone.equals(Seoul_M2_zoneid)) zone = "KOR-Seoul M2";
                else if (zone.equals(CentralB_zoneid)) zone = "KOR-Central B";
                else if (zone.equals(CentralA_zoneid)) zone = "KOR-Central A";
                else if (zone.equals(HA_zoneid)) zone = "KOR-HA";
                else if (zone.equals(West_zoneid)) zone = "US-West";

                System.out.println(" " + zone);
            }

            String autoscalingtype = String.valueOf(autoscalinggroup.get("autoscalingtype"));
            if (autoscalingtype.equals("Run")) autoscalingtype = "VM On/Off";
            else if (autoscalingtype.equals("Deploy")) autoscalingtype = "VM 생성/삭제";

            System.out.println("동작 옵션: " + autoscalingtype);

            JSONArray instance = (JSONArray) autoscalinggroup.get("instances");

            String desiredcapacity = String.valueOf(autoscalinggroup.get("desiredcapacity"));
            String activationNum = desiredcapacity;

            if (String.valueOf(autoscalinggroup.get("status")).equals("Ready"))
                activationNum = "N/A";
            else desiredcapacity = "N/A";

            //이름, 상태, 위치, 현재 vm, 목표 vm, 최소 vm, 최대 vm
            list.add(new String[]{(String) autoscalinggroup.get("autoscalinggroupname"), (String) autoscalinggroup.get("status"),
                    zone, String.valueOf(instance.size()), desiredcapacity, (String)autoscalinggroup.get("minsize"),
                    (String)autoscalinggroup.get("maxsize")});
        }
        return list;
    }
}
