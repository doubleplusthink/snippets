import com.sustain.menu.model.MenuItem
import com.sustain.menu.model.Menu

menuIds = [62L]
menuIds.each{ id -> 
  menu = Menu.get(id)
  menuText = '\n' + menu.name
  root = menu.root
  menuText += appendChildrenText(root)
  logger.debug( menuText)
}

def appendChildrenText(item){
  result = '\n' + item.label
  if(item.children.size() > 0){
    item.children.each{
      result += appendChildrenText(it)
    }
  }
  return result
}
