import  com.sustain.form.model.Form
import  com.sustain.form.model.FormItem

frm = Form.get(2338L)
items = frm.formItems.findAll{it.type in [0,2] && it.path.startsWith('documents')}
items.each{
  it.path = it.path.replace('documents', 'subCases.documents')
  it.saveOrUpdate()
}
