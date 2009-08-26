/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jmri.jmrix.can.cbus;

import java.util.*;
import jmri.jmrix.AbstractMessage;

/**
 * CbusOpCodes.java
 *
 * Description:		methods to decode CBUS opcodes
 *
 * @author		Andrew Crosland   Copyright (C) 2009
 * @version $Revision: 1.1 $
 */
public class CbusOpCodes {
    /**
     * Return a string representation of a decoded CBUS Message
     *
     * @param msg CbusMessage to be decoded
     * Return String decoded message
     */
    public static String decode(AbstractMessage msg) {
        String str = "";
        int bytes;
        int value;

        // look for the opcode
        String format = opcodeMap.get(msg.getElement(0));
        if (format == null) return "Reserved opcode";

        // split the format string at each comma
        String [] fields = format.split(",");
        
        int idx = 1;
        for (int i = 0; i < fields.length; i++){
            if (fields[i].startsWith("%")) {
                // replace with bytes from the message
                value = 0;
                bytes = Integer.parseInt(fields[i].substring(1, 2));
                for ( ; bytes > 0; bytes--) {
                    value = value*256 + msg.getElement(idx++);
                }
                fields[i] = String.valueOf(value);
            }
            // concatenat to the result
            str = str + fields[i];
        }
        return str;
    }

    public static final Map<Integer, String> opcodeMap = createMap();

    /*
     * Populate hashmap with format strings keyed by opcode
     *
     * The format string is used to decode and display the CBUS message. At the
     * moment only very simple %x formats are supported where x is a single
     * digit specifying the number of bytes form the message to be displayed.
     * The format string must be separated into fragments to be displayed and
     * format specifiers with comma characters.
     */
    private static Map<Integer, String> createMap() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        // Opcodes with no data
        result.put(0x02,"Bus Halt (HLT)");
        result.put(0x03,"Bus ON (BON)");

        result.put(0x12,"CV No Acknowledge (CVNAK)");
        result.put(0x13,"CV Write Acknowledge (CVACK)");

        // Opcodes with 1 data
        result.put(0x10,"Read Node Parameters (RDPAR)");

        // Opcodes with 2 data
        result.put(0x42,"Set Node Number (SNN) NN:,%n");
        result.put(0x50,"NN Acknowledge (NNACK) NN:,%2");
        result.put(0x51,"NN Release (NNREL) NN:,%2");
        result.put(0x52,"Keep Alive (NNREF) NN:,%2");
        result.put(0x53,"Enter Learn Mode (NNLRN) NN:,%2");
        result.put(0x54,"Exit Learn Mode (NNULN) NN:,%2");
        result.put(0x55,"Clear All Events (NNCLR) NN:,%2");
        result.put(0x56,"Request Event Space (NNEVN) NN:,%2");

        result.put(0x61,"Read CV (QCVS) CV:,%2, Mode:,%1");
        result.put(0x62,"Report CV (PCVS) CV:,%2, Data:,%1");

        // Opcodes with 3 data
        result.put(0x70,"Event Space Reply (ENNLF) NN:,%2, Space:,%1");
        result.put(0x71,"Request Node Variable (NVRD) NN:,%2, NV:,%1");

        result.put(0x81,"Write CV (WCVS) CV:,%2, Mode:,%1, Data:,%1");

        // Opcodes with 4 data
        result.put(0x90,"Accessory ON (ACON) NN:,%2, EV:,%2");
        result.put(0x91,"Accessory OFF (ACOF) NN:,%2, EV:,%2");

        result.put(0x94,"Read Event (EVRD) NN:,%2 EV:,%2");
        result.put(0x95,"Unlearn Event (EVULN) NN:,%2 EV:,%2");
        result.put(0x96,"Set Node Variable (NVSET) NN:,%2 NV:,%1, VAL:,%1");
        result.put(0x97,"Returned Node Variable (NVANS) NN:,%2 NV:,%1 VAL:,%1");

        // Opcodes with 5 data

        // Opcodes with 6 data
        result.put(0xD2,"Teach Event (EVLRN) NN:,%2, EV:,%2, EV1:,%1, EV2:,%1");
        result.put(0xD3,"Returned Event (EVANS) NN:,%2, EV:,%2, EV1:,%1, EV2:,%1");

        // Opcodes with 7 data

        return Collections.unmodifiableMap(result);
    }

}

/* @(#)CbusOpCodes.java */
