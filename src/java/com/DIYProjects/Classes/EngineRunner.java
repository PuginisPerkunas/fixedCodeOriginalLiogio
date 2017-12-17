/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DIYProjects.Classes;
import com.pi4j.io.gpio.*;
/**
 *
 * @author root
 */
public class EngineRunner implements Runnable{
    
    private GpioPinDigitalOutput frontLeftPIN1;
    private GpioPinDigitalOutput frontLeftPIN2;
    private GpioPinDigitalOutput frontLeftPIN3;
    
    private GpioPinDigitalOutput frontRightPIN1;
    private GpioPinDigitalOutput frontRightPIN2;
    private GpioPinDigitalOutput frontRightPIN3;
    
    private GpioPinDigitalOutput rearLeftPIN1;
    private GpioPinDigitalOutput rearLeftPIN2;
    private GpioPinDigitalOutput rearLeftPIN3;
    
    private GpioPinDigitalOutput rearRightPIN1;
    private GpioPinDigitalOutput rearRightPIN2;
    private GpioPinDigitalOutput rearRightPIN3;
    private final DrivenAxis axis;
    
    private GpioPinPwmOutput left;
    private GpioPinPwmOutput right;
    
    private int speed;
    private boolean stopThread;
    private Thread runnerThread;
    
    private static EngineRunner instance;
    
    public static EngineRunner getInstance(GpioPinDigitalOutput[] axisGPIOPins, DrivenAxis axis) {
        if (instance == null)
            instance = new EngineRunner(axisGPIOPins, axis);
        
        return instance;
    }
    
    public static EngineRunner getInstance(GpioPinPwmOutput frontLeft, GpioPinPwmOutput frontRight) {
        if (instance == null)
            instance = new EngineRunner(frontLeft, frontRight);
        
        return instance;
    }
    
    public static EngineRunner getInstance(GpioPinDigitalOutput frontLeft, GpioPinDigitalOutput frontRight) {
        if (instance == null)
            instance = new EngineRunner(frontLeft, frontRight);
        
        return instance;
    }
    
    private EngineRunner(GpioPinPwmOutput frontLeft, GpioPinPwmOutput frontRight)
    {
        this.axis = DrivenAxis.FRONT;
        left = frontLeft;
        right = frontRight;
    }
    
    private EngineRunner(GpioPinDigitalOutput frontLeft, GpioPinDigitalOutput frontRight)
    {
        this.axis = DrivenAxis.FRONT;
        frontLeftPIN3 = frontLeft;
        frontRightPIN3 = frontRight;
    }
    
    private EngineRunner(GpioPinDigitalOutput[] axisGPIOPins, DrivenAxis axis)
    {
        this.axis = axis;
        
        if (axis == DrivenAxis.FRONT)
        {
            frontLeftPIN1 = axisGPIOPins[0];
            frontLeftPIN2 = axisGPIOPins[1];
            frontLeftPIN3 = axisGPIOPins[2];

            frontRightPIN1 = axisGPIOPins[3];
            frontRightPIN2 = axisGPIOPins[4];
            frontRightPIN3 = axisGPIOPins[5];
        }
        else if (axis == DrivenAxis.REAR)
        {
            rearLeftPIN1 = axisGPIOPins[0];
            rearLeftPIN2 = axisGPIOPins[1];
            rearLeftPIN3 = axisGPIOPins[2];

            rearRightPIN1 = axisGPIOPins[3];
            rearRightPIN2 = axisGPIOPins[4];
            rearRightPIN3 = axisGPIOPins[5];
        }

        speed = 0;
    }
    
    public EngineRunner(GpioPinDigitalOutput[] frontAxisGPIOPins, GpioPinDigitalOutput[] rearAxisGPIOPins)
    {
        axis = DrivenAxis.ALL;
        
        frontLeftPIN1 = frontAxisGPIOPins[0];
        frontLeftPIN2 = frontAxisGPIOPins[1];
        frontLeftPIN3 = frontAxisGPIOPins[2];
        
        frontRightPIN1 = frontAxisGPIOPins[3];
        frontRightPIN2 = frontAxisGPIOPins[4];
        frontRightPIN3 = frontAxisGPIOPins[5];
        
        rearLeftPIN1 = rearAxisGPIOPins[0];
        rearLeftPIN2 = rearAxisGPIOPins[1];
        rearLeftPIN3 = rearAxisGPIOPins[2];
        
        rearRightPIN1 = rearAxisGPIOPins[3];
        rearRightPIN2 = rearAxisGPIOPins[4];
        rearRightPIN3 = rearAxisGPIOPins[5];
        
        speed = 0;
        stopThread = false;
    }
    
    @Override
    public void run()
    {
        speed = 30;
        while(!stopThread)
        {
            
            frontLeftPIN3.setState(PinState.HIGH);
            frontRightPIN3.setState(PinState.HIGH);
            waitFor(60);
            frontLeftPIN3.setState(PinState.LOW);
            frontRightPIN3.setState(PinState.LOW);
            waitFor(40);
            
            //left.setPwm(400);
            //right.setPwm(400);
            //waitFor(1000);

            /*
            else
            {      
                while (speed > 60 && stopThread == false)
                {
                    frontLeftPIN1.setState(PinState.HIGH);
                    frontLeftPIN2.setState(PinState.LOW);
                    frontLeftPIN3.setState(PinState.HIGH);
                    
                    frontRightPIN1.setState(PinState.HIGH);
                    frontRightPIN2.setState(PinState.LOW);
                    frontRightPIN3.setState(PinState.HIGH);
                    
                    waitFor(100);
                }               
            }
            */
        }
    }
    
    public void start()
    {
        stopThread = false;
        if (runnerThread == null)
        {
            runnerThread = new Thread(this, "runnerThread");
            runnerThread.start();
        }
    }
    
    private void waitFor(long ms)
    {
        try            
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException exc)
        {
            
        }
    }
    
    public void stop()
    {
        speed = 0;
        stopThread = true;
        instance = null;
    }
    
    public void setSpeed(int speed)
    {
        this.speed = speed;
        if (this.speed < 10)
            this.speed = 10;
        
        if (stopThread == true || runnerThread == null)
        {
            stopThread = false;
            runnerThread = new Thread(this, "runnerThread");
            runnerThread.start();
        }
    }
}

