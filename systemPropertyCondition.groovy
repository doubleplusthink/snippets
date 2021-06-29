//checkIfProd

environment = SystemProperty.get('interfaces.display.environment')
if(environment == 'prod'){
  _result = true
}else {
  _result = false
}
_result
