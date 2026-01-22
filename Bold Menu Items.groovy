Where where = new Where()	
where.addEquals('type', 'LEFT')

menus = DomainObject.find( Menu.class, where, sel('id'))
menus.each{ id ->
  m = Menu.get(id)
  if(!m.name.contains('<b>')){
    m.name = "<b>${m.name}</b>"
    m.saveOrUpdate()
  }
}
