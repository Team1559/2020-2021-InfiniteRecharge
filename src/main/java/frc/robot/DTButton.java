package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
//Mike, this one's for you to handle

public class DTButton {

    private boolean old, current;
    private Joystick stick;
    private int button;

    public DTButton(Joystick stick, int button) {
        this.stick = stick;
        this.button = button;
    }

    public void update() {
        update(stick.getRawButton(button));
    }

    public void update(boolean b) {
        old = current;
        current = b;
    }

    /**
     * @return if button is being held down
     */
    public boolean isDown() {
        return current;
    }

    /**
     * @return Falling edge of button press
     */
    public boolean isReleased() {
        return old && !current;
    }
} 