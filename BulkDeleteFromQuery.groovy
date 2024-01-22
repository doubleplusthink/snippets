//delete any ctSubCaseSpecialStatus 
//where subCase.ctSubCaseSpecialStatus.status = OCADISP 
//and ctSubCaseSpecialStatus.endDate == null 
//and where subCase.ctSubCaseSpecialStatuses.size > 1 (meaning there’s more than one OCADISP without an endDate) 
//and delete all besides the lowest ID.

import com.sustain.entities.custom.CtSubcaseSpecialStatus

sql = """
SELECT scss.subCase_id
FROM ecourt.tCtSubcaseSpecialStatus scss
WHERE 
	scss.status = 'OCADISP'
	and scss.endDate is NULL
GROUP BY scss.subCase_id
HAVING COUNT(scss.subCase_id) > 1;
"""

scids = DomainObject.querySQL(sql)
logger.debug( scids.size())

scids.each{scid ->
  subCase = SubCase.get(scid.toLong())
  _csvText += "${subCase.subCaseNumber}\n"
  if(_proceedWithDeletion){
    statuses = subCase.ctSubcaseSpecialStatuses.findAll{it.status == 'OCADISP' && it.endDate == null}.id.sort{-it}
    statusesToDelete = statuses[0..-2]
    statusesToDelete.each{ sid -> 
      stat = CtSubcaseSpecialStatus.get(CtSubcaseSpecialStatus.class, sid)
      withTx{
        stat.deleteRemoveFromPeers()
      }
      DomainObject.clearSession()
    }
  }
}

csv = File.createTempFile('DeleteOCADISPSubCaseStatuses', '.csv')
csv.write(_csvText)
new Email().to('igarrity@journaltech.com').from('noreply@journaltech.com').subject('DeleteOCADISPSubCaseStatuses').attachment(csv).send()



