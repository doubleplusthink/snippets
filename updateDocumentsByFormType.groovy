doc = _document; // rule input parameter
List<SubCase> sclist = doc.findByXRef(SubCase.class, "REL")
HashSet<Document> outputList = []
documents = doc.case.collect("documents[docDef.formType=='"+_formType+"']")
for (doc in documents){
  List<SubCase> subcases = doc.findByXRef(SubCase.class, "REL")
  subcases.each{
    if (it in sclist){
      outputList.add(doc)
    }
  }
}
outputList.each{
  it.status = _status
  it.saveOrUpdate()
}
