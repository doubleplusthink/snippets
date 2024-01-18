deleteEntries = [98346L, 98344L, 98345L, 97741L, 98343L, 97740L]

sql = """
Delete from ecourt.tDirEntry_parents
where children_id in ${deleteEntries};

Delete from ecourt.tDirEntry_related
where related_id in ${deleteEntries};

Delete from ecourt.tDirOrgUnit
where id in ${deleteEntries};

Delete from ecourt.tDirTelephone
where dirEntry_id in ${deleteEntries};

Delete from ecourt.tDirAddress
where dirEntry_id in ${deleteEntries};

Delete from ecourt.tDirEntry_parents
where parents_id in ${deleteEntries};

Delete from ecourt.tDocketDay_createdUserBuckets
where DocketDay_id in (
select id from ecourt.tDocketDay
where location_id in ${deleteEntries}
);

Delete from ecourt.tDocketDay
where location_id in ${deleteEntries};

Delete from ecourt.tCourtRoomPersonnel
where location_id in ${deleteEntries};

Delete from ecourt.tTillParticipant
where tillGroup_id in (
select id from ecourt.tTillGroup
where groupLocation_id in ${deleteEntries}
);

Delete from ecourt.tTillDefAgencyAccount
where tillDef_id in (
select id from ecourt.tTillDef
where tillGroup_id in (
select id from ecourt.tTillGroup
where groupLocation_id in ${deleteEntries}
)
)

Delete from ecourt.tTillDef
where tillGroup_id in (
select id from ecourt.tTillGroup
where groupLocation_id in ${deleteEntries}
);

Delete from ecourt.tTillGroup
where groupLocation_id in ${deleteEntries};

Delete from ecourt.tTSSlotDefinitionEventType
where tsDefinition_id in (
select id from ecourt.tTSDefinition
where tsTemplate_id in(
select id from ecourt.tTSTemplate
where dirLocation_id in ${deleteEntries}
)
);

Delete from ecourt.tTimeSlotHold
where definition_id in (
select id from ecourt.tTSDefinition
where tsTemplate_id in(
select id from ecourt.tTSTemplate
where dirLocation_id in ${deleteEntries}
)
);

Delete from ecourt.tTSDefinition
where tsTemplate_id in(
select id from ecourt.tTSTemplate
where dirLocation_id in ${deleteEntries}
);

Delete from ecourt.tTSTemplate
where dirLocation_id in ${deleteEntries};

Delete from ecourt.tCalendarDayBucket
where dirEntry_id in ${deleteEntries};

Delete from ecourt.tDirLocation
where id in ${deleteEntries};

Delete from ecourt.tDirEntry
where rootDirEntry_id in ${deleteEntries};

Delete from ecourt.tDirEntry
where id in ${deleteEntries};
select * from ecourt.tDirEntry
where id in ${deleteEntries};
""".replace('[', '(').replace(']', ')')


logger.debug( sql)
logger.debug( DomainObject.querySQL(sql))

