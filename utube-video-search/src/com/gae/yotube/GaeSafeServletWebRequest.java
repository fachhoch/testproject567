package com.gae.yotube;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileUploadException;

/**
 * @author uudashr
 * 
 */
public class GaeSafeServletWebRequest extends ServletWebRequest {

    public GaeSafeServletWebRequest(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
        FileCleaner.reapFiles(2);
    }

    @Override
    public WebRequest newMultipartWebRequest(Bytes maxsize) {
        try {
            return new GaeSafeMultipartServletWebRequest(
                    getHttpServletRequest(), maxsize);
        } catch (FileUploadException e) {
            throw new WicketRuntimeException(e);
        }
    }
}
