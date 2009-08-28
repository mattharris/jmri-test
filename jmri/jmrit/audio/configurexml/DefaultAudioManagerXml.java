// DefaultAudioManagerXml.java

package jmri.jmrit.audio.configurexml;

import jmri.InstanceManager;
import jmri.managers.configurexml.AbstractAudioManagerConfigXML;
import org.jdom.Element;

/**
 * Persistency implementation for the default AudioManager persistence.
 * <P>
 * The state of audio objects is not persisted, just their existence.
 *
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 * <P>
 *
 * @author Matthew Harris  copyright (c) 2009
 * @version $Revision: 1.2 $
 */
public class DefaultAudioManagerXml extends AbstractAudioManagerConfigXML {

    /**
     * Default constructor
     */
    public DefaultAudioManagerXml() {
    }

    /**
     * Subclass provides implementation to create the correct top
     * element, including the type information.
     * Default implementation is to use the local class here.
     * @param audio The top-level element being created
     */
    public void setStoreElementClass(Element audio) {
        audio.setAttribute("class","jmri.jmrit.audio.configurexml.DefaultAudioManagerXml");
    }

    /**
     * Create a AudioManager object of the correct class, then
     * register and fill it.
     * @param audio Top level Element to unpack.
     * @return true if successful
     */
    public boolean load(Element audio) {
        // create the master object
        InstanceManager.audioManagerInstance();
        // load individual audio objects
        loadAudio(audio);
        return true;
    }
}

/* $(#)DefaultAudioManagerXml.java */