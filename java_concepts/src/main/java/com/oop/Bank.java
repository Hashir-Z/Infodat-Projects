package com.oop;
import java.io.*;

interface Vehicle 
{
    // all are the abstract methods.
    void changeGear(int a);
    void speedUp(int a);
    void applyBrakes(int a);
}

class Bicycle  implements Vehicle
{
    int gear = 0;
    int speed = 0;

    @Override
    public void changeGear(int newGear)
    {
        gear = newGear;
    }

    @Override
    public void speedUp(int increment)
    {
        speed += increment;
    }

    @Override
    public void applyBrakes(int decrement)
    {
        speed -= decrement;
    }
    
    public void printStates() {
        System.out.println("speed: " + speed
            + " gear: " + gear);
    }
}

class Bike implements Vehicle
{
    int gear = 0;
    int speed = 0;

    static int test1 = 5;
    static int test2 = 10;
    
    
    @Override
    public void changeGear(int newGear)
    {
        gear = newGear;
    }

    @Override
    public void speedUp(int increment)
    {
        speed += increment;
    }

    @Override
    public void applyBrakes(int decrement)
    {
        speed -= decrement;
    }
    
    public void printStates() 
    {
        System.out.println("speed: " + speed
            + " gear: " + gear);
        System.out.println("Test 1 Bike: " + test1);
        System.out.println("Test 2 Bike: " + test2);
    }
}

class test extends Bike
{
    static public void testPrint()
    {
        System.out.println("Test 1: " + test1);
        System.out.println("Test 2: " + test2);
        test1 = 100;
        test2 = 200;
        System.out.println("Test 1: " + test1);
        System.out.println("Test 2: " + test2);
    }
}
// class Main
// {
//     // Driver Code
//     public static void main(String[] args)
//     {
//         test obj = new test();
//         obj.testPrint();
        
//         // creating instance of the bike.
//         Bike bike = new Bike();
//         bike.changeGear(1);
//         bike.speedUp(4);
//         bike.applyBrakes(3);
        
//         System.out.println("Bike present state :");
//         bike.printStates();
//     }
// }


// ### Introduction to Multi-Level Inheritance in Java

// In this exercise, we will explore the concept of multi-level inheritance in Java by creating a program that simulates basic banking operations. The program is structured in multiple levels, each building upon the previous one to demonstrate how inheritance works in Java.

// #### Key Concepts:
// 1. **Interfaces**: Define a contract that classes can implement.
// 2. **Abstract Classes**: Provide a base class with some implemented methods and some abstract methods that must be implemented by subclasses.
// 3. **Inheritance**: Allows a class to inherit properties and methods from another class.

// #### Program Structure:
// 1. **Level 1**: Define an interface `Bank` with methods for basic banking operations.
// 2. **Level 2**: Create an abstract class `Dev1` that implements the `Bank` interface and provides an implementation for the `deposit` method.
// 3. **Level 3**: Extend `Dev1` with another abstract class `Dev2` that implements the `withdraw` method.
// 4. **Level 4**: Create a concrete class `Dev3` that extends `Dev2` and provides empty implementations for the remaining methods.
// 5. **Main Class**: The `GFG` class contains the `main` method, where we create an instance of `Dev3` and call the banking methods.

// By following these steps, you will understand how to use interfaces, abstract classes, and inheritance to build a structured and modular program in Java.

/**
 * Bank

 */
interface Bank 
{
    void deposit();
    void withdraw();
    void loan();
    void account();    
}

abstract class Dev1 implements Bank
{
    public void deposit()
    {
        System.out.println("Your Deposit Amount = " + 100);
    }
}

abstract class Dev2 extends Dev1
{
    public void withdraw()
    {
        System.out.println("Your Withdraw Amount = " + 100);
    }
}

class Dev3 extends Dev2
{
    public void loan(){}
    public void account(){}
}

class Main
{
    // Driver Code
    public static void main(String[] args)
    {
        Dev3 obj = new Dev3();
        obj.deposit();
        obj.withdraw();
        obj.loan();
        obj.account();
    }
}
