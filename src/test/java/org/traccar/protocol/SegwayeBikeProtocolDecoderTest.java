package org.traccar.protocol;

import org.junit.jupiter.api.Test;
import org.traccar.ProtocolTest;
import org.traccar.model.Position;

public class SegwayeBikeProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        var decoder = inject(new SegwayeBikeProtocolDecoder(null));

        // Segway eBike Protocol Tests - Device to Server Commands
        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,Q0,412,80,28,46000,V127P3#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,H0,0,412,28,80,0#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,R0,0,55,1234,1497689816#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,V0,1#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,S2,1#"));

        verifyPosition(decoder, text(
                "*HBCR,NB,123456789123456,D0,111,124458.00,A,2237.7514,N,11408.6214,E,6,0.21,151216,10,M,A#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,I0,123456789AB123456789,super,89860#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,J1,1100,0,0,0,,,15&55,12,#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,LS1,1,,,,1,100,,,,,,,,,,#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,P2,0,36,3,90,36,36,90,1,30,36,90,1,30,36,90,1,30,90,90,90,800#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,C1,51,202,0#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,AI2,2,1,1,,,,,0&0&0&0,123456789AB#"));

        // Additional Segway eBike commands
        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,W0,1#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,W1,1#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,E0,123#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,E1#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,G3,1.2.3#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,U5,1#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,U6,1,50#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,K0,12345678#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,K1,87654321#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,JR4,data1,data2,data3#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,B1,3.7,3.8,3.9,4.0#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,JR0,1,2,3#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,JR1,passcode123#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,S4,setting1,setting2#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,S5,config1,config2#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,S6,data1,data2#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,S7,setting1,setting2#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,S8,data1,data2#"));

        verifyAttributes(decoder, text(
                "*HBCR,NB,123456789123456,D1,300#"));

    }

    @Test
    public void testLockUnlockResponses() throws Exception {

        var decoder = inject(new SegwayeBikeProtocolDecoder(null));

        // Test all L0 (unlock) return states from LockNUnlock specification
        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L0,0,1234,1497689816#"), // Success
                Position.KEY_RESULT, "0,1234,1497689816");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L0,1,user123,1497689820#"), // Failure
                Position.KEY_RESULT, "1,user123,1497689820");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L0,2,admin,1497689825#"), // KEY error or invalid
                Position.KEY_RESULT, "2,admin,1497689825");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L0,4,test,1497689830#"), // ECU failed
                Position.KEY_RESULT, "4,test,1497689830");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L0,11,bike1,1497689835#"), // Unlock Failure (eBike only)
                Position.KEY_RESULT, "11,bike1,1497689835");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L0,12,bike2,1497689840#"), // Unlock Failure and roll back failure (eBike only)
                Position.KEY_RESULT, "12,bike2,1497689840");

        // Test all L1 (lock) return states from LockNUnlock specification
        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,0,1234,1497689816,15#"), // Success with riding time
                Position.KEY_RESULT, "0,1234,1497689816,15");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,1,user123,1497689820,22#"), // Failure
                Position.KEY_RESULT, "1,user123,1497689820,22");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,2,admin,1497689825,0#"), // KEY error or invalid
                Position.KEY_RESULT, "2,admin,1497689825,0");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,3,speed1,1497689830,5#"), // Cannot lock as scooter's speed is not 0
                Position.KEY_RESULT, "3,speed1,1497689830,5");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,4,ecu1,1497689835,0#"), // ECU failed
                Position.KEY_RESULT, "4,ecu1,1497689835,0");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,5,strategy1,1497689840,10#"), // Strategy declines to lock
                Position.KEY_RESULT, "5,strategy1,1497689840,10");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,6,gps1,1497689845,0#"), // Lock fail because GPS no signal
                Position.KEY_RESULT, "6,gps1,1497689845,0");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,11,bike3,1497689850,8#"), // Lock Failure (eBike only)
                Position.KEY_RESULT, "11,bike3,1497689850,8");

        // Test R0 responses (from device after server sends R0 command)
        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,R0,0,55,1234,1497689816#"), // Unlock R0 response with operation key 55
                Position.KEY_RESULT, "0,55,1234,1497689816");

        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,R0,1,77,admin,1497689900#"), // Lock R0 response with operation key 77
                Position.KEY_RESULT, "1,77,admin,1497689900");
    }

    @Test
    public void testBluetoothNfcOperations() throws Exception {
        
        var decoder = inject(new SegwayeBikeProtocolDecoder(null));

        // Test Bluetooth/NFC unlock (operation key and user ID filled with 0)
        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L0,0,0,0#"), // Bluetooth/NFC unlock
                Position.KEY_RESULT, "0,0,0");

        // Test Bluetooth/NFC lock (operation key and user ID filled with 0, with riding time)
        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,0,0,0,18#"), // Bluetooth/NFC lock with 18 min riding time
                Position.KEY_RESULT, "0,0,0,18");
    }

    @Test
    public void testExamplesFromLockUnlockSpec() throws Exception {
        
        var decoder = inject(new SegwayeBikeProtocolDecoder(null));

        // Examples directly from the LockNUnlock specification document
        
        // R0 request example from spec: *HBCS,NB,123456789123456,R0,0,20,1234,1497689816#<LF>
        // R0 response example from spec: *HBCR,NB,123456789123456,R0,0,55,1234,1497689816#<LF>
        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,R0,0,55,1234,1497689816#"), // Spec example R0 response
                Position.KEY_RESULT, "0,55,1234,1497689816");

        // L0 request example from spec: *HBCS,NB,123456789123456,L0,55,1234,1497689816#<LF>
        // L0 response example from spec: *HBCR,NB,123456789123456,L0,0,1234,1497689816#<LF>
        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L0,0,1234,1497689816#"), // Spec example L0 response
                Position.KEY_RESULT, "0,1234,1497689816");

        // L1 request example from spec: *HBCS,NB,123456789123456,L1,55#<LF>
        // L1 response example from spec: *HBCR,NB,123456789123456,L1,0,1234,1497689816,3#<LF>
        verifyAttribute(decoder, text(
                "*HBCR,NB,123456789123456,L1,0,1234,1497689816,3#"), // Spec example L1 response
                Position.KEY_RESULT, "0,1234,1497689816,3");
    }

}
