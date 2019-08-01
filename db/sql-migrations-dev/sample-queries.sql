insert into Recipient (id, name, address) values (5, 'svs', 'Address\nsisis');
insert into Recipient (id, name, address) values (10, 'bill', 'dfvdfvfd\ndfvdfvdfv');

insert into Customer (id, name, address) values (6, 'customer one', 'Address\nsisis');
insert into Customer (id, name, address) values (11, 'other customer', 'dfvdfvfd\ndfvdfvdfv');


insert into Invoice (name, description, createdDateUTC, recipientId, customerId)
    values ('aaa', 'bla bla bb', utc_timestamp, (select id from Recipient where name='svs'), (select id from Customer where name='customer one'));
insert into Invoice (name, description, createdDateUTC, recipientId, customerId)
    values ('olool', 'kjdf kjdfvn dfvjn', utc_timestamp, (select id from Recipient where name='bill'), (select id from Customer where name='other customer'));





select
    p.Name,
    c.Name
from Project p
left join Customer c on p.originalCustomerId = c.originalId
where p.customerId is null;

update Project p
inner join Customer c on p.originalCustomerId = c.originalId
set p.customerId = c.id
where p.customerId is null;

--
-- jobcard
--

select
    wh.WorkHoursRowID,
    wh.Hours,    -- decimal: 1.5
    wh.Description,
    wh.Date,
    pu.ProjectUnitID,
    pu.Name,
    p.ProjectID,
    p.Name,
    -- coalesce(puParent.Name, '')
    case when puParent.Name is null then pu.Name else puParent.Name + ': ' + pu.Name end as puname
from WorkHours wh
left join ProjectUnit pu on pu.ProjectUnitID = wh.ProjectUnitID
left join Project p on p.ProjectID = pu.ProjectID
left join ProjectUnit puParent on pu.ParentProjectUnitID = puParent.ProjectUnitID
where
    p.Name = 'FY 2018 Support'
    and wh.Date > '2017-08-01'

-- get customers
select
    c.ClientID,
    c.CompanyName,
    coalesce(a.AddressLine1, '') as addr1,
    coalesce(a.AddressLine2, '') as addr2,
    coalesce(a.City, '') as city,
    rtrim(coalesce(cast(sp.StateProvinceCode as varchar), '')) as state, --   // no dialect mapping for jdbc type -15
    coalesce(a.ZipCode, '') as zip,
    coalesce(c.Comments, '')
from Client c
left join ClientAddress a on a.ClientID = c.ClientID
left join StateProvince sp on sp.StateProvinceID = a.StateProvinceID


-- jira

select
    wl.ID,
    wl.timeworked,    -- seconds: 1800 for 30 min
    wl.worklogbody,
    wl.STARTDATE,
    i.ID as issueId,
    i.SUMMARY,
    p.ID as projectId,
    p.pname,
    concat(p.pkey, '-', i.issuenum) as issueLabel
from worklog wl
left join jiraissue i on i.ID = wl.issueid
left join project p on p.ID = i.PROJECT
where
    p.pname = 'Billing app'
    and wl.STARTDATE > '2017-10-01'


-- https://community.atlassian.com/t5/Jira-questions/Get-issues-by-sprint-from-database/qaq-p/422957

SELECT
    c.STRINGVALUE AS SprintId,
    i.pkey AS Task,
    i.summary,
    s.name
FROM customfieldvalue AS c
left JOIN jiraissue i ON (i.ID=c.ISSUE)
left join AO_60DB71_SPRINT s on s.id = c.stringvalue
WHERE CUSTOMFIELD = (select id from customfield where cfname='Sprint')


select
    a.CREATED,
    a.SUMMARY,
    p.pname,
    p.ID
from audit_log a
left join project p on p.ID = a.OBJECT_ID
where SUMMARY = 'Project created'
and p.ID is not null
and a.CREATED > '2017-04-06'
order by a.CREATED desc

