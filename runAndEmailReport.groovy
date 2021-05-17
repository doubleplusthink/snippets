import com.sustain.reports.model.ReportConfig
import com.sustain.reports.ReportFormat
import com.sustain.mail.Attachments

File output = new File('temp.pdf')
HashMap<String, Object> data = new HashMap<String, Object>()
data.put('startDate',DateUtil.getDate(2020,0,1))
data.put('endDate',DateUtil.getDate(2020,0,31))

ReportConfig.runAndReportAsFile('RPRT_FELONYTRIAL', data, output, ReportFormat.pdf)

today = DateUtil.getToday()
listOfEmails = 'rsellers@journaltech.com'
subject = 'Court of Queen\'s Bench for Saskatchewan - J-STAR System Deposit Account -' + DateUtil.getDayOfMonth(today) + '-' + DateUtil.format(today,'MM') + '-' + DateUtil.getYear(today)
body = 'Hello XXXXX (the main deposit account holder),\n\nPlease find your attached monthly statement for your Provincial J-STAR System Deposit Account.\n\nThis email is not monitored so please do not reply. If you have questions, reach out to your nearest Court House:\n\nBattleford Local Registrar; Box 340, 291 23rd Street West, Battleford, SK S0M 0E0; 306-446-7675\n\nEstevan Local Registrar; 1016 4th Street, Estevan, SK S4A 0W5; 306-637-4527\n\nMelfort Local Registrar; Box 2530, 409 Main Street, Melfort, SK S0E 1A0; 306-752-6265\n\nMoose Jaw Local Registrar; 64 Ominica Street West, Moose Jaw, SK S6H 1W9; 306-694-3602\n\nPrince Albert Local Registrar; 1800 Central Avenue, Prince Albert, SK S6V 4W7; 306-953-3200\n\nRegina Local Registrar; 2425 Victoria Avenue, Regina, SK S4P 4W6; 306-787-5377\n\nSaskatoon Local Registrar; 520 Spadina Crescent East, Saskatoon, SK S7K 3G7; 306-933-5135\n\nSwift Current Local Registrar; 121 Lorne Street West, Swift Current, SK S9H 0J4; 306-778-8400\n\nYorkton Local Registrar; 29 Darlington Street East, Yorkton, SK S3N 0C2; 306-786-1515\n\nTake care,\n\nJ-STAR System Support'
Attachments attachedFile = new Attachments(output)
mailManager.sendMail(listOfEmails, subject, body, attachedFile)



