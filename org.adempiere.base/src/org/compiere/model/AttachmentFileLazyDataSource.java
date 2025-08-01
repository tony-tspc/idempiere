/***********************************************************************
 * This file is part of iDempiere ERP Open Source                      *
 * http://www.idempiere.org                                            *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 *                                                                     *
 * Sponsor:                                                            *
 * - FH                                                                *
 * Contributors:                                                       *
 * - Carlos Ruiz                                                       *
 **********************************************************************/

package org.compiere.model;

import java.io.*;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CLogger;

/**
 *	IDEMPIERE-4889
 * 	@author Carlos Ruiz - globalqss
 */
public class AttachmentFileLazyDataSource implements IAttachmentLazyDataSource {

	private final CLogger log = CLogger.getCLogger(getClass());

	private File m_file;

	/**
	 * Constructor for lazy load - keep the file information
	 * @param file
	 */
	public AttachmentFileLazyDataSource(File file) {
		m_file = file;
	}

	/**
	 * Return a byte array containing the data from the File
	 * @return
	 */
	@Override
	public byte[] getData() {
		// read files into byte[]
        long length = m_file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IllegalStateException("File too large to load into a byte array");
        }
		final byte[] dataEntry = new byte[(int) length];
		try (FileInputStream fileInputStream = new FileInputStream(m_file)) {
			fileInputStream.read(dataEntry);
		} catch (FileNotFoundException e) {
			log.severe("File Not Found.");
			e.printStackTrace();
		} catch (IOException e1) {
			log.severe("Error Reading The File.");
			e1.printStackTrace();
		}
		return dataEntry;
	}

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(m_file);
        } catch (FileNotFoundException e) {
            throw new AdempiereException(e);
        }
    }

    @Override
    public File getFile() {
        return m_file;
    }

    @Override
    public long getSize() {
        return m_file.length();
    }

}
