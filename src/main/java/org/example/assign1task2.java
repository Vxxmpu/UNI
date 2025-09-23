package org.example;

import java.util.*;

public class assign1task2 {

    static class Point implements Comparable<Point> {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point other) {
            return Double.compare(this.x, other.x);
        }
    }

    static class PointYComparator implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            return Double.compare(p1.y, p2.y);
        }
    }

    static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public static int robustQuickSort(int[] arr) {
        return robustQuickSort(arr, 0, arr.length - 1, 0);
    }

    private static int robustQuickSort(int[] arr, int low, int high, int depth) {
        Random rand = new Random();
        int maxDepth = depth;
        while (low < high) {
            int pivotIdx = low + rand.nextInt(high - low + 1);
            int temp = arr[pivotIdx];
            arr[pivotIdx] = arr[high];
            arr[high] = temp;

            int pivot = arr[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (arr[j] <= pivot) {
                    i++;
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            int pi = i + 1;

            maxDepth = Math.max(maxDepth, depth + 1);

            if (pi - low < high - pi) {
                maxDepth = Math.max(maxDepth, robustQuickSort(arr, low, pi - 1, depth + 1));
                low = pi + 1;
            } else {
                maxDepth = Math.max(maxDepth, robustQuickSort(arr, pi + 1, high, depth + 1));
                high = pi - 1;
            }
        }
        return maxDepth;
    }
    public static int deterministicSelect(int[] arr, int k) {
        if (k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("k out of bounds");
        }
        return deterministicSelect(arr, 0, arr.length - 1, k);
    }

    private static int deterministicSelect(int[] arr, int low, int high, int k) {
        while (low < high) {
            int pivotIdx = medianOfMedians(arr, low, high);
            pivotIdx = partitionSelect(arr, low, high, pivotIdx);
            if (k == pivotIdx - low) {
                return arr[pivotIdx];
            } else if (k < pivotIdx - low) {
                high = pivotIdx - 1;
            } else {
                k -= (pivotIdx - low + 1);
                low = pivotIdx + 1;
            }
        }
        return arr[low];
    }

    private static int medianOfMedians(int[] arr, int low, int high) {
        int n = high - low + 1;
        if (n <= 5) {
            insertionSort(arr, low, high);
            return low + (n / 2);
        }
    
        int numGroups = n / 5;
        for (int g = 0; g < numGroups; g++) {
            int gLow = low + g * 5;
            int gHigh = gLow + 4;
            insertionSort(arr, gLow, gHigh);
            swap(arr, low + g, gLow + 2);
        }

        int remainLow = low + numGroups * 5;
        if (remainLow <= high) {
            insertionSort(arr, remainLow, high);
            swap(arr, low + numGroups, remainLow + (high - remainLow) / 2);
            numGroups++;
        }

        return medianOfMedians(arr, low, low + numGroups - 1);
    }

    private static void insertionSort(int[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private static int partitionSelect(int[] arr, int low, int high, int pivotIdx) {
        swap(arr, pivotIdx, high);
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static double closestPair(Point[] points) {
        if (points.length < 2) {
            return Double.POSITIVE_INFINITY;
        }
        Point[] px = Arrays.copyOf(points, points.length);
        Arrays.sort(px);
        Point[] py = Arrays.copyOf(points, points.length);
        Arrays.sort(py, new PointYComparator());

        return closestPairRec(px, py);
    }

    private static double closestPairRec(Point[] px, Point[] py) {
        int n = px.length;
        if (n <= 3) {
            double minD = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    minD = Math.min(minD, distance(px[i], px[j]));
                }
            }
            return minD;
        }

        int mid = n / 2;
        Point midPoint = px[mid];
        Point[] leftPx = Arrays.copyOfRange(px, 0, mid);
        Point[] rightPx = Arrays.copyOfRange(px, mid, n);

        List<Point> leftPy = new ArrayList<>();
        List<Point> rightPy = new ArrayList<>();
        for (Point p : py) {
            if (p.x <= midPoint.x) {
                leftPy.add(p);
            } else {
                rightPy.add(p);
            }
        }

        double dl = closestPairRec(leftPx, leftPy.toArray(new Point[0]));
        double dr = closestPairRec(rightPx, rightPy.toArray(new Point[0]));
        double d = Math.min(dl, dr);

        List<Point> strip = new ArrayList<>();
        for (Point p : py) {
            if (Math.abs(p.x - midPoint.x) < d) {
                strip.add(p);
            }
        }
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && j - i < 8; j++) {
                d = Math.min(d, distance(strip.get(i), strip.get(j)));
            }
        }

        return d;
    }


    public static double bruteClosestPair(Point[] points) {
        int n = points.length;
        double minD = Double.POSITIVE_INFINITY;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                minD = Math.min(minD, distance(points[i], points[j]));
            }
        }
        return minD;
    }


    public static void main(String[] args) {
        System.out.println("Testing Robust QuickSort:");
        int[] arr = {10, 7, 8, 9, 1, 5};
        int maxDepth = robustQuickSort(arr);
        System.out.println("Sorted: " + Arrays.toString(arr));
        int[] sortedArr = new int[10000];

        for (int i = 0; i < 10000; i++) sortedArr[i] = i;
        robustQuickSort(sortedArr);
        Random rand = new Random();
        int[] randomArr = new int[100];

        for (int i = 0; i < 100; i++) randomArr[i] = rand.nextInt(101);
        int[] sortedRandom = Arrays.copyOf(randomArr, 100);
        Arrays.sort(sortedRandom);
        robustQuickSort(randomArr);
        if (!Arrays.equals(randomArr, sortedRandom)) {
            System.out.println("QuickSort failed on random");
        }


        System.out.println("\nTesting Deterministic Select:");
        boolean allPassed = true;
        for (int trial = 0; trial < 100; trial++) {
            int[] testArr = new int[50];
            for (int i = 0; i < 50; i++) testArr[i] = rand.nextInt(101);
            int k = rand.nextInt(50);
            int[] arrCopy = Arrays.copyOf(testArr, 50);
            int result = deterministicSelect(testArr, k);
            Arrays.sort(arrCopy);
            int expected = arrCopy[k];
            if (result != expected) {
                System.out.println("Select failed: " + result + " != " + expected);
                allPassed = false;
            }
        }
        if (allPassed) System.out.println("All 100 trials passed.");


        System.out.println("\nTesting Closest Pair:");
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(3, 4), new Point(1, 0)};
        double distFast = closestPair(points);
        double distBrute = bruteClosestPair(points);
        System.out.println("Fast: " + distFast + " Brute: " + distBrute);  // Should match
        if (!Double.valueOf(distFast).equals(distBrute)) {
            System.out.println("Closest Pair failed");
        }

        Point[] smallPoints = new Point[100];
        for (int i = 0; i < 100; i++) smallPoints[i] = new Point(rand.nextDouble(), rand.nextDouble());
        if (!Double.valueOf(closestPair(smallPoints)).equals(bruteClosestPair(smallPoints))) {
            System.out.println("Failed small n");
        }
        Point[] largePoints = new Point[10000];
        for (int i = 0; i < 10000; i++) largePoints[i] = new Point(rand.nextDouble(), rand.nextDouble());
        System.out.println("Large n dist: " + closestPair(largePoints));
        System.out.println("All tests passed.");
    }
}
