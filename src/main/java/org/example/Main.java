package org.example;

import java.lang.reflect.Array;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int[] arr = {3,4,1,2,5,7,6,8,9,11,14,12,10,13,15,16,19,17,20,18,21,23};
        mergesort(arr,0,arr.length-1);
        for (int x : arr) {
            System.out.print(x + " ");
        }
    }

    public static void mergesort(int a[], int left, int right){
        if(left>=right){
            return;
        }
        int mid = (left+right)/2;
        mergesort(a,left,mid);
        mergesort(a,mid+1,right);
        merge(a,left,mid,right);
    }

    static void merge(int a[], int left, int mid, int right){
        int[] aux = new int[a.length];

        int i = left;
        int j = mid+1;
        int k = left;
        while(i<=mid && j<=right){
            if(a[i]<a[j]){
                aux[k] = a[i];
                i++;
                k++;
            }else {
                aux[k] = a[j];
                k++;
                j++;
            }

        }
        while (i<=mid){
            aux[k++] = a[i++];
        }
        while (j<=right){
            aux[k++] = a[j++];
        }
        for (int index = left; index <= right; index++) {
            a[index] = aux[index];
        }

    }
}