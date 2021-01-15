package com.company;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MaxSubarray {
    public static void main(String[] args) {
        //int[] array = { 0, 13, -3, -25, 20, -3, -16, -23,18,20,-7,12,-5,-22,15,-4,7,-11};
        int[] array = {-13, -3, -25, -20, -3, -16, -23,-18,-20,-7,-12,-5,-22,-15,-4,-7,-11};
        System.out.println("最大子数组的下标为" + dynamic_maxSubarray(array)[0] + ":"
                + dynamic_maxSubarray(array)[1] + ",和为：" + dynamic_maxSubarray(array)[2]);

        Map result = recursion_MaxSubarray(array, 0, array.length - 1);
        System.out.println("最大子数组的下标为"+result.get("low")+":"+result.get("height")+",和为："+result.get("max"));

        System.out.println("最大子数组的下标为" + force_maxSubarray(array)[0] + ":"
                + force_maxSubarray(array)[1] + ",和为：" + force_maxSubarray(array)[2]);
        for(int i = 2;i < 300;i++) {
            int[] array1 = readArray(i);
            long startTime1 = System.nanoTime();
            int[] dynamic_array = dynamic_maxSubarray(array1);
            long endTime1 = System.nanoTime(); //获取结束时间
            //System.out.println("规模为："+i+"动态规划程序运行时间： " + (endTime1 - startTime1) + "ns" + "最大子数组的下标为" + dynamic_array[0] + ":"
            //        + dynamic_array[1] + ",和为：" + dynamic_array[2]);
            long startTime2 = System.nanoTime();
            int[] force_array = force_maxSubarray(array1);
            long endTime2 = System.nanoTime(); //获取结束时间
            //System.out.println("规模为："+i+"暴力程序运行时间： " + (endTime2 - startTime2) + "ns" + "最大子数组的下标为" + force_array[0] + ":"
            //        + force_array[1] + ",和为：" + force_array[2]);
            long startTime3 = System.nanoTime();
            Map recursion_array = recursion_MaxSubarray(array1, 0, array1.length - 1);
            long endTime3 = System.nanoTime(); //获取结束时间
            //System.out.println("规模为："+i+"分治法程序运行时间： " + (endTime3 - startTime3) + "ns" + "最大子数组的下标为" + recursion_array.get("low") + ":"
            //+ recursion_array.get("height") + ",和为：" + recursion_array.get("max"));
            long a = ((endTime2 - startTime2)-(endTime1 - startTime1));
            long b = ((endTime2 - startTime2)-(endTime3 - startTime3));
            long c = ((endTime3 - startTime3)-(endTime1 - startTime1));
            if(a < 0){
                a = 0-a;
            }
            if(b < 0){
                b = 0-b;
            }
            if(c < 0){
                c = 0-c;
            }
            if((a < 2000)||(b <2000)||(c < 2000)) {
                System.out.println("规模为:" + i + " 暴力-动态：" + a + " 暴力-分治: " + b + " 分治-动态: " + c);
            }
        }
    }

    public static int[] dynamic_maxSubarray(int[] array) {
        int sum1 = 0;
        int sum2 = 0;
        int begin = 0;
        int end = 0;
        for (int i = 0; i < array.length; i++) {
            //和为小于0表明需要重置开始位置，重新计算
            if (sum2 < 0) {
                sum2 = array[i];
                begin = i;
            } else {
                sum2 += array[i];
            }
            //如果暂时加了负数，sum2<sum1,如果再加正数导致sum2>sum1则更新sum1
            if (sum1 < sum2) {
                sum1 = sum2;
                end = i;
            }
        }
        int[] result = { begin, end, sum1 };
        return result;
    }

    public static int[] force_maxSubarray(int[] array) {
        int sum=Integer.MIN_VALUE;
        int tempSum=0;
        int start=-1;
        int end=-1;
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j < array.length; j++) {
                tempSum=0;
                for (int k = i; k <=j; k++) {
                    tempSum+=array[k];
                }
                if (tempSum>sum) {
                    sum=tempSum;
                    end=j;
                    start=i;
                }
            }
        }
        int[] result={start,end,sum};
        return result;
    }

    public static Map recursion_MaxSubarray(int[] array, int low, int height) {
        Map<String, Integer> map = new HashMap();
        if (low == height) {
            map.put("low", low);
            map.put("height", height);
            map.put("max",array[low]);
            return map;
        }

        int mid = (low+height)/2;//分解
        Map<String, Integer> left = recursion_MaxSubarray(array, low, mid);//解决
        Map<String, Integer> right = recursion_MaxSubarray(array, mid+1, height);// 解决
        Map<String, Integer> midMap = maxCrossingSubarray(array, low, mid, height);//合并

        Map retMap = new HashMap();
        if (left.get("max") >= right.get("max") && left.get("max") >= midMap.get("max")) {
            retMap.put("low", left.get("low"));
            retMap.put("height", left.get("height"));
            retMap.put("max", left.get("max"));
        } else if (right.get("max") >= left.get("max") && right.get("max") >= midMap.get("max")) {
            retMap.put("low", right.get("low"));
            retMap.put("height", right.get("height"));
            retMap.put("max", right.get("max"));
        } else {
            retMap.put("low", midMap.get("low"));
            retMap.put("height", midMap.get("height"));
            retMap.put("max", midMap.get("max"));
        }

        return retMap;
    }

    //求横穿中间点的最大子数组
    public static Map maxCrossingSubarray(int[] array, int low, int mid, int height) {
        int leftSum = -65535;
        int maxLeft  = 0;
        int tempSum = 0;
        for (int i = mid; i >= low; i--) {
            tempSum = tempSum + array[i];
            if (tempSum > leftSum) {
                leftSum = tempSum;
                maxLeft = i;
            }
        }

        tempSum = 0;
        int rightSum = -65535;
        int maxRight = 0;
        for (int j = mid+1; j <= height; j++) {
            tempSum = tempSum + array[j];
            if (tempSum > rightSum) {
                rightSum = tempSum;
                maxRight = j;
            }
        }

        Map<String, Integer> map = new HashMap();
        map.put("low", maxLeft);
        map.put("height", maxRight);
        map.put("max", leftSum+rightSum);
        return map;
    }
    public static int[] readArray(int size){
        int[] array = new int[size];
        for(int i = 0;i < array.length;i++){
            int a = (int) (Math.random()*40);
            a = a-20;
            array[i] = a;
        }
        return array;
    }
}