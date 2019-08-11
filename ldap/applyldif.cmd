set OPENDJ_HOME=d:\billing-demo\opendj
set HOST=localhost
set PORT=1389
set ADMIN_PASSWORD=Q1w2e3r4t5



call %OPENDJ_HOME%\bat\ldapmodify ^
    --defaultAdd ^
    --hostname %HOST% ^
    --port %PORT% ^
    --bindDN "cn=Directory Manager" ^
    --bindPassword %ADMIN_PASSWORD% ^
    --filename %1
