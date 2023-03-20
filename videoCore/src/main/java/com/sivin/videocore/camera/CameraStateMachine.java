package com.sivin.videocore.camera;

import com.sivin.videocore.utils.State;
import com.sivin.videocore.utils.StateMachine;

public class CameraStateMachine extends StateMachine {

    public static final int CMD_1 = 1;
    public static final int CMD_2 = 2;
    public static final int CMD_3 = 3;
    public static final int CMD_4 = 4;
    public static final int CMD_5 = 5;

    public static CameraStateMachine makeUp() {
        CameraStateMachine sm = new CameraStateMachine("hsm1");
        sm.start();
        return sm;
    }

    protected CameraStateMachine(String name) {
        super(name);
    }

    class State1 extends State {
        @Override
        public void enter() {
            super.enter();
        }
    }
}
