//Delete Bank Rule
import com.sustain.financial.model.BankStatement
import com.sustain.financial.model.BankTransaction
bankids = [6L, 8L, 7L, 9L, 4L]
reconciliationSetupIds = []
bankStatementIds = []
bankTransactionIds = []

bankids.each{ bnkid -> 
  bank = Bank.get(bnkid)
  bank.reconciliationSetups.id.each{ rs ->
    reconciliationSetupIds.add(rs)
  }
  bank.bankStatements.id.each{ bs ->
    bnks = BankStatement.get(bs)
    bankStatementIds.add(bs)
    bnks.bankTransactions.id.each{bt -> 
      bnkt = BankTransaction.get(bt)
      bankTransactionIds.add(bt)
    }
  }
}

logger.debug bankids = [6L, 8L, 7L, 9L, 4L]
logger.debug reconciliationSetupIds
logger.debug bankStatementIds
logger.debug bankTransactionIds

sql = """
Delete from ecourt.tBankTransaction
where id in ${bankTransactionIds};

Delete from ecourt.tBankStatement
where id in ${bankStatementIds};

Delete from ecourt.tReconciliationSetup_monInstSu
where reconciliationSetup_id in ${reconciliationSetupIds};

Delete from ecourt.tReconciliationSetup
where id in ${reconciliationSetupIds};

DELETE from ecourt.tCheckBatch
where bank_id in ${bankids};

DELETE from ecourt.tBankPrinter
where Bank_id in ${bankids};

Delete from ecourt.tBank 
where id in ${bankids};
""".replace('[', '(').replace(']', ')')

DomainObject.querySQL(sql)
