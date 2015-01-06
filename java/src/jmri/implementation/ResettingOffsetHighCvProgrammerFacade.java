// ResettingOffsetHighCvProgrammerFacade.java

package jmri.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.Programmer;
import jmri.ProgListener;
import jmri.jmrix.AbstractProgrammerFacade;

/**
 * Programmer facade, at this point just an example.
 * <p>
 * This is for decoders that have an
 * alternate high-CV access method for
 * command stations that can't address all 1024.
 * It falls back to that mode if the CS can't
 * directly address an requested CV address.
 * In the fall back, 
 * CVs from 0 to "top" are addressed
 * directly. 
 * Above the top CV, the upper part of the address is written to 
 * a specific CV, followed by an operation
 * to just the lower part of the address. 
 * The upper and lower parts are calculated using a supplied modulus, e.g. 100,
 * and an indicator value that's added in.
 * Finally, the specific CV is reset to zero to end the offset operation.
 *<p>
 * For example, to write the value N to CV xyy, this will do (modulo = 100, indicator = 200):
 *<ul>
 *<li>Write 200+x*10 to CV7 where 10 is cvFactor, 200 is indicator and 7 is addrCV
 *<li>Write N to CVyy
 *<li>Write 0 to CV7
 *</ul>
 *<p>
 * This method is used by some Zimo decoders
 *
 * @see jmri.implementation.ProgrammerFacadeSelector
 *
 * @author      Bob Jacobsen  Copyright (C) 2013
 * @version	$Revision: 24246 $
 */
public class ResettingOffsetHighCvProgrammerFacade extends AbstractProgrammerFacade implements ProgListener {

    /**
     * @param top CVs above this use the indirect method
     * @param addrCV  CV to which the high part of address is to be written
     * @param cvFactor  CV to which the low part of address is to be written
     * @param modulo Modulus for determining high/low address parts
     */
    public ResettingOffsetHighCvProgrammerFacade(Programmer prog, String top, String addrCV, String cvFactor, String modulo, String indicator) {
        super(prog);
        this.top = Integer.parseInt(top);
        this.addrCV = Integer.parseInt(addrCV);
        this.cvFactor = Integer.parseInt(cvFactor);
        this.modulo = Integer.parseInt(modulo);
        this.indicator = Integer.parseInt(indicator);
    }

    int top;
    int addrCV;
    int cvFactor;
    int modulo;
    int indicator;


    // members for handling the programmer interface

    int _val;	// remember the value being read/written for confirmative reply
    int _cv;	// remember the cv being read/written

    // programming interface
    @Override
    public void writeCV(int CV, int val, jmri.ProgListener p) throws jmri.ProgrammerException {
        writeCV(""+CV, val, p);
    }
    @Override
    public void writeCV(String CV, int val, jmri.ProgListener p) throws jmri.ProgrammerException {
        log.debug("start writeCV");
        _cv = Integer.parseInt(CV);
        _val = val;
        useProgrammer(p);
        if (prog.getCanRead(CV) || _cv <= top) {
            state = ProgState.PROGRAMMING;
            prog.writeCV(_cv, val, this);
        } else {
            // write index first
            state = ProgState.FINISHWRITE;
            prog.writeCV(addrCV, (_cv/modulo)*cvFactor+indicator, this);
        }
    }

    @Override
    public void confirmCV(int CV, int val, jmri.ProgListener p) throws jmri.ProgrammerException {
        confirmCV(""+CV, val, p);
    }
    @Override
    public void confirmCV(String CV, int val, jmri.ProgListener p) throws jmri.ProgrammerException {
        readCV(CV, p);
    }

    @Override
    public void readCV(int CV, jmri.ProgListener p) throws jmri.ProgrammerException {
        readCV(""+CV, p);
    }
    @Override
    public void readCV(String CV, jmri.ProgListener p) throws jmri.ProgrammerException {
        log.debug("start readCV");
        _cv = Integer.parseInt(CV);
        useProgrammer(p);
        if (prog.getCanWrite(CV) || _cv <= top) {
            state = ProgState.PROGRAMMING;
            prog.readCV(_cv, this);
        } else {
            // write index first
            state = ProgState.FINISHREAD;
            prog.writeCV(addrCV, (_cv/modulo)*cvFactor+indicator, this);
        }
    }

    private jmri.ProgListener _usingProgrammer = null;

    // internal method to remember who's using the programmer
    protected void useProgrammer(jmri.ProgListener p) throws jmri.ProgrammerException {
        // test for only one!
        if (_usingProgrammer != null && _usingProgrammer != p) {
            if (log.isInfoEnabled()) log.info("programmer already in use by "+_usingProgrammer);
            throw new jmri.ProgrammerException("programmer in use");
        }
        else {
            _usingProgrammer = p;
            return;
        }
    }

    enum ProgState { PROGRAMMING, FINISHREAD, FINISHWRITE, RESET, NOTPROGRAMMING }
    ProgState state = ProgState.NOTPROGRAMMING;
    
    // get notified of the final result
    // Note this assumes that there's only one phase to the operation
    public void programmingOpReply(int value, int status) {
        if (log.isDebugEnabled()) log.debug("notifyProgListenerEnd value "+value+" status "+status);
        
        if (_usingProgrammer == null) log.error("No listener to notify");

        switch (state) {
            case FINISHREAD:
                try {
                    state = ProgState.RESET;
                    prog.readCV(_cv%modulo, this);
                } catch (jmri.ProgrammerException e) {
                    log.error("Exception doing final read", e);
                }
                break;
            case FINISHWRITE:
                try {
                    state = ProgState.RESET;
                    prog.writeCV(_cv%modulo, _val, this);
                } catch (jmri.ProgrammerException e) {
                    log.error("Exception doing final write", e);
                }
                break;
            case RESET:
                try {
                    state = ProgState.PROGRAMMING;
                    prog.writeCV(addrCV, 0, this);
                } catch (jmri.ProgrammerException e) {
                    log.error("Exception doing reset write", e);
                }
                break;
            case PROGRAMMING:
                // the programmingOpReply handler might send an immediate reply, so
                // clear the current listener _first_
                jmri.ProgListener temp = _usingProgrammer;
                _usingProgrammer = null; // done
                state = ProgState.NOTPROGRAMMING;
                temp.programmingOpReply(value, status);
                break;
            default:
                log.error("Unexpected state on reply: "+state);
                // clean up as much as possible
                _usingProgrammer = null;
                state = ProgState.NOTPROGRAMMING;
                
        }

    }

    // Access to full address space provided by this.
    public boolean getCanRead() { return true; }
    public boolean getCanRead(String addr) { return Integer.parseInt(addr)<=1024; }

    public boolean getCanWrite()  { return true; }
    public boolean getCanWrite(String addr) { return Integer.parseInt(addr)<=1024; }

    static Logger log = LoggerFactory.getLogger(ResettingOffsetHighCvProgrammerFacade.class.getName());

}

