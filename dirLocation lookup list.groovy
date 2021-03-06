_dirLocation; // rule input parameter
_lookupListName; // rule input parameter
_locationType; // rule input parameter


//location = _dirLocation; // rule input parameter
list = LookupList.get(_lookupListName)

if(!_dirLocation){
  items = list.getItems()
  Where where = new Where()
  where.addIn('locationType', _locationType)
  locations = DomainObject.find(DirLocation.class, where)

  for (loc in locations){
    routeLocation(loc)
  }
}else{
  if(_dirLocation.locationType in _locationType){
    routeLocation(_dirLocation)
  }
}

DomainObject.clearCache();

// Determine what should happen to the lookup list for a location
def routeLocation(DirLocation location){
  relatedItem = LookupItem.get(list.name, location.code)
  if(!relatedItem){
    relatedItem = addItem(location)
  }
  // update the list item if the label or status do not match the location label or status. 
  if((relatedItem.label != location.locationName) || (relatedItem.isActiveNow() != location.activeNow)){
    relatedItem = updateItem(location, relatedItem)
  }
  //logger.debug relatedItem.code + ' ' + relatedItem.label
}

def addItem(DirLocation location){
  LookupItem newItem = new LookupItem()
  newItem.code = location.code
  newItem.label = location.locationName
  newItem.lookupList = list
  logger.debug newItem.lookupList
  newItem.saveOrUpdate()
  return newItem
}

def updateItem(DirLocation location, LookupItem item){
  if (item.label != location.locationName){
    item.label = location.locationName
  }
  if(item.isActiveNow() != location.activeNow){
    if (location.activeNow){
      activateItem(item)
    }else{
      deactivateItem(item)
    }
  }
  item.saveOrUpdate()
  return item
}

def deactivateItem(LookupItem item){
  item.activeTo = DateUtil.getYesterday()
  item.saveOrUpdate()
  return item
}

def activateItem(LookupItem item){
  item.activeTo = null
  item.saveOrUpdate()
  return item
}
