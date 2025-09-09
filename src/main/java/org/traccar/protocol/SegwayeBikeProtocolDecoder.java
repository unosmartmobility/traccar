/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.protocol;

import io.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.session.DeviceSession;
import org.traccar.NetworkMessage;
import org.traccar.Protocol;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.model.Command;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.regex.Pattern;

public class SegwayeBikeProtocolDecoder extends BaseProtocolDecoder {

    private String pendingCommand;

    public void setPendingCommand(String pendingCommand) {
        this.pendingCommand = pendingCommand;
    }

    public SegwayeBikeProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("*")
            .expression("....,")
            .expression("..,")                   // vendor
            .number("d{15},")                    // imei
            .number("d{12},").optional()         // time
            .expression("..,")                   // type
            .number("[01],").optional()          // reserved (optional for Segway eBike)
            .number("(dd)(dd)(dd).d+,")          // time (hhmmss)
            .expression("([AV]),")               // validity
            .number("(dd)(dd.d+),")              // latitude
            .expression("([NS]),")
            .number("(d{2,3})(dd.d+),")          // longitude
            .expression("([EW]),")
            .number("(d+),")                     // satellites
            .number("(d+.d+),")                  // hdop
            .number("(dd)(dd)(dd),")             // date (ddmmyy)
            .number("(-?d+.?d*),")               // altitude
            .expression(".,")                    // height unit
            .expression(".#")                    // mode
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;
        String[] values = sentence.replaceAll("#$", "").split(",");

        int index = 0;
        String header = values[index++];
        String vendor = values[index++];

        // Check if this is a Segway eBike protocol format
        if (!header.equals("*HBCR") || !vendor.equals("NB")) {
            return null;
        }

        String imei = values[index++];
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);
        if (deviceSession == null) {
            return null;
        }

        String time;
        if (values[index].length() == 12) {
            time = values[index++];
        } else {
            time = null;
        }

        String type = values[index++];
        if (channel != null) {
            StringBuilder response = new StringBuilder("\u00ff\u00ff");
            response.append("*HBCS,");
            response.append(vendor).append(',');
            response.append(imei).append(',');
            if (time != null) {
                response.append(time).append(',');
            }
            if (type.matches("L0|L1|W0|E1")) {
                response.append(type).append("#\n");
                channel.write(new NetworkMessage(response.toString(), remoteAddress));
            } else if (type.equals("R0") && pendingCommand != null) {
                String command = pendingCommand.equals(Command.TYPE_ALARM_ARM) ? "L1" : "L0";
                response.append(command);
                String[] remaining = Arrays.copyOfRange(values, index, values.length);
                response.append(String.join(",", remaining));
                response.append("#\n");
                channel.write(new NetworkMessage(response.toString(), remoteAddress));
                pendingCommand = null;
            }
        }

        if (!type.startsWith("D") || type.equals("D1")) {

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            getLastLocation(position, null);

            switch (type) {
                case "Q0" -> {
                    position.set(Position.KEY_BATTERY, Integer.parseInt(values[index++]) * 0.01);
                    position.set(Position.KEY_BATTERY_LEVEL, Integer.parseInt(values[index++]));
                    position.set(Position.KEY_RSSI, Integer.parseInt(values[index++]));
                    // Handle additional Segway eBike Q0 fields if present
                    if (index < values.length) {
                        position.set("network", values[index++]);
                    }
                    if (index < values.length) {
                        position.set("operator", values[index++]);
                    }
                }
                case "H0" -> {
                    position.set(Position.KEY_BLOCKED, Integer.parseInt(values[index++]) > 0);
                    position.set(Position.KEY_BATTERY, Integer.parseInt(values[index++]) * 0.01);
                    position.set(Position.KEY_RSSI, Integer.parseInt(values[index++]));
                    position.set(Position.KEY_BATTERY_LEVEL, Integer.parseInt(values[index++]));
                }
                case "W0" -> {
                    switch (Integer.parseInt(values[index++])) {
                        case 1 -> position.addAlarm(Position.ALARM_MOVEMENT);
                        case 2 -> position.addAlarm(Position.ALARM_FALL_DOWN);
                        case 3 -> position.addAlarm(Position.ALARM_LOW_BATTERY);
                    }
                }
                case "W1" -> {
                    position.set("alarmDismissed", Integer.parseInt(values[index++]));
                }
                case "E0" -> {
                    position.addAlarm(Position.ALARM_FAULT);
                    position.set("error", Integer.parseInt(values[index++]));
                }
                case "E1" -> {
                    position.set("faultCleared", true);
                }
                case "S1" -> position.set(Position.KEY_EVENT, Integer.parseInt(values[index++]));
                case "S2" -> {
                    position.set("scooterControl", Integer.parseInt(values[index++]));
                }
                case "V0" -> {
                    position.set("voicePlay", Integer.parseInt(values[index++]));
                }
                case "G3" -> {
                    position.set("firmwareVersion", values[index++]);
                }
                case "U5" -> {
                    position.set("upgradeStart", Integer.parseInt(values[index++]));
                }
                case "U6" -> {
                    position.set("upgradeState", Integer.parseInt(values[index++]));
                    if (index < values.length) {
                        position.set("upgradeProgress", Integer.parseInt(values[index++]));
                    }
                }
                case "K0" -> {
                    position.set("bleKey", values[index++]);
                }
                case "K1" -> {
                    position.set("bleKey2", values[index++]);
                }
                case "B1" -> {
                    // Battery cell voltages
                    StringBuilder cellVoltages = new StringBuilder();
                    while (index < values.length) {
                        if (cellVoltages.length() > 0) {
                            cellVoltages.append(",");
                        }
                        cellVoltages.append(values[index++]);
                    }
                    position.set("batteryCells", cellVoltages.toString());
                }
                case "JR0" -> {
                    position.set("aiDetection", values[index++]);
                    if (index < values.length) {
                        position.set("aiData1", values[index++]);
                    }
                    if (index < values.length) {
                        position.set("aiData2", values[index++]);
                    }
                }
                case "JR1" -> {
                    position.set("bmsPasscode", values[index++]);
                }
                case "JR4" -> {
                    StringBuilder extendedData = new StringBuilder();
                    while (index < values.length) {
                        if (extendedData.length() > 0) {
                            extendedData.append(",");
                        }
                        extendedData.append(values[index++]);
                    }
                    position.set("extendedData", extendedData.toString());
                }
                case "P2" -> {
                    // Battery data - complex format
                    StringBuilder batteryData = new StringBuilder();
                    while (index < values.length) {
                        if (batteryData.length() > 0) {
                            batteryData.append(",");
                        }
                        batteryData.append(values[index++]);
                    }
                    position.set("batteryData", batteryData.toString());
                }
                case "C1" -> {
                    position.set("vehicleControl", values[index++]);
                    if (index < values.length) {
                        position.set("controlParam1", values[index++]);
                    }
                    if (index < values.length) {
                        position.set("controlParam2", values[index++]);
                    }
                }
                case "AI2" -> {
                    StringBuilder aiData = new StringBuilder();
                    while (index < values.length) {
                        if (aiData.length() > 0) {
                            aiData.append(",");
                        }
                        aiData.append(values[index++]);
                    }
                    position.set("aiBoxInfo", aiData.toString());
                }
                case "LS1" -> {
                    StringBuilder lcdData = new StringBuilder();
                    while (index < values.length) {
                        if (lcdData.length() > 0) {
                            lcdData.append(",");
                        }
                        lcdData.append(values[index++]);
                    }
                    position.set("lcdSettings", lcdData.toString());
                }
                case "J1" -> {
                    StringBuilder queryData = new StringBuilder();
                    while (index < values.length) {
                        if (queryData.length() > 0) {
                            queryData.append(",");
                        }
                        queryData.append(values[index++]);
                    }
                    position.set("queryData", queryData.toString());
                }
                case "I0" -> {
                    position.set("iccid", values[index++]);
                    if (index < values.length) {
                        position.set("simOperator", values[index++]);
                    }
                    if (index < values.length) {
                        position.set("simCountry", values[index++]);
                    }
                }
                case "D1" -> {
                    position.set("trackingFrequency", Integer.parseInt(values[index++]));
                }
                // Handle all other commands as generic result data
                case "R0", "L0", "L1", "S4", "S5", "S6", "S7", "S8", "V1", "G0", "M0" -> {
                    String[] remaining = Arrays.copyOfRange(values, index, values.length);
                    position.set(Position.KEY_RESULT, String.join(",", remaining));
                }
            }

            return !position.getAttributes().isEmpty() ? position : null;

        } else {

            // Handle Segway eBike D0 format which has an extra field
            if (type.equals("D0") && values.length > 15) {
                // Skip the extra field for Segway eBike D0 format
                String[] adjustedValues = new String[values.length - 1];
                System.arraycopy(values, 0, adjustedValues, 0, 4); // header, vendor, imei, time
                System.arraycopy(values, 5, adjustedValues, 4, values.length - 5); // skip the extra field
                sentence = String.join(",", adjustedValues) + "#";
            }

            Parser parser = new Parser(PATTERN, sentence);
            if (!parser.matches()) {
                return null;
            }

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            DateBuilder dateBuilder = new DateBuilder()
                    .setTime(parser.nextInt(), parser.nextInt(), parser.nextInt());

            position.setValid(parser.next().equals("A"));
            position.setLatitude(parser.nextCoordinate());
            position.setLongitude(parser.nextCoordinate());

            position.set(Position.KEY_SATELLITES, parser.nextInt());
            position.set(Position.KEY_HDOP, parser.nextDouble());

            dateBuilder.setDateReverse(parser.nextInt(), parser.nextInt(), parser.nextInt());
            position.setTime(dateBuilder.getDate());

            position.setAltitude(parser.nextDouble());

            return position;

        }
    }

}
