import com.sustain.form.model.FormItem
goodItem = FormItem.get(146318L)
badItem = FormItem.get(146319L)

replaceWith = "parties.person.personIdentifier.persons.ctTblPaymentSchedules"
pathToReplace = "parties[].person.ctTblPaymentSchedules[]"

itemsToUpdate = goodItem.parentPanel.panelItems.findAll{it?.path?.contains(pathToReplace)}

itemsToUpdate.each{item -> 
  logger.debug( item.path)
  item.path = item.path.replace(pathToReplace, replaceWith)
  logger.debug( item.path)
  //withTx{
  //  item.saveOrUpdate()
  //}
}
