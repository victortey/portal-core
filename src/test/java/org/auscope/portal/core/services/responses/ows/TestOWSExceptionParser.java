package org.auscope.portal.core.services.responses.ows;

import org.auscope.portal.core.services.responses.ows.OWSException;
import org.auscope.portal.core.services.responses.ows.OWSExceptionParser;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.util.DOMUtil;
import org.junit.Test;
import org.w3c.dom.Document;

public class TestOWSExceptionParser extends PortalTestClass {

    private void check(String path) throws Exception {
        final String xmlString = org.auscope.portal.core.test.Util.loadXML(path);

        Document doc = DOMUtil.buildDomFromString(xmlString);

        //Test both the string and document versions
        OWSExceptionParser.checkForExceptionResponse(xmlString);
        OWSExceptionParser.checkForExceptionResponse(doc);
    }

    @Test(expected=OWSException.class)
    public void testThrowException1() throws Exception {
        check("src/test/resources/OWSExceptionSample1.xml");
    }

    @Test(expected=OWSException.class)
    public void testThrowException2() throws Exception {
        check("src/test/resources/GetMineError.xml");
    }

    @Test
    public void testDontThrowException() throws Exception {
        check("src/test/resources/DescribeCoverageResponse1.xml");
        check("src/test/resources/DescribeCoverageResponse2.xml");
        check("src/test/resources/GetMineralOccurrencesWithSpecifiedEndowmentCutOffGrade.xml");
        check("src/test/resources/GetMineralOccurrencesWithSpecifiedReserveMinimumOreAmount.xml");
        check("src/test/resources/GetMiningActivity-AssociatedMineDateRangeProducedMaterial.xml");
    }
}