package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import sample.model.Initialize;
import sample.model.Location;
import java.util.ArrayList;
import java.util.Map;
import java.util.*;
import java.io.IOException;
import java.util.Set;

public class MapSearch {
    private ArrayList<Location> locationArrayList = new ArrayList<>();//普通地点
    private ArrayList<Line> lineArrayList = new ArrayList<>();//普通路径
    private Map<Location[],Line> lineMap = new HashMap<Location[],Line>();//地点与路径的关系
    private ArrayList<Location> busStationArrayList = new ArrayList<>();
    private ArrayList<Line> busLineArrayList = new ArrayList<>();
    private Map<Location[],Line> busLineMap = new HashMap<>();
    private Line[] lines = new Line[2];//重置
    private Location[] locations = new Location[2];//重置
    private int M = 100000;//邻接矩阵最大值
    @FXML
    private TextField text_addPath_end;

    @FXML
    private TextField text_addPath_start;

    @FXML
    private TextField text_add_busLocation;

    @FXML
    private TextField text_delLocation;

    @FXML
    private TextArea text_path;

    @FXML
    private Button bt_navigation_walk;

    @FXML
    private TextField text_delLocation_bus;

    @FXML
    private TextField text_delPath_end;

    @FXML
    private Button bt_del_path;

    @FXML
    private TextField text_3;

    @FXML
    private TextField text_addPath_busStart;

    @FXML
    private TextField text_delPath_busEnd;

    @FXML
    private Button bt_del_location;

    @FXML
    private TextField text_addPath_busEnd;

    @FXML
    private Button bt_add_path;

    @FXML
    private TextField text_navigation_end;

    @FXML
    private Button bt_init;

    @FXML
    private Button bt_navigation_bus;

    @FXML
    private Button bt_delPath_busStation;

    @FXML
    private TextField text_navigation_start;

    @FXML
    private TextField text_delPath_start;

    @FXML
    private Pane pane_map;

    @FXML
    private TextField text_delPath_busStart;

    @FXML
    private Button bt_addPath_busStation;

    @FXML
    private Button bt_delLocation_busStation;

    @FXML
    private Button bt_reset;
    @FXML//添加地点
    void select_location(MouseEvent event) throws IOException {
            if(!text_3.getText().equals("")&&text_add_busLocation.getText().equals("")) {
                int X = (int) event.getX();
                int Y = (int) event.getY();
                Circle circle = new Circle(X, Y, 3, Color.BLUE);
                Label label = new Label(text_3.getText());
                label.setLayoutX(X);
                label.setLayoutY(Y);
                Location location = new Location();
                location.circle = circle;
                location.label = label;
                pane_map.getChildren().add(location.circle);
                pane_map.getChildren().add(location.label);
                locationArrayList.add(location);
                text_3.setText("");
            }
            if(!text_add_busLocation.getText().equals("")&&text_3.getText().equals("")){
                int X = (int) event.getX();
                int Y = (int) event.getY();
                Circle circle = new Circle(X, Y, 3, Color.BROWN);
                Label label = new Label(text_add_busLocation.getText());
                label.setLayoutX(X);
                label.setLayoutY(Y);
                Location location = new Location();
                location.circle = circle;
                location.label = label;
                location.busStation = true;
                pane_map.getChildren().add(location.circle);
                pane_map.getChildren().add(location.label);
                busStationArrayList.add(location);
                text_add_busLocation.setText("");
            }
    }

    @FXML//添加路径
    void addPath(ActionEvent event) {
        String start = text_addPath_start.getText();
        String end = text_addPath_end.getText();
        Location startLocation = null;
        Location endLocation = null;
         for(int i = 0;i < locationArrayList.size();i++){
             if(start.equals(locationArrayList.get(i).label.getText())){
                 startLocation = locationArrayList.get(i);
             }
             if(end.equals(locationArrayList.get(i).label.getText())){
                 endLocation = locationArrayList.get(i);
             }
         }
         Line line = new Line(startLocation.circle.getCenterX(),startLocation.circle.getCenterY(),endLocation.circle.getCenterX(),endLocation.circle.getCenterY());
         lineMap.put(new Location[]{startLocation, endLocation},line);
         lineMap.put(new Location[]{endLocation, startLocation},line);
         pane_map.getChildren().add(line);
         lineArrayList.add(line);
         text_addPath_start.setText("");
         text_addPath_end.setText("");
    }

    @FXML//添加公交站路径
    void addPath_busStation(ActionEvent event) {
        String start = text_addPath_busStart.getText();
        String end = text_addPath_busEnd.getText();
        Location startLocation = null;
        Location endLocation = null;
        for(int i = 0;i < busStationArrayList.size();i++){
            if(start.equals(busStationArrayList.get(i).label.getText())){
                startLocation = busStationArrayList.get(i);
            }
            if(end.equals(busStationArrayList.get(i).label.getText())){
                endLocation = busStationArrayList.get(i);
            }
        }
        Line line = new Line(startLocation.circle.getCenterX(),startLocation.circle.getCenterY(),endLocation.circle.getCenterX(),endLocation.circle.getCenterY());
        line.setStroke(Color.YELLOW);
        busLineMap.put(new Location[]{startLocation, endLocation},line);
        busLineMap.put(new Location[]{endLocation, startLocation},line);
        pane_map.getChildren().add(line);
        busLineArrayList.add(line);
        text_addPath_busStart.setText("");
        text_addPath_busEnd.setText("");
    }

    @FXML//删除地点
    void delLocation(ActionEvent event) {
         String name = text_delLocation.getText();
         Location location = null;
         for(int i = 0;i < locationArrayList.size();i++){
             if(name.equals(locationArrayList.get(i).label.getText())){
                 location = locationArrayList.get(i);
             }
         }
         System.out.println(locationArrayList.size());
         pane_map.getChildren().remove(location.circle);
         pane_map.getChildren().remove(location.label);
         locationArrayList.remove(location);
         text_delLocation.setText("");
    }


    @FXML//添加公交站
    void delLocation_busStation(ActionEvent event) {
        String name = text_delLocation_bus.getText();
        Location location = null;
        for(int i = 0;i < busStationArrayList.size();i++){
            if(name.equals(busStationArrayList.get(i).label.getText())){
                location = busStationArrayList.get(i);
            }
        }
        System.out.println(busStationArrayList.size());
        pane_map.getChildren().remove(location.circle);
        pane_map.getChildren().remove(location.label);
        busStationArrayList.remove(location);
        text_delLocation_bus.setText("");
    }

    @FXML//删除路径
    void delPath(ActionEvent event) {
        String start = text_delPath_start.getText();
        String end = text_delPath_end.getText();
        System.out.println(start + end);
        Location startLocation = null;
        Location endLocation = null;
        Line line = null;
        for (int i = 0; i < locationArrayList.size(); i++) {
            if (start.equals(locationArrayList.get(i).label.getText())) {
                startLocation = locationArrayList.get(i);
            }
            if (end.equals(locationArrayList.get(i).label.getText())) {
                endLocation = locationArrayList.get(i);
            }
        }
        for (int i = 0; i < lineArrayList.size(); i++) {
            if ((startLocation.circle.getCenterX() == lineArrayList.get(i).getStartX() && startLocation.circle.getCenterY() == lineArrayList.get(i).getStartY() && endLocation.circle.getCenterX() == lineArrayList.get(i).getEndX() && endLocation.circle.getCenterY() == lineArrayList.get(i).getEndY()) || (endLocation.circle.getCenterX() == lineArrayList.get(i).getStartX() && endLocation.circle.getCenterY() == lineArrayList.get(i).getStartY() && startLocation.circle.getCenterX() == lineArrayList.get(i).getEndX() && startLocation.circle.getCenterY() == lineArrayList.get(i).getEndY())) {
                line = lineArrayList.get(i);
            }
        }
        Collection<Line> collect = lineMap.values();
        if(collect.contains(line)) {
            collect.remove(line);
            collect.remove(line);
            System.out.println(lineMap.isEmpty());
        }
        System.out.println(lineArrayList.size());
        pane_map.getChildren().remove(line);
        lineArrayList.remove(line);
        text_delPath_start.setText("");
        text_delPath_end.setText("");
    }

    @FXML//删除公交站路径
    void delPath_busStation(ActionEvent event) {
        String start = text_delPath_busStart.getText();
        String end = text_delPath_busEnd.getText();
        System.out.println(start + end);
        Location startLocation = null;
        Location endLocation = null;
        Line line = null;
        for (int i = 0; i < busStationArrayList.size(); i++) {
            if (start.equals(busStationArrayList.get(i).label.getText())) {
                startLocation = busStationArrayList.get(i);
            }
            if (end.equals(busStationArrayList.get(i).label.getText())) {
                endLocation = busStationArrayList.get(i);
            }
        }
        for (int i = 0; i < busLineArrayList.size(); i++) {
            if ((startLocation.circle.getCenterX() == busLineArrayList.get(i).getStartX() && startLocation.circle.getCenterY() == busLineArrayList.get(i).getStartY() && endLocation.circle.getCenterX() == busLineArrayList.get(i).getEndX() && endLocation.circle.getCenterY() == busLineArrayList.get(i).getEndY()) || (endLocation.circle.getCenterX() == busLineArrayList.get(i).getStartX() && endLocation.circle.getCenterY() == busLineArrayList.get(i).getStartY() && startLocation.circle.getCenterX() == busLineArrayList.get(i).getEndX() && startLocation.circle.getCenterY() == busLineArrayList.get(i).getEndY())) {
                line = busLineArrayList.get(i);
            }
        }
        Collection<Line> collect = busLineMap.values();
        if(collect.contains(line)) {
            collect.remove(line);
            collect.remove(line);
            System.out.println(busLineMap.isEmpty());
        }
        System.out.println(busLineArrayList.size());
        pane_map.getChildren().remove(line);
        busLineArrayList.remove(line);
        text_delPath_busStart.setText("");
        text_delPath_busEnd.setText("");
    }

    @FXML//公交导航
    void navigation_bus(ActionEvent event) {
        long startTime=System.nanoTime();   //获取开始时间
       String start = text_navigation_start.getText();
       String end = text_navigation_end.getText();
       Location startLocation = null;
       Location endLocation = null;
        for (int i = 0; i < locationArrayList.size(); i++) {
            if (start.equals(locationArrayList.get(i).label.getText())) {
                startLocation = locationArrayList.get(i);
            }
            if (end.equals(locationArrayList.get(i).label.getText())) {
                endLocation = locationArrayList.get(i);
            }
        }
        Location closeStartLocation = getCloseBusSStation(startLocation);
        Location closeEndLocation = getCloseBusSStation(endLocation);
        Line line1 = new Line(startLocation.circle.getCenterX(),startLocation.circle.getCenterY(),closeStartLocation.circle.getCenterX(),closeStartLocation.circle.getCenterY());
        line1.setStroke(Color.RED);
        Line line2 = new Line(endLocation.circle.getCenterX(),endLocation.circle.getCenterY(),closeEndLocation.circle.getCenterX(),closeEndLocation.circle.getCenterY());
        line2.setStroke(Color.RED);
        busLineMap.put(new Location[]{startLocation, closeStartLocation},line1);
        busLineMap.put(new Location[]{closeStartLocation, startLocation},line1);
        busLineMap.put(new Location[]{endLocation, closeEndLocation},line2);
        busLineMap.put(new Location[]{closeEndLocation, endLocation},line2);
        busLineArrayList.add(line1);
        busLineArrayList.add(line2);
        busStationArrayList.add(startLocation);
        busStationArrayList.add(endLocation);
        pane_map.getChildren().add(line1);
        pane_map.getChildren().add(line2);
        lines[0] = line1;
        lines[1] = line2;
        locations[0] = startLocation;
        locations[1] = endLocation;
        locationArrayList.remove(startLocation);
        locationArrayList.remove(endLocation);

        Dijkstra dijkstra = new Dijkstra();
        dijkstra.findShortPath(busDistances(),startLocation.ID,endLocation.ID);
        String[] paths = dijkstra.path;
        int[] shortPath = dijkstra.shortPath;
        int[] pathID = getPathID(paths[endLocation.ID]);
        String path = getPath(pathID,busStationArrayList);
        markPath(pathID,busLineMap);
        long endTime=System.nanoTime(); //获取结束时间
        System.out.println();
        if(shortPath[endLocation.ID] == M){
            text_path.setText("无法找到路径！");
        }else {
            System.out.println("最短路径："+path+"\n"+"距离："+shortPath[endLocation.ID]+"\n"+"程序运行时间："+(endTime-startTime)+"ns");
            text_path.setText("最短路径："+path+"\n"+"距离："+shortPath[endLocation.ID]+"\n"+"程序运行时间："+(endTime-startTime)+"ns");
        }
    }

    @FXML//步行导航
    void navigation_walk(ActionEvent event) {
        long startTime=System.nanoTime();   //获取开始时间
        String start = text_navigation_start.getText();
        String end = text_navigation_end.getText();
        Location startLocation = getLocation(start);
        Location endLocation = getLocation(end);
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.findShortPath(distances(),startLocation.ID,endLocation.ID);
        String[] paths = dijkstra.path;
        int[] shortPath = dijkstra.shortPath;
        int[] pathID = getPathID(paths[endLocation.ID]);
        String path = getPath(pathID,locationArrayList);
        markPath(pathID,lineMap);
        long endTime=System.nanoTime(); //获取结束时间
        System.out.println();
        System.out.println("程序运行时间："+(endTime-startTime)+"ns");
        if(shortPath[endLocation.ID] == M){
            text_path.setText("无法找到路径！");
        }else {
            System.out.println("最短路径："+path+"\n"+"距离："+shortPath[endLocation.ID]+"\n"+"程序运行时间："+(endTime-startTime)+"ns");
            text_path.setText("最短路径："+path+"\n"+"距离："+shortPath[endLocation.ID]+"\n"+"程序运行时间："+(endTime-startTime)+"ns");
        }
    }

    @FXML//重置，消除标记
    void reset(ActionEvent event) {
         for(int i = 0;i < lineArrayList.size();i++){
             lineArrayList.get(i).setStroke(Color.BLACK);
         }
        for(int i = 0;i < busLineArrayList.size();i++){
            busLineArrayList.get(i).setStroke(Color.YELLOW);
        }
        if(lines[0] != null&&lines[0].getEndX() != 0){
            if(busStationArrayList.contains(locations[0])&&busStationArrayList.contains(locations[1])){
                busStationArrayList.remove(locations[0]);
                busStationArrayList.remove(locations[1]);
                locationArrayList.add(locations[0]);
                locationArrayList.add(locations[1]);
            }
            if(busLineArrayList.contains(lines[0])&&busLineArrayList.contains(lines[1])){
                busLineArrayList.remove(lines[0]);
                busLineArrayList.remove(lines[1]);
                pane_map.getChildren().removeAll(lines[0],lines[1]);

            }
            Collection<Line> collect = busLineMap.values();
            if(collect.contains(lines[0])&&collect.contains(lines[1])) {
                //注意两条线段要删两次
                collect.remove(lines[0]);
                collect.remove(lines[0]);
                collect.remove(lines[1]);
                collect.remove(lines[1]);
            }
            lines[0] = new Line(0,0,0,0);
            lines[1] = new Line(0,0,0,0);
            locations[0] = new Location();
            locations[1] = new Location();
        }
        //text_navigation_start.setText("");
        //text_navigation_end.setText("");
        text_path.setText("");
    }

    @FXML//初始化地图
    void init_map(ActionEvent event) {
        Initialize initialize = new Initialize();
        locationArrayList = initialize.getLocationArrayList();
        busStationArrayList = initialize.getBusStationArrayList();
        for(int i = 0;i < locationArrayList.size();i++) {
            pane_map.getChildren().add(locationArrayList.get(i).circle);
            pane_map.getChildren().add(locationArrayList.get(i).label);
        }
        for (int i = 0;i < busStationArrayList.size();i++){
            pane_map.getChildren().add(busStationArrayList.get(i).circle);
            pane_map.getChildren().add(busStationArrayList.get(i).label);
        }
        String result = initialize.readFileByLines("data.txt");
        String[] strs = result.split(" ");
        for(int j = 0;j < strs.length-1;j = j+2){
            String start = strs[j];
            String end = strs[j+1];
            Location startLocation = null;
            Location endLocation = null;
            if(start.length()<2&&end.length()<2) {
                for (int i = 0; i < locationArrayList.size(); i++) {
                    if (start.equals(locationArrayList.get(i).label.getText())) {
                        startLocation = locationArrayList.get(i);
                    }
                    if (end.equals(locationArrayList.get(i).label.getText())) {
                        endLocation = locationArrayList.get(i);
                    }
                }
                    Line line = new Line(startLocation.circle.getCenterX(), startLocation.circle.getCenterY(), endLocation.circle.getCenterX(), endLocation.circle.getCenterY());
                    lineMap.put(new Location[]{startLocation, endLocation}, line);
                    lineMap.put(new Location[]{endLocation, startLocation}, line);
                    pane_map.getChildren().add(line);
                    lineArrayList.add(line);
            }else {
                for (int i = 0; i < busStationArrayList.size(); i++) {
                    if (start.equals(busStationArrayList.get(i).label.getText())) {
                        startLocation = busStationArrayList.get(i);
                    }
                    if (end.equals(busStationArrayList.get(i).label.getText())) {
                        endLocation = busStationArrayList.get(i);
                    }
                }
                    Line line = new Line(startLocation.circle.getCenterX(), startLocation.circle.getCenterY(), endLocation.circle.getCenterX(), endLocation.circle.getCenterY());
                    line.setStroke(Color.YELLOW);
                    busLineMap.put(new Location[]{startLocation, endLocation}, line);
                    busLineMap.put(new Location[]{endLocation, startLocation}, line);
                    pane_map.getChildren().add(line);
                    busLineArrayList.add(line);
            }
        }
    }

    //将距离转化为邻接矩阵
    public int[][] distances(){
        for(int i = 0;i < locationArrayList.size();i++){
            locationArrayList.get(i).ID = i;
        }
        //获取Map中的所有key与value的对应关系
        Set<Map.Entry<Location[],Line>> entrySet = lineMap.entrySet();
        //遍历Set集合
        Iterator<Map.Entry<Location[],Line>> it =entrySet.iterator();
        int[][] distances = new int[locationArrayList.size()][locationArrayList.size()];
        while (it.hasNext()){
            Map.Entry<Location[],Line> entry = it.next();
            //通过每一对对应关系获取对应的key
            Location[] key = entry.getKey();
            distances[key[0].ID][key[1].ID] = calculateDistance(key[0].circle.getCenterX(),key[0].circle.getCenterY(),key[1].circle.getCenterX(),key[1].circle.getCenterY());
            distances[key[1].ID][key[0].ID] = calculateDistance(key[0].circle.getCenterX(),key[0].circle.getCenterY(),key[1].circle.getCenterX(),key[1].circle.getCenterY());
        }
        for(int i = 0;i < locationArrayList.size();i++){
        for (int j = 0;j < locationArrayList.size();j++){
           if (distances[i][j] == 0&&(i!=j)){
               distances[i][j] = M;
           }
        }
        }
    return distances;
    }
    //将距离转化为邻接矩阵
    public int[][] busDistances() {
        for (int i = 0; i < busStationArrayList.size(); i++) {
            busStationArrayList.get(i).ID = i;
        }
        //获取Map中的所有key与value的对应关系
        Set<Map.Entry<Location[], Line>> entrySet = busLineMap.entrySet();
        //遍历Set集合
        Iterator<Map.Entry<Location[], Line>> it = entrySet.iterator();
        int[][] distances = new int[busStationArrayList.size()][busStationArrayList.size()];
        while (it.hasNext()) {
            Map.Entry<Location[], Line> entry = it.next();
            //通过每一对对应关系获取对应的key
            Location[] key = entry.getKey();
            Line line = entry.getValue();
                distances[key[0].ID][key[1].ID] = calculateDistance(key[0].circle.getCenterX(), key[0].circle.getCenterY(), key[1].circle.getCenterX(), key[1].circle.getCenterY());
                distances[key[1].ID][key[0].ID] = calculateDistance(key[0].circle.getCenterX(), key[0].circle.getCenterY(), key[1].circle.getCenterX(), key[1].circle.getCenterY());
        }
        for (int i = 0; i < busStationArrayList.size(); i++) {
            for (int j = 0; j < busStationArrayList.size(); j++) {
                if (distances[i][j] == 0 && (i != j)) {
                    distances[i][j] = M;
                }
            }
        }
        System.out.println(Arrays.deepToString(distances));
        return distances;
    }
    //计算距离
    public int calculateDistance(double x1,double y1,double x2,double y2){
        return (int) Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
    //通过地名获取地点对象
    public Location getLocation(String name){
        Location location = null;
        for(int i = 0;i < locationArrayList.size();i++){
            if(name.equals(locationArrayList.get(i).label.getText())){
                location = locationArrayList.get(i);
            }
        }
        return location;
    }

    public Location getLocation(int ID){
        Location location = null;
        for(int i = 0;i < locationArrayList.size();i++){
            if(ID==locationArrayList.get(i).ID){
                location = locationArrayList.get(i);
            }
        }
        return location;
    }

    public int[] getPathID(String path){
        String[] strs = path.split("-->");
        int[] array = Arrays.asList(strs).stream().mapToInt(Integer::parseInt).toArray();
        return array;
    }
    //通过标号还原路径
    public String getPath(int[] pathID,ArrayList<Location> arrayList){
        String result = "";
        for(int i = 0;i < pathID.length;i++){
            for(int j = 0;j < arrayList.size();j++){
                if(arrayList.get(j).ID == pathID[i]){
                    if(result.equals("")){
                        result = arrayList.get(j).label.getText();
                    }else {
                        result = result + "-->"+arrayList.get(j).label.getText();
                    }
                }
            }
        }

        return result;
    }
    //标记路径
    public void markPath(int[] pathID,Map<Location[],Line> map){
        Set<Map.Entry<Location[],Line>> entrySet = map.entrySet();
        //遍历Set集合
        Iterator<Map.Entry<Location[],Line>> it =entrySet.iterator();
        while (it.hasNext()){
            Map.Entry<Location[],Line> entry = it.next();
            //通过每一对对应关系获取对应的key
            Location[] key = entry.getKey();
            //通过每一对对应关系获取对应的value
            Line value = entry.getValue();
            for(int i = 0;i < pathID.length-1;i++){
                if(key[0].ID==pathID[i]&&key[1].ID==pathID[i+1]){
                    value.setStroke(Color.RED);
                }
            }
        }
    }
    //测试使用
    public void printInitMap(){
        for(int i = 0; i < locationArrayList.size();i++){
            System.out.println(locationArrayList.get(i).label+" "+locationArrayList.get(i).circle.toString());
        }
        Set<Map.Entry<Location[],Line>> entrySet = lineMap.entrySet();
        //遍历Set集合
        Iterator<Map.Entry<Location[],Line>> it =entrySet.iterator();
        while (it.hasNext()){
            Map.Entry<Location[],Line> entry = it.next();
            //通过每一对对应关系获取对应的key
            Location[] key = entry.getKey();
            //通过每一对对应关系获取对应的value
            Line value = entry.getValue();
            System.out.println(key[0].label+" "+key[1].label+" ");
        }


    }
    //得到距离地点最近的两个公交站
    public Location getCloseBusSStation(Location location){
        Location result = null;
        int[] distances = new int[busStationArrayList.size()];
        int[] distances1 = new int[busStationArrayList.size()];
        for(int i = 0;i < distances.length;i++){
            distances[i] = calculateDistance(location.X,location.Y,busStationArrayList.get(i).X,busStationArrayList.get(i).Y);
            distances1[i] = calculateDistance(location.X,location.Y,busStationArrayList.get(i).X,busStationArrayList.get(i).Y);
        }
        quickSort(distances,0,distances.length-1);
        for(int i = 0;i < distances1.length;i++){
            if(distances1[i] == distances[0]){
                result = busStationArrayList.get(i);
            }
        }
        return result;
    }
    //快排
    public void quickSort(int[] arr,int low,int high){
        int i,j,temp,t;
        if(low>high){
            return;
        }
        i=low;
        j=high;
        temp = arr[low];

        while (i<j) {
            while (temp<=arr[j]&&i<j) {
                j--;
            }
            while (temp>=arr[i]&&i<j) {
                i++;
            }
            if (i<j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        //递归调用左半数组
        quickSort(arr, low, j-1);
        //递归调用右半数组
        quickSort(arr, j+1, high);
    }
}