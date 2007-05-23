package edu.mit.simile.painter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author dfhuynh
 *
 */
public class PainterServlet extends HttpServlet {
	private static final long serialVersionUID = 6516116877723699755L;
	final static private Logger s_logger = Logger.getLogger(PainterServlet.class);
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.YEAR, 1);
    	
        response.setContentType("image/png");
        response.setDateHeader("Expires", c.getTimeInMillis());
        try {
        	BufferedImage image = null;
        	
        	Map<String, String[]> parameters = request.getParameterMap();
        	String renderer = PainterUtilities.getString(parameters, "renderer", "map-marker");
        	if ("map-marker".equalsIgnoreCase(renderer)) {
        		image = new MapMarkerRenderer().render(parameters);
        	} else if ("map-marker-shadow".equalsIgnoreCase(renderer)) {
            	image = new MapMarkerShadowRenderer().render(parameters);
        	}
        	
        	if (image != null) {
	            try {
	                //new PNGImageEncoder(os, new PNGEncodeParam.Palette()).encode(image);
	            	ImageIO.write(image, "png", response.getOutputStream());
	            } finally {
	                image.flush();
	            }
        	}
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            s_logger.error(e);
            //e.printStackTrace();
        }
    }
}
