package com.gae.yotube;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.io.ByteArrayOutputStream;

/**
 * @author uudashr
 *
 */
public class DatastoreImage extends Image {
    private static final long serialVersionUID = -6103333163734189303L;

    private final IModel<String> model;

    /**
     * @param id
     * @param urlModel
     */
    public DatastoreImage(String id, IModel<String> model) {
        super(id);
        this.model = model;
    }
    
    public DatastoreImage(String id, String fileName) {
        this(id, new Model<String>(fileName));
    }

    /**
     * @see org.apache.wicket.markup.html.image.Image#getImageResource()
     */
    @Override
    protected Resource getImageResource() {
        return new DynamicImageResource() {
            private static final long serialVersionUID = -2812349562312499405L;

            @Override
            protected byte[] getImageData() {
                InputStream in = null;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    String fileName = (String)model.getObject();
                    in = new DatastoreInputStream(fileName);
                    int b = 0;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }
                    byte[] data = out.toByteArray();
                    return data;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try { in.close(); } catch (Exception e) {}
                    try { out.close(); } catch (Exception e) {}
                }
                return new byte[] {};
            }

            @Override
            protected void setHeaders(WebResponse response) {
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
            }
        };
    }
}