Where where = new Where()
where.addEquals('subCase', 68L)

statuses = DomainObject.find(SubCaseStatus.class,where,maxResult(10000))
statuses.each{
  Thread t = new Thread(new Loader(it))
  t.start()
}

class Loader implements Runnable {
  SubCaseStatus status
    public Loader(com.sustain.cases.model.SubCaseStatus scs) {
        status = scs
    }
 	public void run() {
        status.delete()
    }
}
