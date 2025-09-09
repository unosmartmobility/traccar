package org.traccar.protocol;

import org.junit.jupiter.api.Test;
import org.traccar.ProtocolTest;

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

}
