//IMPORTANT! Don't use this, use the Lookup List Import functionality

values = [
  "label:code"
]

lul = LookupList.get('LOOKUP_LIST')
values.each{ valstring -> 
  val = new LookupItem()
  val.code = valstring.split(':')[1]
  val.label = valstring.split(':')[0]
  val.retired = false
  val.system = false
  val.lookupList = racelul
  val.saveOrUpdate()
}
