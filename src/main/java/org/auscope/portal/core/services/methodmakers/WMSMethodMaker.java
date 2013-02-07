package org.auscope.portal.core.services.methodmakers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * A class for generating methods that can interact with a OGC Web Map Service
 * @author Josh Vote
 *
 */
public class WMSMethodMaker extends AbstractMethodMaker {

    /**
     * Generates a WMS method for making a GetCapabilities request
     * @param wmsUrl The WMS endpoint (will have any existing query parameters preserved)
     * @return
     */
    public HttpMethodBase getCapabilitiesMethod(String wmsUrl) {
        GetMethod method = new GetMethod(wmsUrl);
        List<NameValuePair> options = new ArrayList<NameValuePair>();

        options.addAll(this.extractQueryParams(wmsUrl)); //preserve any existing query params
        options.add(new NameValuePair("service", "WMS"));
        options.add(new NameValuePair("request", "GetCapabilities"));
        options.add(new NameValuePair("version", "1.1.1"));

        method.setQueryString(options.toArray(new NameValuePair[options.size()]));

        return method;
    }


    /**
     * Generates a WMS request for downloading part of a map layer as an image
     * @param wmsUrl The WMS endpoint (will have any existing query parameters preserved)
     * @param layer The name of the layer to download
     * @param imageMimeType The format of the image to download as
     * @param srs The spatial reference system for the bounding box
     * @param westBoundLongitude The west bound longitude of the bounding box
     * @param southBoundLatitude The south bound latitude of the bounding box
     * @param eastBoundLongitude The east bound longitude of the bounding box
     * @param northBoundLatitude The north bound latitude of the bounding box
     * @param width The desired output image width in pixels
     * @param height The desired output image height in pixels
     * @param styles [Optional] What style name should be applied
     * @param styleBody [Optional] Only valid for Geoserver WMS, a style sheet definition
     * @return
     */
    public HttpMethodBase getMapMethod(String wmsUrl, String layer, String imageMimeType, String srs, double westBoundLongitude, double southBoundLatitude, double eastBoundLongitude, double northBoundLatitude, int width, int height, String styles, String styleBody) {
        GetMethod method = new GetMethod(wmsUrl);
        List<NameValuePair> options = new ArrayList<NameValuePair>();

        options.addAll(this.extractQueryParams(wmsUrl)); //preserve any existing query params
        options.add(new NameValuePair("service", "WMS"));
        options.add(new NameValuePair("request", "GetMap"));
        options.add(new NameValuePair("version", "1.1.1"));
        options.add(new NameValuePair("format", imageMimeType));
        options.add(new NameValuePair("transparent", "TRUE"));
        options.add(new NameValuePair("layers", layer));
        if (styles != null) {
            options.add(new NameValuePair("styles", styles));
        }
        //This is a geoserver specific URL param
        if (styleBody != null) {
            options.add(new NameValuePair("sld_body", styleBody));
        }
        options.add(new NameValuePair("srs", srs));
        options.add(new NameValuePair("bbox", String.format("%1$s,%2$s,%3$s,%4$s",
                westBoundLongitude,southBoundLatitude, eastBoundLongitude, northBoundLatitude)));

        options.add(new NameValuePair("width", Integer.toString(width)));
        options.add(new NameValuePair("height", Integer.toString(height)));

        method.setQueryString(options.toArray(new NameValuePair[options.size()]));

        return method;
    }

    /**
     * Returns a method for requesting a legend/key image for a particular layer
     * @param wmsUrl The WMS endpoint (will have any existing query parameters preserved)
     * @param layerName The WMS layer name
     * @param width Desired output width in pixels
     * @param height Desired output height in pixels
     * @param styles What style name should be applied
     * @return
     */
    public HttpMethodBase getLegendGraphic(String wmsUrl, String layerName, int width, int height, String styles) {
        GetMethod method = new GetMethod(wmsUrl);
        List<NameValuePair> options = new ArrayList<NameValuePair>();

        options.addAll(this.extractQueryParams(wmsUrl)); //preserve any existing query params
        options.add(new NameValuePair("service", "WMS"));
        options.add(new NameValuePair("request", "GetLegendGraphic"));
        options.add(new NameValuePair("version", "1.1.1"));
        options.add(new NameValuePair("format", "image/png"));
        options.add(new NameValuePair("layers", layerName));
        options.add(new NameValuePair("layer", layerName));
        if (styles != null && styles.trim().length() > 0) {
            options.add(new NameValuePair("styles", styles.trim()));
        }
        if (width > 0) {
            options.add(new NameValuePair("width", Integer.toString(width)));
        }
        if (height > 0) {
            options.add(new NameValuePair("height", Integer.toString(height)));
        }


        method.setQueryString(options.toArray(new NameValuePair[options.size()]));


        return method;
    }

    /**
     * Generates a WMS request for downloading information about a user click on a particular
     * GetMap request.
     * @param wmsUrl The WMS endpoint (will have any existing query parameters preserved)
     * @param format The desired mime type of the response
     * @param layer The name of the layer to download
     * @param srs The spatial reference system for the bounding box
     * @param westBoundLongitude The west bound longitude of the bounding box
     * @param southBoundLatitude The south bound latitude of the bounding box
     * @param eastBoundLongitude The east bound longitude of the bounding box
     * @param northBoundLatitude The north bound latitude of the bounding box
     * @param width The desired output image width in pixels
     * @param height The desired output image height in pixels
     * @param styles [Optional] What style should be included
     * @param pointLng Where the user clicked (longitude)
     * @param pointLat Where the user clicked (latitude)
     * @param pointX Where the user clicked in pixel coordinates relative to the GetMap that was used (X direction)
     * @param pointY Where the user clicked in pixel coordinates relative to the GetMap that was used (Y direction)
     * @return
     */
    public HttpMethodBase getFeatureInfo(String wmsUrl, String format, String layer, String srs, double westBoundLongitude, double southBoundLatitude, double eastBoundLongitude, double northBoundLatitude, int width, int height, double pointLng, double pointLat, int pointX, int pointY, String styles,String sld) {
        GetMethod method = new GetMethod(wmsUrl);
        List<NameValuePair> options = new ArrayList<NameValuePair>();

        String bboxString = String.format("%1$s,%2$s,%3$s,%4$s",
                westBoundLongitude,
                southBoundLatitude,
                eastBoundLongitude,
                northBoundLatitude);

        options.addAll(this.extractQueryParams(wmsUrl)); //preserve any existing query params
        options.add(new NameValuePair("service", "WMS"));
        options.add(new NameValuePair("request", "GetFeatureInfo"));
        options.add(new NameValuePair("version", "1.1.1"));
        options.add(new NameValuePair("layers", layer));
        options.add(new NameValuePair("layer", layer));
        options.add(new NameValuePair("BBOX", bboxString));
        options.add(new NameValuePair("QUERY_LAYERS", layer));
        options.add(new NameValuePair("INFO_FORMAT", format));
        options.add(new NameValuePair("lng", Double.toString(pointLng)));
        options.add(new NameValuePair("lat", Double.toString(pointLat)));
        options.add(new NameValuePair("x", Integer.toString(pointX)));
        options.add(new NameValuePair("y", Integer.toString(pointY)));
        options.add(new NameValuePair("width", Integer.toString(width)));
        options.add(new NameValuePair("height", Integer.toString(height)));
        options.add(new NameValuePair("SRS", srs));
        if(sld != null && sld.trim().length() > 0){
            options.add(new NameValuePair("SLD", sld));
        }
        if (styles != null && styles.trim().length() > 0) {
            options.add(new NameValuePair("styles", styles.trim()));
        }

        method.setQueryString(options.toArray(new NameValuePair[options.size()]));

        return method;
    }
}
