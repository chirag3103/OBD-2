package com.obd.commands.temperature;

import com.obd.enums.AvailableCommandNames;

/**
 * Engine Coolant Temperature.
 */
public class EngineCoolantTemperatureCommand extends TemperatureCommand {

    /**
     *
     */
    public EngineCoolantTemperatureCommand() {
        super("01 05");
    }

    /**
     * @param other a {@link TemperatureCommand} object.
     */
    public EngineCoolantTemperatureCommand(TemperatureCommand other) {
        super(other);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_COOLANT_TEMP.getValue();
    }

}
