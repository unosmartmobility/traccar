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
import org.traccar.BaseProtocolEncoder;
import org.traccar.model.Command;
import org.traccar.Protocol;

public class SegwayeBikeProtocolEncoder extends BaseProtocolEncoder {

    public SegwayeBikeProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private String formatCommand(Command command, String content) {
        return String.format("\u00ff\u00ff*HBCS,NB,%s,%s#\n", getUniqueId(command.getDeviceId()), content);
    }

    @Override
    protected Object encodeCommand(Channel channel, Command command) {

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(command, command.getString(Command.KEY_DATA));
            case Command.TYPE_POSITION_SINGLE -> formatCommand(command, "D0");
            case Command.TYPE_POSITION_PERIODIC ->
                    formatCommand(command, "D1," + command.getInteger(Command.KEY_FREQUENCY));
            case Command.TYPE_ENGINE_STOP, Command.TYPE_ALARM_DISARM -> {
                // Extract parameters with defaults and validation
                int validTime = 20;
                if (command.getInteger("keyValidTime") != 0) {
                    validTime = command.getInteger("keyValidTime");
                }
                String userId = command.getString("userId");
                if (userId == null) {
                    userId = "1234";
                }
                long timestamp = System.currentTimeMillis() / 1000;
                if (command.getLong("timestamp") != 0L) {
                    timestamp = command.getLong("timestamp");
                }

                // Validate parameter ranges per specification
                if (validTime < 0 || validTime > 65535) {
                    validTime = 20;
                }
                if (userId.length() > 15) {
                    userId = userId.substring(0, 15);
                }
                if (timestamp < 0 || timestamp > 4294967295L) {
                    timestamp = System.currentTimeMillis() / 1000;
                }

                if (channel != null) {
                    SegwayeBikeProtocolDecoder decoder = channel.pipeline().get(SegwayeBikeProtocolDecoder.class);
                    if (decoder != null) {
                        decoder.setPendingCommand(command.getType());
                    }
                }
                yield formatCommand(command, String.format("R0,0,%d,%s,%d", validTime, userId, timestamp));
            }
            case Command.TYPE_ALARM_ARM -> {
                // Extract parameters with defaults and validation
                int validTime = 20;
                if (command.getInteger("keyValidTime") != 0) {
                    validTime = command.getInteger("keyValidTime");
                }
                String userId = command.getString("userId", "1234");

                long timestamp = System.currentTimeMillis() / 1000;
                if (command.getLong("timestamp") != 0L) {
                    timestamp = command.getLong("timestamp");
                }

                // Validate parameter ranges per specification
                if (validTime < 0 || validTime > 65535) {
                    validTime = 20;
                }
                if (userId.length() > 15) {
                    userId = userId.substring(0, 15);
                }
                if (timestamp < 0 || timestamp > 4294967295L) {
                    timestamp = System.currentTimeMillis() / 1000;
                }

                if (channel != null) {
                    SegwayeBikeProtocolDecoder decoder = channel.pipeline().get(SegwayeBikeProtocolDecoder.class);
                    if (decoder != null) {
                        decoder.setPendingCommand(command.getType());
                    }
                }
                yield formatCommand(command, String.format("R0,1,%d,%s,%d", validTime, userId, timestamp));
            }
            default -> null;
        };
    }

}
