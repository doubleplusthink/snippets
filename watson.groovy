_entityName; // rule input parameter
_emailTo; // rule input parameter
//Warnings And Tracking for Schema Omissions & Nonconformities
//Courtesy of Team Snack Overflow - Harvest Hack 2025

import com.sustain.metadata.model.MdEntity

def requiredIfFieldExists = [
    "Case": [
        ["status", "statusDate"]
    ],
    "ScheduledEvent": [
        ["resultType", "resultDate"],
        "startDateTime"
    ],
    "Document": [
        "status",
        "dateFiled"
    ],
    "Sentence": [
        "type",
        "statusDate"
    ]
]

def entity = MdEntity.getBySimpleName(_entityName)

def sql = """SELECT id FROM ecourt.${entity.tableName} WHERE id IS NULL"""

// Process lookup fields
def lookupFields = entity.fields.findAll {
    it.lookupListName != null &&
    it.fieldType.toString() == 'PLAIN' &&
    !it.transient_ &&
    !it.name.containsIgnoreCase("accessLevel") &&
    !it.name.containsIgnoreCase('join') &&
    !it.name.containsIgnoreCase('customDocPropsStatus')
}

lookupFields.each { field ->
    def listCodes = LookupList.get(field.lookupListName)?.itemsCodes
    if (listCodes && !listCodes.isEmpty()) {
        def formatted = listCodes.collect { "'${it.toString()}'" }.join(", ")
        sql += """
        OR ${field.name} NOT IN (${formatted})"""
    }
}

// Handle special required fields logic (only for the current entity)
def requiredFields = requiredIfFieldExists[_entityName]
if (requiredFields) {
    requiredFields.each { fieldDef ->
        if (fieldDef instanceof List && fieldDef.size() == 2) {
            def f1 = fieldDef[0]
            def f2 = fieldDef[1]
            sql += """
            OR (
                (${f1} IS NULL AND ${f2} IS NOT NULL)
                OR (${f1} IS NOT NULL AND ${f2} IS NULL)
            )"""
        } else if (fieldDef instanceof String) {
            sql += """
            OR ${fieldDef} IS NULL"""
        }
    }
}

// Execute the SQL
logger.debug("Executing SQL:\n${sql}")
def brokenObjects = DomainObject.querySQL(sql)

// Prepare log output
def outputLines = []

brokenObjects.each { id ->
    def obj = DomainObject.get(_entityName, id.toLong())

    lookupFields.each { field ->
        def value = obj[field.name]
        def codes = LookupList.get(field.lookupListName)?.itemsCodes
        if (value && codes && !codes.contains(value)) {
            outputLines << "${_entityName} ${obj.id} ${obj.title} - ${field.name} '${value}' is not in lookup list ${field.lookupListName}"
        }
    }

    if (requiredFields) {
        requiredFields.each { fieldDef ->
            if (fieldDef instanceof List && fieldDef.size() == 2) {
                def v1 = obj[fieldDef[0]]
                def v2 = obj[fieldDef[1]]
                def isBroken = (v1 == null && v2 != null) || (v1 != null && v2 == null)
                if (isBroken) {
                    outputLines << "${_entityName} ${obj.id} ${obj.title} - ${fieldDef[0]} and ${fieldDef[1]} must both be null or both be populated, but found: ${v1} / ${v2}"
                }
            } else if (fieldDef instanceof String) {
                def v = obj[fieldDef]
                if (v == null) {
                    outputLines << "${_entityName} ${obj.id} ${obj.title} - Required field '${fieldDef}' is null"
                }
            }
        }
    }
}

// Create temp file for results
def resultFile = File.createTempFile("watson_${_entityName}_", ".txt")
resultFile.withWriter('UTF-8') { writer ->
    outputLines.each { writer.writeLine(it) }
}

// Summary
def summary = """WATSON completed for ${_entityName}:

  Fields checked: ${lookupFields.size()}
  Broken records found: ${brokenObjects.size()}
"""

logger.info(summary)

// Parse _emailTo and send email with file
def emailRecipients = _emailTo?.split(',')*.trim()
if (emailRecipients && emailRecipients.size() > 0) {
    new Email()
        .to(*emailRecipients)
        .subject("WATSON results for ${_entityName}")
        .body(summary)
        .attachment(resultFile)
        .send()
}



