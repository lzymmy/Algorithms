package com.company;
import java.util.Scanner;


public class Interval_graph_coloring {
    public static class Activity {
        int label;
        int beginTime;
        int endTime;
        int roomLabel;
        boolean mark;

        public Activity(int label,int beginTime,int endTime,int roomLabel,boolean mark){
            this.label = label;
            this.beginTime = beginTime;
            this.endTime = endTime;
            this.roomLabel = roomLabel;
            this.mark = mark;
        }
    }
    //按照结束时间递增排序,使用快速排序
    public static void quickSort(Activity[] activities,int low,int high){
        int i,j;
        Activity temp;
        if(low>high){
            return;
        }
        i=low;
        j=high;
        temp = activities[low];
        while (i<j) {
            while (temp.endTime<=activities[j].endTime&&i<j) {
                j--;
            }
            while (temp.endTime>=activities[i].endTime&&i<j) {
                i++;
            }
            if (i<j) {
                Activity activity1 = activities[i];
                Activity activity2 = activities[j];
                activities[i] = activity2;
                activities[j] = activity1;
            }
        }
        activities[low] = activities[i];
        activities[i] = temp;
        //递归调用左半数组
        quickSort(activities, low, j-1);
        //递归调用右半数组
        quickSort(activities, j+1, high);
    }
    public static int select_room(Activity[] activities, int[] roomTime, int size) {
        //安排第一个活动
        int roomSum=1;
        int activitySum=1;
        roomTime[1] = activities[0].endTime;
        activities[0].roomLabel = 1;
        //遍历i个活动
        for (int i = 1; i < size; i++)
        { //遍历j个教室,把活动i放进哪个教室
            for(int j=1;j<=roomSum;j++)
                if (activities[i].beginTime >= roomTime[j] && (!activities[i].mark))
                {
                    activities[i].roomLabel = j;
                    activities[i].mark = true;
                    roomTime[j] = activities[i].endTime;
                    activitySum++;
                }
            if (activitySum < size&&i == size - 1)
            {//控制重新遍历的条件
                i = 0;
                roomSum++;
            }
        }
        return roomSum;
    }
    public static void main(String[] args) {
        System.out.println("请输入活动数量：");
        Scanner input = new Scanner(System.in);
        int size = input.nextInt();
        int[] roomTime = new int[size];
        Activity[] activities = new Activity[size];
        for (int i = 0;i < size;i++){
            System.out.println("活动"+(i+1)+"开始时间：");
            Scanner scanner1 = new Scanner(System.in);
            int beginTime = scanner1.nextInt();
            System.out.println("活动"+(i+1)+"结束时间：");
            Scanner scanner2 = new Scanner(System.in);
            int endTime = scanner2.nextInt();
            activities[i] = new Activity(i,beginTime,endTime,0,false);
        }
        quickSort(activities,0,size-1);
        int roomNum = select_room(activities,roomTime,size);
        System.out.println("所占空间总数量："+roomNum);
        for (int i = 0;i < size;i++){
            System.out.println("活动"+activities[i].label+"在空间"+activities[i].roomLabel);
        }
    }
}
