package com.obd.reader.config;
    
import java.util.ArrayList;

import com.obd.commands.ObdCommand;
import com.obd.commands.SpeedCommand;
import com.obd.commands.control.TroubleCodesCommand;
import com.obd.commands.engine.LoadCommand;
import com.obd.commands.engine.MassAirFlowCommand;
import com.obd.commands.engine.RPMCommand;
import com.obd.commands.engine.RuntimeCommand;
import com.obd.commands.engine.ThrottlePositionCommand;

/**
 * TODO put description
 */
public final class ObdConfig {

    public static ArrayList<ObdCommand> getCommands() {
        ArrayList<ObdCommand> cmds = new ArrayList();
        cmds.add(new TroubleCodesCommand());
        cmds.add(new LoadCommand());
        cmds.add(new RPMCommand());
        cmds.add(new RuntimeCommand());
        cmds.add(new MassAirFlowCommand());
        cmds.add(new ThrottlePositionCommand());
        cmds.add(new SpeedCommand());
        
        // Control
        //cmds.add(new CommandControlModuleVoltageObdCommand());
//        cmds.add(new ModuleVoltageCommand());
//        cmds.add(new EquivalentRatioCommand());
//        cmds.add(new DistanceTraveledSinceCodesClearedCommand());
//        cmds.add(new DistanceTraveledWithMILOnCommand());
//        cmds.add(new DtcNumberCommand());
//        cmds.add(new TimingAdvanceCommand());
    
//        cmds.add(new VinCommand());

        // Engine
     

        // Fuel
//        cmds.add(new FindFuelTypeCommand());
//        cmds.add(new ConsumptionRateCommand());
        // cmds.add(new AverageFuelEconomyObdCommand());
        //cmds.add(new FuelEconomyObdCommand());
//        cmds.add(new AirFuelRatioCommand());
//        cmds.add(new FuelLevelCommand());
        // cmds.add(new FuelEconomyMAPObdCommand());
        // cmds.add(new FuelEconomyCommandedMAPObdCommand());
//        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1));
//        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_2));
//        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_1));
//        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_2));
//        cmds.add(new AirFuelRatioCommand());
//        cmds.add(new WidebandAirFuelRatioCommand());
//        cmds.add(new OilTempCommand());

        // Pressure
//        cmds.add(new BarometricPressureCommand());
//        cmds.add(new FuelPressureCommand());
//        cmds.add(new FuelRailPressureCommand());
//        cmds.add(new IntakeManifoldPressureCommand());
//
//        // Temperature
//        cmds.add(new AirIntakeTemperatureCommand());
//        cmds.add(new AmbientAirTemperatureCommand());
//        cmds.add(new EngineCoolantTemperatureCommand());

        // Misc
      


        return cmds;
    }

}
