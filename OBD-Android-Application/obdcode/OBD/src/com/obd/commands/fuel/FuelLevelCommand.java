package com.obd.commands.fuel;

import com.obd.commands.ObdCommand;
import com.obd.commands.PercentageObdCommand;
import com.obd.enums.AvailableCommandNames;

/**
 * Get fuel level in percentage
 */
public class FuelLevelCommand extends PercentageObdCommand {

    public FuelLevelCommand() {
        super("01 2F");
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        percentage = 100.0f * buffer.get(2) / 255.0f;
    }

    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_LEVEL.getValue();
    }

    /**
     * @return a float.
     */
    public float getFuelLevel() {
        return percentage;
    }

}
