package com.software.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReportConstants {
    public static final String REPORT_NAME = "Отчет по поездкам";
    public static final String REPORT_DRIVER_ID = "ID водителя";
    public static final String REPORT_PASSENGER_ID = "ID пассажира";
    public static final String REPORT_FROM = "Откуда";
    public static final String REPORT_TO = "Куда";
    public static final String REPORT_STATUS = "Статус";
    public static final String REPORT_START_DATE = "Дата начала поездки";
    public static final String REPORT_END_DATE = "Дата окончания поездки";
    public static final String EMAIL_FROM = "taxi.provider@gmail.com";
    public static final String EMAIL_TEXT = "Прикреплен отчет по поездкам.";
    public static final String ATTACHMENT_FILENAME = "rides_report.xlsx";
    public static final String EMAIL_DATA_SOURCE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
}
