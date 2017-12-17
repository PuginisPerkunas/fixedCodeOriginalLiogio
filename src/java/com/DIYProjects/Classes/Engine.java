/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DIYProjects.Classes;

/**
 *
 * @author Mindaugas Liogys
 */
public class Engine {
    private EngineListener listener = null;
    public int musuGreitis = 512;
    
    public Engine()
    {

    }
    
    public Engine(EngineListener engineListener)
    {
        this.listener = engineListener;
    }
    
    public void Stop()
    {
        if (listener != null)
            listener.onStop();
    }
    
    public int SpeedDown() throws Exception
    {
        if (listener != null) {
            musuGreitis-=100;
             int speed = listener.onChosenSpeed(musuGreitis);
            return speed;
        }
        else
            throw new Exception("EngineListener error");
    }
    
    public int ChosenSpeed() throws Exception
    {
        if (listener != null) {
            int speed = listener.onChosenSpeed(musuGreitis);
            
            return speed;
        }
        else
            throw new Exception("EngineListener error");
    }
    
        public int SpeedUp() throws Exception
    {
        if (listener != null) {
            musuGreitis+= 100;
            int speed = listener.onChosenSpeed(musuGreitis);
            return speed;
        }
        else
            throw new Exception("EngineListener error");
    }

    public void Backward()
    {
        if (listener != null)
            listener.onBackwards();
    }
    
    public void Forward()
    {
        if (listener != null)
            listener.onForward();
    }

    public void Left()
    {
        if (listener != null)
            listener.onLeft();
    }

    public void Right()
    {
        if (listener != null)
            listener.onRight();
    }
}

