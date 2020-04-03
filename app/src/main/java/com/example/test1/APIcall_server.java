package com.example.test1;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class APIcall_server extends APIcall_main {

    /**
     * @brief APIcall_server 클래스의 생성자
     **/
    public APIcall_server(){
        this.baseurl =  "https://api.ucloudbiz.olleh.com/server/v1/client/api?";
    }


    /**
     * @return
     * @throws ParseException
     * @brief Server 기능에 해당하는, 생성된 VM 정보 출력을 위한 함수
     * @return 각 서버의 서버명, 스펙, 상태, 생성일시, 운영체제를 가지고 있는 list
     */
    public ArrayList<String[]> listServers() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 2;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());

        String req_message = generateReq(request);

        JSONObject obj = readJsonFromUrl(req_message);

        JSONObject parse_listvirtualmachinesresponse = (JSONObject) obj.get("listvirtualmachinesresponse");

        JSONArray parse_virtualmachine = (JSONArray) parse_listvirtualmachinesresponse.get("virtualmachine");

        JSONObject virtualmachine;

        ArrayList<String[]> list = new ArrayList<String[]>();

        for (int i = 0; i < parse_virtualmachine.size(); i++) {
            virtualmachine = (JSONObject) parse_virtualmachine.get(i);

            String s = virtualmachine.get("cpunumber").toString() + " vCore";
            String l = String.valueOf((long)virtualmachine.get("memory")/1024) + " GB";

            //서버명, 스펙, 상태, 생성일시, 운영체제, 내부주소, CPU/메모리, 디스크
            list.add(new String[]{(String) virtualmachine.get("displayname"),
                    s, (String) virtualmachine.get("state"), (String) virtualmachine.get("created"),
                    (String) virtualmachine.get("templatename"), (String)virtualmachine.get("id"),
                    (String)virtualmachine.get("ipaddress"), l});
        }


        if(zone.equals("전체")){
            this.baseurl = "https://api.ucloudbiz.olleh.com/server/v2/client/api?";

            req_message = generateReq(request);

            obj = readJsonFromUrl(req_message);

            parse_listvirtualmachinesresponse = (JSONObject) obj.get("listvirtualmachinesresponse");

            parse_virtualmachine = (JSONArray) parse_listvirtualmachinesresponse.get("virtualmachine");

            for (int i = 0; i < parse_virtualmachine.size(); i++) {
                virtualmachine = (JSONObject) parse_virtualmachine.get(i);

                String s = virtualmachine.get("cpunumber").toString() + " vCore";

                //서버명, 스펙, 상태, 생성일시, 운영체제
                list.add(new String[]{(String) virtualmachine.get("displayname"),
                        s, (String) virtualmachine.get("state"), (String) virtualmachine.get("created"),
                        (String) virtualmachine.get("templatename"), (String)virtualmachine.get("id")});
            }

        }

        return list;
    }

    /**
     * @throws ParseException
     * @brief Server 기능에 해당하는, 사용중인 VM을 정지시키기 위한 함수
     * @return 현재 정지시킨 서버의 상태
     * @param id 정지시키고자 하는 서버의 id
     **/
    public void stopServer(String id) throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 3;

        List<Integer> vm = new ArrayList<Integer>();
        TreeMap<String, String> request = new TreeMap<String, String>();

        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());
        request.put("id", id);

        String req_message = generateReq(request);

//        System.out.println("Request Message is...");
//        System.out.println(req_message);
//
//        System.out.println();

        readJsonFromUrl(req_message);
        System.out.println( "SUCCESS to STOP " + id);
    }

    /**
     * @throws ParseException
     * @brief Server 기능에 해당하는, 정지 상태인 VM을 시작하기 위한 함수
     * @param id 정지시키고자 하는 서버의 view id
     **/
    public void startServer(String id) throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 4;

        List<Integer> vm = new ArrayList<Integer>();

        TreeMap<String, String> request = new TreeMap<String, String>();

        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());
        request.put("id", id);

        String req_message = generateReq(request);
//
//        System.out.println("Request Message is...");
//        System.out.println(req_message);
//
//        System.out.println();

        readJsonFromUrl(req_message);

        System.out.println( "SUCCESS to START " + id);
    }

    /**
     * @throws ParseException
     * @brief Server 기능에 해당하는, VM을 재시작하기 위한 함수
     * @param id 정지시키고자 하는 서버의 id
     **/
    public void rebootServer(String id) throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 5;

        List<Integer> vm = new ArrayList<Integer>();

        TreeMap<String, String> request = new TreeMap<String, String>();

        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());
        request.put("id", id);

        String req_message = generateReq(request);
//
//        System.out.println("Request Message is...");
//        System.out.println(req_message);
//
//        System.out.println();


        readJsonFromUrl(req_message);

        System.out.println( "SUCCESS to REBOOT " + id);

    }
}