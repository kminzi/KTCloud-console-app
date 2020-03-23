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

public class APIcall_LB extends APIcall_main{
    /**
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ParseException
     * @brief 서버의 zoneid와 vm id로 Displayname을 받아오는 함수
     * @param zoneid displayname을 알기를 원하는 서버의 zoneid
     * @param id displayname을 알기를 원하는 서버의 id
     * @return zoneid와 id이 일치하는 서버의 displayname, 찾지못했을 경우 null 반환
     **/
    public static String getDisplaynameById(String zoneid, String id) throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {
        String displayName = "";
        int button = 2;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());

        request.put("zoneid", zoneid);



        String req_message = generateReq(request);

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_listvirtualmachinesresponse = (JSONObject) obj.get("listvirtualmachinesresponse");

        // 조회된 서버가 하나 이상일 경우에만 출력, 메트릭 조회 가능
        if(parse_listvirtualmachinesresponse.size() > 0 ) {

            JSONArray parse_virtualmachine = (JSONArray) parse_listvirtualmachinesresponse.get("virtualmachine");

            JSONObject virtualmachine;

            for(int i = 0 ; i < parse_virtualmachine.size(); i++) {

                virtualmachine = (JSONObject) parse_virtualmachine.get(i);

                if(String.valueOf(virtualmachine.get("id")).equals(id)) {
                    displayName = String.valueOf(virtualmachine.get("displayname"));
					/*
					System.out.println("(For DisplayName)Request Message is...");
					System.out.println(req_message);
					System.out.println("DisplayName= " + displayName);
					System.out.println();
					*/
                    return displayName;
                }

            }
        }
        return displayName;
    }


    /**
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ParseException
     * @brief Networking 기능에 해당하는, LB 리스트 출력을 위한 함수
     **/
    public static ArrayList<String[]> listLB() throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {
        int button = 16;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());

        String req_message = generateReq(request);
//
//        System.out.println("Request Message is...");
//        System.out.println(req_message);

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_listloadbalancersresponse = (JSONObject) obj.get("listloadbalancersresponse");

        JSONArray parse_loadbalancer = (JSONArray) parse_listloadbalancersresponse.get("loadbalancer");

        JSONObject loadbalancer;

        ArrayList<String[]> list = new ArrayList<String[]>();

        for(int i = 0 ; i < parse_loadbalancer.size(); i++) {
            loadbalancer = (JSONObject) parse_loadbalancer.get(i);

            //LB이름, 옵션, 타입, 위치, IP, Port
            list.add(new String[]{(String) loadbalancer.get("name"),
                    (String)loadbalancer.get("zonename"), (String)loadbalancer.get("loadbalanceroption"), (String) loadbalancer.get("servicetype"),
                    (String)loadbalancer.get("serviceip"), (String) loadbalancer.get("serviceport")});

        }
        return list;

    }

    /**
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ParseException
     * @brief 각 LB에 등록된 웹 서버의 정보 조희를 위한 함수
     **/
    public static void listLBWebServers() throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {
        int button = 17;

        Scanner sc = new Scanner(System.in);

        System.out.print("등록된 웹 서버를 조회할 LB 이름 입력: ");

        String lbNname = sc.next();


        // 먼저 LB 리스트 조회해서 해당 이름의 LB id를 받아옴
        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(16, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());
        request.put("name", lbNname);


        String req_message = generateReq(request);

        System.out.println("Request Message is...");
        System.out.println(req_message);

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_listloadbalancersresponse = (JSONObject) obj.get("listloadbalancersresponse");

        JSONArray parse_loadbalancer = (JSONArray) parse_listloadbalancersresponse.get("loadbalancer");

        JSONObject loadbalancer = (JSONObject) parse_loadbalancer.get(0);

        String loadbalancerid = String.valueOf(loadbalancer.get("loadbalancerid"));
        String zoneid = String.valueOf(loadbalancer.get("zoneid"));

        // listLoadBalancerWebServers api url 생성 시작
        request.clear();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey",getApikey());
        request.put("loadbalancerid", loadbalancerid);

        req_message = generateReq(request);

        System.out.println("Request Message is...");
        System.out.println(req_message);

        obj =  readJsonFromUrl(req_message);

        JSONObject parse_listLoadBalancerWebServersresponse = (JSONObject) obj.get("listloadbalancerwebserversresponse");

        JSONArray parse_loadbalancerwebserver = (JSONArray) parse_listLoadBalancerWebServersresponse.get("loadbalancerwebserver");

        JSONObject loadbalancerwebserver;



        for(int i = 0 ; i < parse_loadbalancerwebserver.size(); i++) {
            loadbalancerwebserver = (JSONObject) parse_loadbalancerwebserver.get(i);

            // response로 받은 virtualmachineid로부터 얻은 vm id와 zoneid를 사용하여 vm명 검색 및 출력
            String name = getDisplaynameById(zoneid, String.valueOf(loadbalancerwebserver.get("virtualmachineid")));

            System.out.println("서버: "+ name);
            System.out.println("Public IP: " + loadbalancerwebserver.get("ipaddress"));
            System.out.println("Public Port: "+ loadbalancerwebserver.get("publicport"));
            System.out.println("Throughput: "+ loadbalancerwebserver.get("throughputrate"));
            System.out.println("Server connections: "+ loadbalancerwebserver.get("cursrvrconnections"));
            System.out.println("상태: "+ loadbalancerwebserver.get("state"));
            System.out.println();
        }

    }
}
