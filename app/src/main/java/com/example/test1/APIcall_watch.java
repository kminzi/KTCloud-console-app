package com.example.test1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class APIcall_watch extends APIcall_main {
    private static String statistics = "Maximum";
    private static String starttime = "";
    private static String endtime = "";
    private static String namespace = "ucloud/server";
    private static String period = "60";
    private static String unit = "Bytes";

    private static HashMap<String, HashMap<String, String>> metricList;
    // metricList의 key에는 "CPUUtilization", "NetworkIn" 등의 Metric명이 들어가고, value에는  각 Metric별 points<timestamp, value>가 들어간다.

    public static HashMap<String, String> getInfo(String metricname){
        HashMap<String, String> tmp = new HashMap<String, String>();
        tmp = metricList.get(metricname);
        return tmp;
    }


    /**
     * @brief section 값을 구간으로, 현재 시간 및 통계 시작 시간을 설정하기 위한 함수
     **/
    private static void setTime(int section) {
        ZonedDateTime currDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        endtime = currDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        starttime = currDateTime.minusHours(section).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }


    /**
     * @brief 각 상세 그래프에서, 통계, 구간, 주기에 대한 사용자 지정 값을 입력받기 위한 함수
     **/
    private static void setValue() {
        Scanner sc = new Scanner(System.in);

        System.out.print("통계(Average, Sum, SampleCount, Maximum, Minimum): ");
        statistics = sc.next();

        System.out.print("구간 설정(시간단위- 1, 3, 6, 12, 24, 168): ");
        int input = sc.nextInt();
        setTime(input);

        System.out.print("주기(분단위- 5, 15, 30, 60, 360, 720, 1440): ");
        period = sc.next();

    }

    /**
     * @brief CPU Utilization 및 네트워크 관련 값을 제외한 각 상세 그래프에서, 단위에 대한 사용자 지정 값을 입력받기 위한 함수
     **/
    public static void setUnit() {
        Scanner sc = new Scanner(System.in);

        System.out.print("단위(Bytes, Kilobytes, Megabytes, Gigabytes): ");
        unit = sc.next();
    }


    /**
     * @brief 네트워크 관련 상세 그래프에서, 단위에 대한 사용자 지정 값을 입력받기 위한 함수
     **/
    public static void setUnitForNetwork() {
        Scanner sc = new Scanner(System.in);

        System.out.print("단위(Bits/Second, Kilobits/Second, Megabits/Second, Gigabits/Second, Bytes/Second, Kilobytes/Second, Megabytes/Second, Gigabytes/Second): ");
        unit = sc.next();
    }


    /**
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ParseException
     * @brief 서버의 displayname으로 zoneid를 받아오는 함수
     * @param displayname 위치한 zoneid를 알기를 원하는 서버명
     * @return 서버명이 displayname인 서버의 zoneid, 찾지못했을 경우 "" 반환
     **/
    public static String getZoneByDisplayname(String displayname) throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {
        String zoneid = "";
        int button = 2;


        // 1. 서버가 Seoul-M2존이 아닐 경우 탐색
        setZone("");

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());


        String req_message = generateReq(request);

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_listvirtualmachinesresponse = (JSONObject) obj.get("listvirtualmachinesresponse");

        // 조회된 서버가 하나 이상일 경우에만 출력, 메트릭 조회 가능
        if(parse_listvirtualmachinesresponse.size() > 0 ) {

            JSONArray parse_virtualmachine = (JSONArray) parse_listvirtualmachinesresponse.get("virtualmachine");

            JSONObject virtualmachine;

            for(int i = 0 ; i < parse_virtualmachine.size(); i++) {

                virtualmachine = (JSONObject) parse_virtualmachine.get(i);

                if(String.valueOf(virtualmachine.get("displayname")).equals(displayname)) {
                    zoneid = String.valueOf(virtualmachine.get("zoneid"));
//                    System.out.println("Request Message is...");
//                    System.out.println(req_message);
//                    System.out.println("zoneid= " + zoneid);
//                    System.out.println();
                    return zoneid;
                }

            }
        }


        // 2. 서버가 Seoul-M2존일 경우 탐색
        setZone("Seoul-M2");

        req_message = generateReq(request);

        obj =  readJsonFromUrl(req_message);

        parse_listvirtualmachinesresponse = (JSONObject) obj.get("listvirtualmachinesresponse");

        // 조회된 서버가 하나 이상일 경우에만 출력, 메트릭 조회 가능
        if(parse_listvirtualmachinesresponse.size() > 0 ) {

            JSONArray parse_virtualmachine = (JSONArray) parse_listvirtualmachinesresponse.get("virtualmachine");

            JSONObject virtualmachine;

            for(int i = 0 ; i < parse_virtualmachine.size(); i++) {

                virtualmachine = (JSONObject) parse_virtualmachine.get(i);

                if(String.valueOf(virtualmachine.get("displayname")).equals(displayname)) {
                    zoneid = String.valueOf(virtualmachine.get("zone"));
//                    System.out.println("(For Zone ID) Request Message is...");
//                    System.out.println(req_message);
//                    System.out.println("zoneid= " + zoneid);
//                    System.out.println();
                    return zoneid;
                }

            }
        }

        // 해당 displayname의 서버를 찾지 못할 경우 "" 반환

        return zoneid;
    }

    /**
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ParseException
     * @brief 서버의 zondid와 displayname으로 name(hostname)을 받아오는 함수
     * @param zoneid hostname을 알기를 원하는 서버의 zoneid
     * @param displayname hostname을 알기를 원하는 서버명
     * @return zoneid와 displayname이 일치하는 서버의 hostname, 찾지못했을 경우 "" 반환
     **/
    public static String getNameByDisplayname(String zoneid, String displayname) throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {
        String name = "";
        int button = 2;


        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());

        request.remove("zoneid");
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

                if(String.valueOf(virtualmachine.get("displayname")).equals(displayname)) {
                    name = String.valueOf(virtualmachine.get("name"));
//                    System.out.println("(For Name)Request Message is...");
//                    System.out.println(req_message);
//                    System.out.println("name= " + name);
//                    System.out.println();
                    return name;
                }

            }
        }

        return name;
    }


    /**
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ParseException
     * @brief 서버의 zoneid와 displayname으로 id을 받아오는 함수
     * @param zoneid id을 알기를 원하는 서버의 zoneid
     * @param displayname id을 알기를 원하는 서버명
     * @return zoneid와 displayname이 일치하는 서버의 id, 찾지못했을 경우 "" 반환
     **/
    public static String getIdByDisplayname(String zoneid, String displayname) throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {
        String id = "";
        int button = 2;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());

        request.remove("zoneid");
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

                if(String.valueOf(virtualmachine.get("displayname")).equals(displayname)) {
                    id = String.valueOf(virtualmachine.get("id"));

//                    System.out.println("(For ID)Request Message is...");
//                    System.out.println(req_message);
//                    System.out.println("id= " + id);
//                    System.out.println();
                    return id;
                }

            }
        }

        return id;

    }





    /**
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ParseException
     * @brief 특정 서버 메트릭 조회 시 필요한, 각 zone별 사용중인 서버 리스트 출력 함수
     * @param z 사용중인 모든 서버 리스트 출력을 원하는 zone
     **/
    private static void listServerAt(String z) throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {

        int button = 2;


        setState("running");

        setZone(z);

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());


        String req_message = generateReq(request);

        System.out.println("Request Message is...");
        System.out.println(req_message);

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_listvirtualmachinesresponse = (JSONObject) obj.get("listvirtualmachinesresponse");

        // 조회된 서버가 하나 이상일 경우에만 출력, 메트릭 조회 가능
        if(parse_listvirtualmachinesresponse.size() > 0 ) {

            JSONArray parse_virtualmachine = (JSONArray) parse_listvirtualmachinesresponse.get("virtualmachine");

            JSONObject virtualmachine;

            for(int i = 0 ; i < parse_virtualmachine.size(); i++) {
                virtualmachine = (JSONObject) parse_virtualmachine.get(i);

                System.out.println("서버명 : "+ virtualmachine.get("displayname"));
                System.out.println();
            }
        }

    }


    /**
     * @throws IOException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @brief 특정 서버 메트릭 조회 시 선택 가능한, 모든 위치의 서버 리스트 출력 함수
     **/
    public static void listAllServer() throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {

        listServerAt("Central-A");
        listServerAt("Central-B");
        listServerAt("Seoul-M");
        listServerAt("Seoul-M2");
        listServerAt("HA");
        listServerAt("US-West");
    }






    /**
     * @throws ParseException
     * @brief Watch 기능에 해당하는, 조회가능한 metricname 출력을 위한 함수
     **/
    public static void listMetrics() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 1;

        TreeMap<String, String> request = new TreeMap<String, String>();
        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());

        String req_message = generateReq(request);

        System.out.println("Request Message is...");
        System.out.println(req_message);

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_listmetricsresponse = (JSONObject) obj.get("listmetricsresponse");

        JSONArray parse_metric = (JSONArray) parse_listmetricsresponse.get("metric");
        JSONObject metric;

        for(int i = 0 ; i < parse_metric.size(); i++) {
            metric = (JSONObject) parse_metric.get(i);

            System.out.println("metricname : "+  metric.get("metricname"));

        }

    }

    /**
     * @throws ParseException
     * @brief Watch 기능에 해당하는, 전체 서버에 대한 지정 메트릭 통계를 위한 함수
     * @param metricname 전체서버에 대해 조회하길 원하는 metricname
     **/
    public static void showMetric(String metricname) throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 6;

//        setValue();
        setTime(6);

        metricList = new HashMap<String, HashMap<String, String>> ();
        HashMap<String, String> points = new HashMap<String, String> ();

        TreeMap<String, String> request = new TreeMap<String, String>();

        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());
        request.put("metricname", metricname);
        request.put("namespace", namespace);

        request.put("statistics.member.1", statistics);
        request.put("endtime", endtime);
        request.put("starttime", starttime);
        request.put("period", period);
        request.put("unit", unit);

        String req_message = generateReq(request);

        System.out.println("Request Message is...");
        System.out.println(req_message);
        System.out.println();

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_getmetricstatisticsresponse = (JSONObject) obj.get("getmetricstatisticsresponse");

        JSONArray parse_metricstatistics = (JSONArray) parse_getmetricstatisticsresponse.get("metricstatistics");
        JSONObject metric;

        System.out.println("총 point 수: "+ parse_getmetricstatisticsresponse.get("count"));

        for(int i = 0 ; i <  parse_metricstatistics.size(); i++) {

            metric = (JSONObject) parse_metricstatistics.get(i);


            points.put(String.valueOf(metric.get("timestamp")).substring(5,16).replaceAll("T", " "), String.valueOf(metric.get(statistics.toLowerCase())));

//            System.out.println("##############################");
//
//            System.out.println("timestamp: "+  String.valueOf(metric.get("timestamp")).substring(5,16).replaceAll("T", " "));
//            System.out.println(statistics+ ": "+  metric.get(statistics.toLowerCase()));
//            System.out.println("unit: "+  metric.get("unit"));


        }

        metricList.put(metricname, points);

    }

    /**
     * @throws ParseException
     * @brief 특정 서버의 지정 메트릭 통계를 위한 함수
     * @param metricname 조회하길 원하는 metricname
     * @param displayname 조회하길 원하는 특정 서버의 displayname
     **/
    public static void showMetric(String metricname, String displayname) throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        int button = 6;


        // 먼저 displayname에 해당하는 name과 value값을 받아옴

        setValue();


        TreeMap<String, String> request = new TreeMap<String, String>();
        String zoneid = getZoneByDisplayname(displayname);
        String value = displayname + "(" + getIdByDisplayname(zoneid, displayname) + ")";

        request.clear();


        // displayname에 해당하는 getMetricStatistics api url 생성 시작

        metricList = new HashMap<String, HashMap<String, String>> ();
        HashMap<String, String> points = new HashMap<String, String> ();



        request = generateRequire(button, request);

        request.put("response", "json");
        request.put("apiKey", getApikey());
        request.put("metricname", metricname);
        request.put("namespace", namespace);

        request.put("statistics.member.1", statistics);
        request.put("endtime", endtime);
        request.put("starttime", starttime);
        request.put("period", period);
        request.put("unit", "Percent");

        request.put("dimensions.member.1.name", "name");
        request.put("dimensions.member.1.value", value);


        String req_message = generateReq(request);

        System.out.println("Request Message is...");
        System.out.println(req_message);
        System.out.println();

        JSONObject obj =  readJsonFromUrl(req_message);

        JSONObject parse_getmetricstatisticsresponse = (JSONObject) obj.get("getmetricstatisticsresponse");

        JSONArray parse_metricstatistics = (JSONArray) parse_getmetricstatisticsresponse.get("metricstatistics");
        JSONObject metric;

        System.out.println("총 point 수: "+ parse_getmetricstatisticsresponse.get("count"));

        for(int i = 0 ; i <  parse_metricstatistics.size(); i++) {
            metric = (JSONObject) parse_metricstatistics.get(i);

            points.put(String.valueOf(metric.get("timestamp")).substring(5,16).replaceAll("T", " "), String.valueOf(metric.get(statistics.toLowerCase())));

//            System.out.println("##############################");
//
//            System.out.println("timestamp: "+  String.valueOf(metric.get("timestamp")).substring(5,16).replaceAll("T", " "));
//            System.out.println(statistics+ ": "+  metric.get(statistics.toLowerCase()));
//            System.out.println("unit: "+  metric.get("unit"));


        }

        metricList.put(metricname, points);

    }

    /**
     * @throws IOException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @brief 개별 서버 메트릭 조회 시, 선택된 서버별 메트릭 조회를 위한 함수
     **/
    public static void showSpecificServerMetric() throws InvalidKeyException, NoSuchAlgorithmException, ParseException, IOException {

        Scanner sc = new Scanner(System.in);

        System.out.print("메트릭 조회할 vm 이름 입력: ");

        String displayname = sc.next();

        while(displayname != "9999") { // 9999 입력 시 종료


            System.out.print(displayname+ " 조회할 메트릭(CPUUtilization, NetworkIn, NetworkOut, DiskReadBytes, DiskWriteBytes, MemoryInternalFree, MemoryTarget): " );
            String metric = sc.next();

            switch(metric) {
                case "CPUUtilization": showMetric("CPUUtilization", displayname); break;
                case "NetworkIn": setUnitForNetwork(); showMetric("NetworkIn", displayname); break;
                case "NetworkOut": setUnitForNetwork(); showMetric("NetworkOut", displayname); break;
                case "DiskReadBytes": setUnit(); showMetric("DiskReadBytes", displayname); break;
                case "DiskWriteBytes": setUnit(); showMetric("DiskWriteBytes", displayname); break;
                case "MemoryInternalFree": setUnit(); showMetric("MemoryInternalFree", displayname); break;
                case "MemoryTarget": setUnit(); showMetric("MemoryTarget", displayname); break;
            }

            displayname = sc.next();
        }

    }


    /**
     * @throws ParseException
     * @brief Watch 기능에 해당하는, 6가지 항목에 대한 통계 요약을 위한 함수
     **/
    public static void showMonitoring() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {

        setTime(3);

        showMetric("CPUUtilization");
        showMetric("NetworkIn");
        showMetric("NetworkOut");
        showMetric("DiskReadBytes");
        showMetric("DiskWriteBytes");
        showMetric("MemoryInternalFree");

    }
}
