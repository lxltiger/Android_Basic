package com.lxl.tiger.designpattern.mvc;

import com.lxl.tiger.designpattern.mvc.controller.BeatControl;
import com.lxl.tiger.designpattern.mvc.controller.ControllerInterface;
import com.lxl.tiger.designpattern.mvc.model.BeatModelImpl;
import com.lxl.tiger.designpattern.mvc.model.IBeatModel;

public class DJTestDrive {
    public static void main(String[] args) {

        IBeatModel beatModel = new BeatModelImpl();
        ControllerInterface controller=new BeatControl(beatModel);
    }
}
