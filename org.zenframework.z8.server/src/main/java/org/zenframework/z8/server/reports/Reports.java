package org.zenframework.z8.server.reports;

public class Reports {
    public static final String DesignExtension = "rptdesign";
    
    public static final String Bindings = "bindings.xml";
    public static final String DefaultDesign = "default.rptdesign";

    // for Layout:
    public static String FIRSTPAGE_CAPTIONCENTER = "CaptionCenter";
    public static String REPORT_BODY = "ReportBody";
    // for MasterPage: 
    public static String EACHPAGE_PAGE_NUMBER = "PageNumber";
    public static String EACHPAGE_TIMESTAMP = "DateTimeStamp";
    public static String EACHPAGE_REPORTNAME = "ReportName";

    // others
    public static final String Pdf = "pdf";
    public static final String Excel = "xls";
    public static final String Word = "doc";
    public static final String Html = "html";
    public static final String Powerpoint = "ppt";

    public static final int DefaultGroupIndentation = 20;
    public static final int DefaultPageOverlapping = 10;
    public static final int MinimalFontSize = 6;

    public static final String GroupTotalText = "Report.groupTotal";
    public static final String GroupGrandTotalText = "Report.groupGrandTotal";
}
