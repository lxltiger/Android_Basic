package com.lxl.tiger.designpattern.mvc;

import com.lxl.tiger.designpattern.mvc.controller.BeatControl;
import com.lxl.tiger.designpattern.mvc.controller.ControllerInterface;
import com.lxl.tiger.designpattern.mvc.controller.HeartBeatControl;
import com.lxl.tiger.designpattern.mvc.model.BeatModelImpl;
import com.lxl.tiger.designpattern.mvc.model.HeartModel;
import com.lxl.tiger.designpattern.mvc.model.HeartModelAdapter;
import com.lxl.tiger.designpattern.mvc.model.IBeatModel;
import com.lxl.tiger.designpattern.mvc.model.IHeartModel;

public class HeartBeatTestDrive {
    public static void main(String[] args) {
        IHeartModel heartModel=new HeartModel();
        ControllerInterface controller=new HeartBeatControl(heartModel);
    }
}
