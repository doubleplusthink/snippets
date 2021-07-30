//logger.debug TimeSlotUtil.getSlot(162L, DateUtil.addDays(new Date(), 7))

Where where = new Where()
where.addGreaterThanOrNull('tsTemplate.endDate', new Date())
where.addEquals('tsTemplate.dirLocation.parents', DirLocation.get(542L)) //542 is the ID for the Zoom dirLocation

//Get all Zoom Timeslot definitions
Map tsDefinitions = new HashMap()
DomainObject.find(TSDefinition.class, where).each{
  bitValue = Integer.toBinaryString(it.days)
  StringBuilder builder = new StringBuilder()
  builder.append(bitValue)
  builder.reverse()
  bitValue = builder.toString()
  while (bitValue.size() < 8){
    bitValue += '0'
  }
  tsDefinitions.put(it, bitValue)
}
def TSSlot slot
requestedDate = DateUtil.startOfDay(DateUtil.addDays(new Date(), 30))

while (!slot){
  while (!DateUtil.isBusinessDay(requestedDate) || DateUtil.formatDayOfWeekLong(requestedDate) == 'Friday'){ //Don't even try to schedule on weekends, holidays, or Fridays (There are no friday timeslots as of now) 
    requestedDate = DateUtil.addDays(requestedDate, 1)
  }
  dow = DateUtil.getDayOfWeek(requestedDate)
  //build a list of timeslot definitions that are valid for our day. 
  List tryDefinitions = new ArrayList<TSDefinition>()
  tsDefinitions.each{
    if(it.value[dow-1] == '1'){
      tryDefinitions.add(it.key)
    }
  }
  //Sort our definitions by time, then by room name so as to fill room 1 before room 2 etc.
  tryDefinitions.sort(new TSDefinitionComparator())
  iter = tryDefinitions.iterator()
  while(iter.hasNext() && !slot){
    trySlot = TimeSlotUtil.getSlot(iter.next().id, requestedDate)
    if(!trySlot.isFull()){
      slot = trySlot
    }
  }
  //If we still haven't found an open slot, try the next day
  if(!slot){
    requestedDate = DateUtil.addDays(requestedDate, 1)
  }
}
logger.debug slot
// Create a hearing for the case and "Contested" SubCase
ScheduledEvent hearing = new ScheduledEvent()
hearing.case = _case
hearing.subCase = _case.collect('subCases[subCaseType == "CON"]')[0]
hearing.subType = 'VID'
hearing.type = 'HOM'
hearing.eventStatus = 'EVENT'
hearing.eventLocation = slot.tsDefinition.tsTemplate.dirLocation
addHours = (slot.eventCount/5).intValue()
hearing.startDateTime = slot.eventCount < 5 ? slot.startTime : DateUtil.addMinutes(slot.startTime, (addHours*60))
hearing.endDateTime = DateUtil.addMinutes(hearing.startDateTime, 59)
hearing.slotId = slot.tsDefinition.id
hearing.saveOrUpdate()

//Assign the first active ALJ on the timeslot to the event (There should ever only be 1 active)
person = slot.tsDefinition.tsTemplate.collect("tsTemplatePersons[activeNow == true]").first()
Set events = new HashSet()
events.add(hearing)
CaseAssignment assignment = new CaseAssignment()
assignment.case = _case
assignment.directoryPerson = person.person
assignment.setEvents(events)
assignment.assignmentRole = 'ALJ'
assignment.assignmentSubRole = 'PRE' //Presiding ALJ
assignment.dateAssigned = new Date()
assignment.status = 'CUR'
assignment.statusDate = new Date()
assignment.locationCode = hearing.eventLocation.code
assignment.locationName = hearing.eventLocation.locationName
assignment.locationId = hearing.eventLocation.id
//parentLocation = DirLocation.get(542L) //Zoom
//assignment.parentLocationCode = parentLocation.code
//assignment.parentLocationName = parentLocation.locationName
//assignment.parentLocationId = parentLocation.id
assignment.saveOrUpdate()

//Timeslot Definition sorter -> Sort by start time, then by room name so as to fill room 1 before room 2 etc. 
class TSDefinitionComparator implements Comparator<TSDefinition> {
  // override the compare() method
  public int compare(TSDefinition def1, TSDefinition def2)
  {
    if(def1.startTime.getHours() == def2.startTime.getHours()){
      if(def1.tsTemplate.code > def2.tsTemplate.code){
        return 1
      }else{
        return -1
      }
    }else if(def1.startTime.getHours() > def2.startTime.getHours()){
      return 1
    }else{
      return -1
    }
  }
}




