package com.obd.commands.engine;

import com.obd.commands.ObdCommand;
import com.obd.enums.AvailableCommandNames;

/**
 * Displays the current engine revolutions per minute (RPM).
 */
public class RPMCommand extends ObdCommand {

    private int rpm = -1;

    /**
     * Default ctor.
     */
    public RPMCommand() {
        super("01 0C");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link RPMCommand} object.
     */
    public RPMCommand(RPMCommand other) {
        super(other);
    }

    //C D A B
    //0 1 2 3  
    
    @Override
    protected void performCalculations() {
        // ignore first two bytes [41 0C] of the response((A*256)+B)/4
        rpm = (buffer.get(2) * 256 + buffer.get(3)) / 4;
    }

    /**
     * @return the engine RPM per minute
     */
    @Override
    public String getFormattedResult() {
        return String.format("%d%s", rpm, getResultUnit());
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(rpm);
    }

    @Override
    public String getResultUnit() {
        return "RPM";
    }

    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RPM.getValue();
    }

    /**
     * @return a int.
     */
    public int getRPM() {
        return rpm;
    }

}
