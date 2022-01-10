//Pro Tips 

//replace a .collect with a .findAll
case.findAll{it.subCase.type == 'mySubCaseType'}

//Proper way to do a bulk update
  //The withTx wraps each object update into its own sql transaction so that if it fails, it only fails on that object and not on the entire set.  
Where where = new Where()
where.addEquals('caseType', 'DUM')
ids = DomainObject.find(Case.class, where, sel(id))

for(id in ids){
  cse = Case.get(id)
  cse.fieldToUpdate = 'JANK'
  withTx{
    try{
      cse.saveOrUpdate()
    }catch(ex){
      logger.debug ex
    }
  }
}

