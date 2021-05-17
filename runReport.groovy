import com.sustain.reports.model.ReportConfig
import com.sustain.reports.ReportFormat

File output = new File('temp.pdf')
HashMap<String, Object> data = new HashMap<String, Object>()
data.put('startDate',DateUtil.getDate(2020,0,1))
data.put('endDate',DateUtil.getDate(2020,0,31))

ReportConfig.runAndReportAsFile('RPRT_FELONYTRIAL', data, output, ReportFormat.pdf)
