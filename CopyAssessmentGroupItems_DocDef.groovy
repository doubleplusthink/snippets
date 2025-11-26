numbers = ['DASR','DCOM','DCFD','DICI','DCII','DCOJ','DDJT','DJEP','DIMA','DIAM','DIFC','DINC','DIPA','DPSR','DAEA','DPEC','DASR1','DASR3','DASR4','DAPP1','DCOM1','DCOM2','DCOM4','DULDC','DPAG1','DIGT1','DPPW1','DPPW2','DPPW3']

Where where = new Where()
where.addIn('number', numbers)

groups = DomainObject.find( DocDef.class, where, sel('docDefFees.fee')).toSet()
logger.debug groups

itemToCopy = AssessmentGroupItem.get(AssessmentGroupItem.class, 123L)

groups.each{ group ->
  agi = itemToCopy.copy()
  agi.assessmentGroup = group
  agi.saveOrUpdate()
}

//Hibernate Cache Clear Needed
