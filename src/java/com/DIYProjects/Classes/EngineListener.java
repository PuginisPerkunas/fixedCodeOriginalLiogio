/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DIYProjects.Classes;

/**
 *
 * @author root
 */
public interface EngineListener {
    void onStop();
    void onBackwards();
    void onForward();
    int onChosenSpeed(int speed);
    void onLeft();
    void onRight();
}
