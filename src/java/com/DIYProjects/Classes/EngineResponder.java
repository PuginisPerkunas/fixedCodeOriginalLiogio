/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DIYProjects.Classes;

import com.pi4j.io.gpio.*;
/**
 *
 * @author Mindaugas Liogys
 */
public class EngineResponder implements EngineListener {
    
    private int speed = 0;
    private GpioPinDigitalOutput frontLeftPIN1;
    private GpioPinDigitalOutput frontLeftPIN2;
    //private GpioPinDigitalOutput frontLeftPIN3;
    private GpioPinPwmOutput frontLeftPIN3;
    
    private GpioPinDigitalOutput frontRightPIN1;
    private GpioPinDigitalOutput frontRightPIN2;
    //private GpioPinDigitalOutput frontRightPIN3;
    private GpioPinPwmOutput frontRightPIN3;
    
    private GpioPinDigitalOutput rearLeftPIN1;
    private GpioPinDigitalOutput rearLeftPIN2;
    private GpioPinDigitalOutput rearLeftPIN3;
    
    private GpioPinDigitalOutput rearRightPIN1;
    private GpioPinDigitalOutput rearRightPIN2;
    private GpioPinDigitalOutput rearRightPIN3;
    
    private DrivenAxis drivenAxis;
    private EngineRunner engineRunner;
    
    public EngineResponder(int[] frontAxisPins, int[] rearAxisPins) throws Exception
    {
        
        if (frontAxisPins != null)
        {
            if (frontAxisPins.length == 6)
            {
                assignPinsToFrontAxis(frontAxisPins);
                com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
                //com.pi4j.wiringpi.Gpio.pwmSetRange(1000);
                //com.pi4j.wiringpi.Gpio.pwmSetClock(500);
                
                if (rearAxisPins != null)
                {
                    if (frontAxisPins.length == 6)
                    {
                        assignPinsToRearAxis(rearAxisPins);
                        drivenAxis = DrivenAxis.ALL;
                        //engineRunner = new EngineRunner(
                        //        new GpioPinDigitalOutput[] { frontLeftPIN1, frontLeftPIN2, frontLeftPIN3, frontRightPIN1, frontRightPIN2, frontRightPIN3 },
                         //       new GpioPinDigitalOutput[] { rearLeftPIN1, rearLeftPIN2, rearLeftPIN3, rearRightPIN1, rearRightPIN2, rearRightPIN3 }
                        //);
                    }
                    else
                        throw new Exception("REAR: Illegal number of pins");
                }
                else
                {
                    drivenAxis = DrivenAxis.FRONT;
                    //engineRunner = new EngineRunner(
                    //    new GpioPinDigitalOutput[] { frontLeftPIN1, frontLeftPIN2, frontLeftPIN3, frontRightPIN1, frontRightPIN2, frontRightPIN3 },
                    //    drivenAxis);
                }
            }
            else
                throw new Exception("FRONT: Illegal number of pins");           
        }
        else
        {
            if (rearAxisPins != null)
            {
                if (rearAxisPins.length == 6)
                {
                    assignPinsToRearAxis(rearAxisPins);
                    drivenAxis = DrivenAxis.REAR;
                    //engineRunner = new EngineRunner(
                    //    new GpioPinDigitalOutput[] { rearLeftPIN1, rearLeftPIN2, rearLeftPIN3, rearRightPIN1, rearRightPIN2, rearRightPIN3 },
                    //    drivenAxis);
                }
                else
                    throw new Exception("REAR: Illegal number of pins");           
            }
            else
                throw new Exception("FRONT + REAR: No pins assigned");
        }
        //engineRunner.start();
    }
    
    private void assignPinsToFrontAxis(int[] frontAxisPins)
    {
        frontLeftPIN1 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(frontAxisPins[0]), PinState.HIGH);
        frontLeftPIN2 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(frontAxisPins[1]), PinState.LOW);
        //rontLeftPIN3 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(frontAxisPins[2]), PinState.LOW);
        frontLeftPIN3 = Controller.GPIO.provisionPwmOutputPin(RaspiPin.getPinByAddress(frontAxisPins[2]));
        //com.pi4j.wiringpi.SoftPwm.softPwmCreate(frontAxisPins[2], 0, 100);
        
        frontRightPIN1 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(frontAxisPins[3]), PinState.HIGH);
        frontRightPIN2 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(frontAxisPins[4]), PinState.LOW);
        //frontRightPIN3 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(frontAxisPins[5]), PinState.LOW);
        frontRightPIN3 = Controller.GPIO.provisionPwmOutputPin(RaspiPin.getPinByAddress(frontAxisPins[5]));
        //com.pi4j.wiringpi.SoftPwm.softPwmCreate(frontAxisPins[5], 0, 100);
    }
    
    private void assignPinsToRearAxis(int[] rearAxisPins)
    {
        rearLeftPIN1 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(rearAxisPins[0]), PinState.HIGH);
        rearLeftPIN2 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(rearAxisPins[1]), PinState.LOW);
        rearLeftPIN3 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(rearAxisPins[2]), PinState.LOW);

        rearRightPIN1 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(rearAxisPins[3]), PinState.HIGH);
        rearRightPIN2 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(rearAxisPins[4]), PinState.LOW);
        rearRightPIN3 = Controller.GPIO.provisionDigitalOutputPin(RaspiPin.getPinByAddress(rearAxisPins[5]), PinState.LOW);
    }
    
    @Override
    public void onStop()
    {
        speed = 0;
        //if (engineRunner != null)
        //    engineRunner.stop();
        
        switch (drivenAxis)
        {
            case FRONT:
            {
                stopFrontAxis();
                break;
            }
            case REAR:
            {
                stopRearAxis();
                break;
            }
            case ALL:
            {
                stopFrontAxis();
                stopRearAxis();
                break;
            }
        }
    }
    
    private void stopFrontAxis()
    {
        frontLeftPIN1.setState(PinState.HIGH);
        frontLeftPIN2.setState(PinState.LOW);
        //frontLeftPIN3.setState(PinState.LOW);
        frontLeftPIN3.setPwm(0);
        
        frontRightPIN1.setState(PinState.HIGH);
        frontRightPIN2.setState(PinState.LOW);
        //frontRightPIN3.setState(PinState.LOW);
        frontRightPIN3.setPwm(0);
        
        //engineRunner.stop();
        
        //com.pi4j.wiringpi.SoftPwm.softPwmWrite(1, 0);
        //com.pi4j.wiringpi.SoftPwm.softPwmWrite(26, 0);
    }
    
    private void stopRearAxis()
    {
        rearLeftPIN1.setState(PinState.LOW);
        rearLeftPIN2.setState(PinState.LOW);
        rearLeftPIN3.setState(PinState.LOW);
        
        rearRightPIN1.setState(PinState.LOW);
        rearRightPIN2.setState(PinState.LOW);
        rearRightPIN3.setState(PinState.LOW);
    }
    
    @Override
    public int onSpeedDown()
    {        
        if (speed <= 10)
            onStop();
        else
            speed -= 10;
        
        //engineRunner.setSpeed(speed);
        return speed;
    }
    
    @Override
    public int onSlowSpeed()
    {       
        frontLeftPIN1.setState(PinState.HIGH); 
        frontLeftPIN2.setState(PinState.LOW); 
       
        frontRightPIN1.setState(PinState.HIGH);
        frontRightPIN2.setState(PinState.LOW);
        
        frontLeftPIN3.setPwm(300);
        frontRightPIN3.setPwm(300);
        
        return 300;
    }
    
    @Override
    public int onMediumSpeed()
    {
        frontLeftPIN1.setState(PinState.HIGH); 
        frontLeftPIN2.setState(PinState.LOW); 
       
        frontRightPIN1.setState(PinState.HIGH);
        frontRightPIN2.setState(PinState.LOW);
        
        frontLeftPIN3.setPwm(500);
        frontRightPIN3.setPwm(500);
        
        return 500;
    }
    
    @Override
    public int onFastSpeed()
    {
        frontLeftPIN1.setState(PinState.HIGH); 
        frontLeftPIN2.setState(PinState.LOW); 
       
        frontRightPIN1.setState(PinState.HIGH);
        frontRightPIN2.setState(PinState.LOW);
        
        frontLeftPIN3.setPwm(800);
        frontRightPIN3.setPwm(800);
        
        return 800;
    }
    
    @Override
    public int onFullSpeed()
    {
        frontLeftPIN1.setState(PinState.HIGH);
        frontLeftPIN2.setState(PinState.LOW);
       
        frontRightPIN1.setState(PinState.HIGH);
        frontRightPIN2.setState(PinState.LOW);
        
        frontRightPIN3.setPwm(1024);
        frontLeftPIN3.setPwm(1024);
        
        return 1024;
    }
    
    @Override
    public int onSpeedUp()
    {        
        //if (speed < 512)
        //    speed += 30;
        //else
        //    speed = 100;
        //speed = 100;
        if (speed == 0)
        {
            /*
            engineRunner = EngineRunner.getInstance(
                new GpioPinDigitalOutput[] { frontLeftPIN1, frontLeftPIN2, frontLeftPIN3, frontRightPIN1, frontRightPIN2, frontRightPIN3 },
                drivenAxis);
            engineRunner.start();
            engineRunner.setSpeed(30);
            */
            frontLeftPIN1.setState(PinState.HIGH);
            frontLeftPIN2.setState(PinState.LOW);
            //frontLeftPIN3.setPwm(512);

            frontRightPIN1.setState(PinState.HIGH);
            frontRightPIN2.setState(PinState.LOW);
            //frontRightPIN3.setPwm(512);
        } else if (speed == 512)
        {
            //engineRunner.setSpeed(60);
            frontLeftPIN1.setState(PinState.HIGH);
            frontLeftPIN2.setState(PinState.LOW);
            //frontLeftPIN3.setPwm(768);

            frontRightPIN1.setState(PinState.HIGH);
            frontRightPIN2.setState(PinState.LOW);
            //frontRightPIN3.setPwm(768);
        }
        else {
            //engineRunner.stop();
            frontLeftPIN1.setState(PinState.HIGH);
            frontLeftPIN2.setState(PinState.LOW);
            //frontLeftPIN3.setState(PinState.HIGH);
            //frontLeftPIN3.setPwm(1000);

            frontRightPIN1.setState(PinState.HIGH);
            frontRightPIN2.setState(PinState.LOW);
            //frontRightPIN3.setState(PinState.HIGH);
            //frontRightPIN3.setPwm(1000);
        }
        
        //speed = frontLeftPIN3.getPwm();
        
        return speed;
    }
    
    @Override
    public void onBackwards()
    {
        if (drivenAxis == DrivenAxis.FRONT)
            reverseFront();
        else if (drivenAxis == DrivenAxis.REAR)
            reverseRear();
        else
        {
            reverseFront();
            reverseRear();
        }
    }
    
    
    private void reverseFront()
    {
        frontLeftPIN1.setState(PinState.LOW);
        frontLeftPIN2.setState(PinState.HIGH);
        //frontLeftPIN3.setState(PinState.HIGH);
        //frontLeftPIN3.setPwm(1024);
        
        frontRightPIN1.setState(PinState.LOW);
        frontRightPIN2.setState(PinState.HIGH);
        //frontRightPIN3.setState(PinState.HIGH);
        //frontRightPIN3.setPwm(1024);
    }
    
    private void reverseRear()
    {
        rearLeftPIN1.setState(PinState.LOW);
        rearLeftPIN2.setState(PinState.HIGH);
        rearLeftPIN3.setState(PinState.HIGH);
        
        rearRightPIN1.setState(PinState.LOW);
        rearRightPIN2.setState(PinState.HIGH);
        rearRightPIN3.setState(PinState.HIGH);
    }
    
    @Override
    public void onForward()
    {
        //pin1.setState(PinState.HIGH);
        //pin2.setState(PinState.LOW);
        //pin3.setState(PinState.HIGH);
    }
    
    
}

