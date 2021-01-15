package sample.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Initialize {
    public ArrayList<Location> locationArrayList = new ArrayList<>();
    private ArrayList<Location> busStationArrayList = new ArrayList<>();
    public Initialize(){
    }
    //初始的地点
    public Location[] initLocation(){
        Location[] locations = new Location[38];
        locations[0] = new Location("a",124,95,false);
        locations[1] = new Location("b",171,299,false);
        locations[2] = new Location("c",301,194,false);
        locations[3] = new Location("d",327,81,false);
        locations[4] = new Location("e",500,154,false);
        locations[5] = new Location("f",463,259,false);
        locations[6] = new Location("g",321,410,false);
        locations[7] = new Location("h",556,386,false);
        locations[8] = new Location("i",714,243,false);
        locations[9] = new Location("j",624,69,false);
        locations[10] = new Location("k",166,555,false);
        locations[11] = new Location("l",602,676,false);
        locations[12] = new Location("m",895,541,false);
        locations[13] = new Location("n",1021,355,false);
        locations[14] = new Location("o",1074,152,false);
        locations[15] = new Location("p",1226,93,false);
        locations[16] = new Location("q",890,101,false);
        locations[17] = new Location("r",763,402,false);
        locations[18] = new Location("s",439,588,false);
        locations[19] = new Location("t",203,739,false);
        locations[20] = new Location("u",503,789,false);
        locations[21] = new Location("v",826,804,false);
        locations[22] = new Location("w",1090,638,false);
        locations[23] = new Location("x",1181,482,false);
        locations[24] = new Location("y",1237,349,false);
        locations[25] = new Location("z",680,522,false);
        locations[26] = new Location("aa",698,38,true);
        locations[27] = new Location("bb",650,160,true);
        locations[28] = new Location("cc",661,331,true);
        locations[29] = new Location("dd",774,549,true);
        locations[30] = new Location("ee",707,710,true);
        locations[31] = new Location("ff",594,851,true);
        locations[32] = new Location("gg",71,231,true);
        locations[33] = new Location("hh",86,379,true);
        locations[34] = new Location("ii",305,509,true);
        locations[35] = new Location("jj",574,546,true);
        locations[36] = new Location("kk",945,267,true);
        locations[37] = new Location("ll",1174,226,true);
        return locations;
    }
    //普通地点
    public ArrayList<Location> getLocationArrayList() {
        Location[] locations = initLocation();
        for(int i = 0;i < 26;i++){
            locationArrayList.add(locations[i]);
        }
        return locationArrayList;
    }
    //公交地点
    public ArrayList<Location> getBusStationArrayList() {
        Location[] locations = initLocation();
        for(int i = 26;i < 38;i++){
            busStationArrayList.add(locations[i]);
        }
        return busStationArrayList;
    }
    //读取文件得到初始边
    public String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String result = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                line++;
                result = result+tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return result;
    }
}
