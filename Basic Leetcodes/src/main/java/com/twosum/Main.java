package com.twosum;

import java.io.Console;
import java.util.*;
import java.util.Scanner;
import java.util.Random;

class Solution 
{
    public static Map<Object, Integer> frequencyMap = new HashMap<>();

    public int[] twoSum(int[] nums, int target) 
    {
        for (int i = 0; i < nums.length; i++)
        {
            for (int j = i+1; j < nums.length; j++)
            {
                if (target == nums[i] + nums[j]) 
                {
                    int[] res = {i, j};
                    return(res);
                }
            }
        }
        return nums;
    }

    public List<List<Integer>> fourSum(int[] nums, int target) 
    {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 4) 
        {
            return res;
        }

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 3; i++) 
        {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // Skip duplicates for i

            for (int j = i + 1; j < nums.length - 2; j++) 
            {
                if (j > i + 1 && nums[j] == nums[j - 1])
                {
                    continue;   // Skip duplicates for j
                }

                int left = j + 1;
                int right = nums.length - 1;

                while (left < right) 
                {
                    long sum = (long) nums[i] + (long) nums[j] + (long) nums[left] + (long) nums[right];

                    if (sum == target) 
                    {
                        res.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));

                        while (left < right && nums[left] == nums[left + 1]) left++; // Skip duplicates for left
                        while (left < right && nums[right] == nums[right - 1]) right--; // Skip duplicates for right

                        left++;
                        right--;
                    } 
                    else if (sum < target) 
                    {
                        left++;
                    } 
                    else 
                    {
                        right--;
                    }
                }
            }
        }

        return res;
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) 
    {
        if (nums1.length == 0 && nums2.length != 0) 
        {
            // If single element
            if (nums2.length == 1) 
            {
                return (nums2[0]);
            }

            double test = nums2.length/2.0 % 2;
            // If even
            if (nums2.length % 2 == 0) 
            {
                return (nums2[nums2.length/2] + nums2[(nums2.length/2) - 1])/2.0;
            }
            else
            {
                return nums2[nums2.length/2];
            }
        }
        else if (nums1.length != 0 && nums2.length == 0) 
        {
            // If single element
            if (nums1.length == 1) 
            {
                return (nums1[0]);
            }

            // If even
            if (nums1.length/2.0 % 2 == 0) 
            {
                return (nums1[nums1.length/2] + nums1[(nums1.length/2) - 1])/2.0;
            }
            else
            {
                return nums1[nums1.length/2];
            }
        }
        else if (nums1.length == 0 && nums2.length == 0) 
        {
            return 0.0;
        }

        double[] result = new double[nums1.length + nums2.length];

        for (int i = 0; i < nums1.length; i++) 
        {
            result[i] = nums1[i];
        }

        for (int i = 0; i < nums2.length; i++) 
        {
            result[nums1.length + i] = nums2[i];
        }

        Arrays.sort(result);

        // If even
        if (result.length/2 % 2.0 == 0) 
        {
            return (result[result.length/2] + result[(result.length/2) - 1])/2.0;
        }
        else
        {
            return result[result.length/2];
        }
    }

    public int myAtoi(String s) 
    {
        int[] numbers = new int[s.length()];
        int counter = 0;
        boolean numDetected = false;

        for (int i = 0; i < s.length(); i++)
        {
            if (((int)s.charAt(i) == 48) && !numDetected) 
            {
                numbers[counter] = (int)s.charAt(i) - 48;
                counter++;
                numDetected = true;
            }
            else if ((int)s.charAt(i) == 45 && !numDetected) 
            {
                numbers[counter] = -1;
                counter++;
            }
            else if ((int)s.charAt(i) >= 48 && (int)s.charAt(i) <= 57) 
            {
                numbers[counter] = (int)s.charAt(i) - 48;
                counter++;
                numDetected = true;
            }
            else if (!((int)s.charAt(i) >= 49 && (int)s.charAt(i) <= 57) && numDetected) 
            {
                break;
            }
            else
            {
                break;
            }
        }
        
        int sum = 0;
        int multiplier = 1;

        if (numbers[0] == -1) 
        {
            multiplier = -1;   
            for (int i = 1; i < counter; i++) 
            {
                sum = sum + numbers[i] * (int)Math.pow(10, (counter - i - 1));
            }    
        }
        else
        {
            for (int i = 0; i < counter; i++) 
            {
                sum = sum + numbers[i] * (int)Math.pow(10, (counter - i - 1));
            }    
        }
        
        return sum*multiplier; 
    }

    public StringBuilder reverseString(String s)
    {
        StringBuilder rs = new StringBuilder(s);
        int len = s.length() - 1;

        for (int i = len; i >= 0; i--)
        {
            rs.setCharAt(len - i, s.charAt(i));
        }
        return rs;
    }

    public int checkNumber(int g_num, int act_num)
    {
        if (g_num == act_num) 
        {
            return 0;
        }
        else if(g_num < act_num)
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

    public void guessingGame()
    {       
        Scanner scanner = new Scanner(System.in);

        int num;
        int randomInt = (int) (Math.random() * 10);
        boolean end = false;
        do
        {
            System.out.println("");
            System.out.print("Enter a number between 0 - 10: ");
            num = scanner.nextInt();

            switch (checkNumber(num, randomInt)) 
            {
                case -1:
                {
                    System.out.println("Number too low!");
                    break;
                }
                case 0:
                {
                    System.out.println("You guess correctly!");
                    end = true;
                    break;
                }
                case 1:
                {
                    System.out.println("Number too high!");
                    break;
                }
            }

        }while(!end);
        String choice = scanner.nextLine();
        System.out.print("Would you like to play again (y/n): ");
        choice = scanner.nextLine();

        if (choice.length() == 1 && (int) choice.charAt(0) == 121) 
        {
            guessingGame();    
        }

        scanner.close();
    }
}

public class Main {
    public static void main(String[] args) 
    {
        String test = "42";
        Solution myObj = new Solution();
        myObj.guessingGame();
        StringBuilder res = myObj.reverseString(test);
        System.out.println("Index = " + res);
    }
}