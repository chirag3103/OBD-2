package com.obd.commands.engine;

import com.obd.commands.PercentageObdCommand;
import com.obd.enums.AvailableCommandNames;

public class AbsoluteLoadCommand extends PercentageObdCommand {

    /**
     * Default ctor.
     */
    public AbsoluteLoadCommand() {
        super("01 43");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link AbsoluteLoadCommand} object.
     */
    public AbsoluteLoadCommand(AbsoluteLoadCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        int a = buffer.get(2);
        int b = buffer.get(3);
        percentage = (a * 256 + b) * 100 / 255;
    }

    /**
     * @return a double.
     */
    public double getRatio() {
        return percentage;
    }

    @Override
    public String getName() {
        return AvailableCommandNames.ABS_LOAD.getValue();
    }

}
