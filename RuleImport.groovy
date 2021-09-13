// Rule 1 named RS_ClassTest
public class TestClass{
  int id 
  public TestClass(int eyedee){
    this.id = eyedee * 50
  }
  public wuphf(){
    return id
  }
}


// Rule 2 named whatever
import com.sustain.rule.model.RuleDef

this.contextAccessor = RuleDef.exec('RS_ClassTest', this.contextAccessor, null)

testObj = new TestClass(35)
logger.debug testObj.wuphf()
