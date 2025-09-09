package org.traccar.protocol;

import org.junit.jupiter.api.Test;
import org.traccar.ProtocolTest;
import org.traccar.model.Command;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SegwayeBikeProtocolEncoderTest extends ProtocolTest {

    @Test
    public void testEncodePositionPeriodic() throws Exception {

        var encoder = inject(new SegwayeBikeProtocolEncoder(null));

        Command command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_POSITION_PERIODIC);
        command.set(Command.KEY_FREQUENCY, 300);

        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,D1,300#\n", encoder.encodeCommand(null, command));

    }

    @Test
    public void testEncodeCustom() throws Exception {

        var encoder = inject(new SegwayeBikeProtocolEncoder(null));

        Command command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_CUSTOM);
        command.set(Command.KEY_DATA, "L0");

        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,L0#\n", encoder.encodeCommand(null, command));

    }

    @Test
    public void testEncodeSegwayeBikeCommands() throws Exception {

        var encoder = inject(new SegwayeBikeProtocolEncoder(null));

        // Test L0 - Unlock command
        Command unlockCommand = new Command();
        unlockCommand.setDeviceId(1);
        unlockCommand.setType(Command.TYPE_CUSTOM);
        unlockCommand.set(Command.KEY_DATA, "L0");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,L0#\n", encoder.encodeCommand(null, unlockCommand));

        // Test L1 - Lock command
        Command lockCommand = new Command();
        lockCommand.setDeviceId(1);
        lockCommand.setType(Command.TYPE_CUSTOM);
        lockCommand.set(Command.KEY_DATA, "L1");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,L1#\n", encoder.encodeCommand(null, lockCommand));

        // Test V1 - Voice config command
        Command voiceConfigCommand = new Command();
        voiceConfigCommand.setDeviceId(1);
        voiceConfigCommand.setType(Command.TYPE_CUSTOM);
        voiceConfigCommand.set(Command.KEY_DATA, "V1,1,7,7,1111111111");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,V1,1,7,7,1111111111#\n", encoder.encodeCommand(null, voiceConfigCommand));

        // Test S2 - Turn on/off control of scooter
        Command scooterControlCommand = new Command();
        scooterControlCommand.setDeviceId(1);
        scooterControlCommand.setType(Command.TYPE_CUSTOM);
        scooterControlCommand.set(Command.KEY_DATA, "S2,1");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,S2,1#\n", encoder.encodeCommand(null, scooterControlCommand));

        // Test D0 - Acquire positioning command (one-time)
        Command positionCommand = new Command();
        positionCommand.setDeviceId(1);
        positionCommand.setType(Command.TYPE_POSITION_SINGLE);
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,D0#\n", encoder.encodeCommand(null, positionCommand));

        // Test D1 - Positioning and tracking command
        Command trackingCommand = new Command();
        trackingCommand.setDeviceId(1);
        trackingCommand.setType(Command.TYPE_POSITION_PERIODIC);
        trackingCommand.set(Command.KEY_FREQUENCY, 300);
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,D1,300#\n", encoder.encodeCommand(null, trackingCommand));

        // Test I0 - Acquire SIM card ICCID number
        Command iccidCommand = new Command();
        iccidCommand.setDeviceId(1);
        iccidCommand.setType(Command.TYPE_CUSTOM);
        iccidCommand.set(Command.KEY_DATA, "I0");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,I0#\n", encoder.encodeCommand(null, iccidCommand));

        // Test J1 - Other query
        Command queryCommand = new Command();
        queryCommand.setDeviceId(1);
        queryCommand.setType(Command.TYPE_CUSTOM);
        queryCommand.set(Command.KEY_DATA, "J1");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,J1#\n", encoder.encodeCommand(null, queryCommand));

        // Test LS1 - LCD setting 1
        Command lcdCommand = new Command();
        lcdCommand.setDeviceId(1);
        lcdCommand.setType(Command.TYPE_CUSTOM);
        lcdCommand.set(Command.KEY_DATA, "LS1,1,,,,1,100,,,,,,,,,,");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,LS1,1,,,,1,100,,,,,,,,,,#\n", encoder.encodeCommand(null, lcdCommand));

        // Test P2 - Acquire the vehicle data 2 - battery data
        Command batteryDataCommand = new Command();
        batteryDataCommand.setDeviceId(1);
        batteryDataCommand.setType(Command.TYPE_CUSTOM);
        batteryDataCommand.set(Command.KEY_DATA, "P2");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,P2#\n", encoder.encodeCommand(null, batteryDataCommand));

        // Test C1 - One-time Control of Vehicle
        Command vehicleControlCommand = new Command();
        vehicleControlCommand.setDeviceId(1);
        vehicleControlCommand.setType(Command.TYPE_CUSTOM);
        vehicleControlCommand.set(Command.KEY_DATA, "C1,52,202");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,C1,52,202#\n", encoder.encodeCommand(null, vehicleControlCommand));

        // Test AI2 - Query AI Box information
        Command aiQueryCommand = new Command();
        aiQueryCommand.setDeviceId(1);
        aiQueryCommand.setType(Command.TYPE_CUSTOM);
        aiQueryCommand.set(Command.KEY_DATA, "AI2");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,AI2#\n", encoder.encodeCommand(null, aiQueryCommand));

        // Test G3 - Acquire firmware version
        Command firmwareCommand = new Command();
        firmwareCommand.setDeviceId(1);
        firmwareCommand.setType(Command.TYPE_CUSTOM);
        firmwareCommand.set(Command.KEY_DATA, "G3");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,G3#\n", encoder.encodeCommand(null, firmwareCommand));

        // Test E1 - Controller fault code clear
        Command faultClearCommand = new Command();
        faultClearCommand.setDeviceId(1);
        faultClearCommand.setType(Command.TYPE_CUSTOM);
        faultClearCommand.set(Command.KEY_DATA, "E1");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,E1#\n", encoder.encodeCommand(null, faultClearCommand));

        // Test U5 - Start upgrade
        Command upgradeStartCommand = new Command();
        upgradeStartCommand.setDeviceId(1);
        upgradeStartCommand.setType(Command.TYPE_CUSTOM);
        upgradeStartCommand.set(Command.KEY_DATA, "U5,1");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,U5,1#\n", encoder.encodeCommand(null, upgradeStartCommand));

        // Test K0 - Set/acquire BLE 8-byte communication KEY
        Command bleKeyCommand = new Command();
        bleKeyCommand.setDeviceId(1);
        bleKeyCommand.setType(Command.TYPE_CUSTOM);
        bleKeyCommand.set(Command.KEY_DATA, "K0,12345678");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,K0,12345678#\n", encoder.encodeCommand(null, bleKeyCommand));

        // Test K1 - Set/acquire secondary BLE 8-byte communication KEY
        Command bleKey2Command = new Command();
        bleKey2Command.setDeviceId(1);
        bleKey2Command.setType(Command.TYPE_CUSTOM);
        bleKey2Command.set(Command.KEY_DATA, "K1,87654321");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,K1,87654321#\n", encoder.encodeCommand(null, bleKey2Command));

        // Test S4 - Scooter setting command 2
        Command scooterSetting2Command = new Command();
        scooterSetting2Command.setDeviceId(1);
        scooterSetting2Command.setType(Command.TYPE_CUSTOM);
        scooterSetting2Command.set(Command.KEY_DATA, "S4,setting1,setting2");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,S4,setting1,setting2#\n", encoder.encodeCommand(null, scooterSetting2Command));

        // Test S5 - IoT device setting
        Command iotSettingCommand = new Command();
        iotSettingCommand.setDeviceId(1);
        iotSettingCommand.setType(Command.TYPE_CUSTOM);
        iotSettingCommand.set(Command.KEY_DATA, "S5,config1,config2");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,S5,config1,config2#\n", encoder.encodeCommand(null, iotSettingCommand));

        // Test S6 - Acquire vehicle data 1
        Command vehicleData1Command = new Command();
        vehicleData1Command.setDeviceId(1);
        vehicleData1Command.setType(Command.TYPE_CUSTOM);
        vehicleData1Command.set(Command.KEY_DATA, "S6");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,S6#\n", encoder.encodeCommand(null, vehicleData1Command));

        // Test S7 - Scooter setting command 1
        Command scooterSetting1Command = new Command();
        scooterSetting1Command.setDeviceId(1);
        scooterSetting1Command.setType(Command.TYPE_CUSTOM);
        scooterSetting1Command.set(Command.KEY_DATA, "S7,setting1,setting2");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,S7,setting1,setting2#\n", encoder.encodeCommand(null, scooterSetting1Command));

        // Test S8 - Acquire scooter data 2
        Command scooterData2Command = new Command();
        scooterData2Command.setDeviceId(1);
        scooterData2Command.setType(Command.TYPE_CUSTOM);
        scooterData2Command.set(Command.KEY_DATA, "S8");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,S8#\n", encoder.encodeCommand(null, scooterData2Command));

        // Test B1 - Acquire battery cell voltage
        Command batteryVoltageCommand = new Command();
        batteryVoltageCommand.setDeviceId(1);
        batteryVoltageCommand.setType(Command.TYPE_CUSTOM);
        batteryVoltageCommand.set(Command.KEY_DATA, "B1");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,B1#\n", encoder.encodeCommand(null, batteryVoltageCommand));

        // Test JR1 - Fetch BMS passcode
        Command bmsPasscodeCommand = new Command();
        bmsPasscodeCommand.setDeviceId(1);
        bmsPasscodeCommand.setType(Command.TYPE_CUSTOM);
        bmsPasscodeCommand.set(Command.KEY_DATA, "JR1");
        assertEquals("\u00ff\u00ff*HBCS,NB,123456789012345,JR1#\n", encoder.encodeCommand(null, bmsPasscodeCommand));

    }

}
