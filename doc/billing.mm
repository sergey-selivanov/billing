<map version="1.0.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1491491585946" ID="ID_1128470134" MODIFIED="1491491592225" TEXT="Billing">
<node CREATED="1509032178079" ID="ID_159391686" MODIFIED="1525364478689" POSITION="right" TEXT="Requirements">
<node CREATED="1491491597333" ID="ID_1991562451" MODIFIED="1491491602646" TEXT="REST server">
<node CREATED="1491491615234" ID="ID_1584857605" MODIFIED="1525364490474" TEXT="JIRA and Jobcard databases as tasks source">
<icon BUILTIN="button_ok"/>
</node>
<node CREATED="1491491687585" ID="ID_1965207945" MODIFIED="1508857024455" TEXT="Hibernate and two databases">
<icon BUILTIN="button_ok"/>
</node>
<node CREATED="1494066785051" ID="ID_1061821019" MODIFIED="1494066811414" TEXT="session, token to access api methods"/>
</node>
<node CREATED="1491491603314" ID="ID_1604091535" MODIFIED="1491491610078" TEXT="Web UI">
<node CREATED="1491491638858" ID="ID_1282282504" MODIFIED="1491491640887" TEXT="Vaadin">
<node CREATED="1494066817869" ID="ID_888960350" MODIFIED="1494066853627" TEXT="session and token used for rest"/>
</node>
</node>
<node CREATED="1491493337820" ID="ID_1190449575" MODIFIED="1491493954368" TEXT="functionality">
<node CREATED="1491496348635" ID="ID_194051518" MODIFIED="1508857032378" TEXT="select organization/customer">
<icon BUILTIN="button_ok"/>
<node CREATED="1491496374705" ID="ID_542339902" MODIFIED="1525364509485" TEXT="remember last choice"/>
</node>
<node CREATED="1491493340859" ID="ID_1701144470" MODIFIED="1509032114795" TEXT="fetch tasks from Jobcard/Jira">
<icon BUILTIN="button_ok"/>
<node CREATED="1491493652225" ID="ID_518969297" MODIFIED="1509032118077" TEXT="by selected project">
<icon BUILTIN="button_ok"/>
<node CREATED="1491493740146" ID="ID_937020906" MODIFIED="1491493753342" TEXT="list e.g 10 most recent projects"/>
<node CREATED="1491496388831" ID="ID_1298861962" MODIFIED="1491496391858" TEXT="remember recent"/>
</node>
<node CREATED="1491493660546" ID="ID_1383033222" MODIFIED="1509032561102" TEXT="within date range">
<node CREATED="1491493684313" ID="ID_5138200" MODIFIED="1491493693750" TEXT="for example now - 30 days"/>
<node CREATED="1509032541692" ID="ID_2114002" MODIFIED="1509032549474" TEXT="since latest invoice on this project"/>
</node>
</node>
<node CREATED="1491493384491" FOLDED="true" ID="ID_1706608994" MODIFIED="1525364537864" TEXT="show as table">
<icon BUILTIN="button_ok"/>
<node CREATED="1507408523862" ID="ID_1804057396" MODIFIED="1507408535969" TEXT="edit each row: text, hours, rate"/>
<node CREATED="1491493603210" ID="ID_117092037" MODIFIED="1491493615862" TEXT="checkboxes to include/not include"/>
<node CREATED="1491493616506" ID="ID_1977693510" MODIFIED="1491493631870" TEXT="add additional entries"/>
</node>
<node CREATED="1507408555576" ID="ID_173789412" MODIFIED="1508857048911" TEXT="recalculate and show sums">
<icon BUILTIN="button_ok"/>
</node>
<node CREATED="1491493414451" ID="ID_1839234571" MODIFIED="1509032082855" TEXT="save as invoice to own database">
<icon BUILTIN="button_ok"/>
</node>
<node CREATED="1491493422354" ID="ID_15925357" MODIFIED="1527530662043" TEXT="render with jasper server">
<icon BUILTIN="button_cancel"/>
<node CREATED="1507408225375" ID="ID_427352178" MODIFIED="1527530652755" TEXT="or with embedded jasper lib">
<icon BUILTIN="button_ok"/>
</node>
</node>
<node CREATED="1491496400077" ID="ID_1751054242" MODIFIED="1527530678294" TEXT="mark invoice as paid, readonly">
<icon BUILTIN="button_cancel"/>
<node CREATED="1491496433642" ID="ID_1270454591" MODIFIED="1527530693519" TEXT="save pdf">
<icon BUILTIN="button_cancel"/>
</node>
</node>
<node CREATED="1491493533618" ID="ID_428941304" MODIFIED="1527530683680" TEXT="manage existing invoices">
<icon BUILTIN="button_cancel"/>
</node>
<node CREATED="1491493471426" ID="ID_1615724002" MODIFIED="1491493500727" TEXT="authentication via ldap"/>
<node CREATED="1491493502018" ID="ID_588493671" MODIFIED="1491493517102" TEXT="authorization by role from ldap group"/>
<node CREATED="1494066711336" ID="ID_1978481465" MODIFIED="1494066733344" TEXT="customization from properties file in user home directory">
<node CREATED="1494066734137" ID="ID_591444729" MODIFIED="1494066748433" TEXT="color to distinguish jira vs jobcard"/>
</node>
</node>
<node CREATED="1491493965971" ID="ID_613037898" MODIFIED="1491493969005" TEXT="devel">
<node CREATED="1491493969632" ID="ID_1624989986" MODIFIED="1509032137183" TEXT="Jenkinsfile for jenkins pipeline build">
<icon BUILTIN="button_ok"/>
</node>
</node>
</node>
<node CREATED="1507639971505" ID="ID_1396081734" MODIFIED="1507639973521" POSITION="right" TEXT="TODO">
<node CREATED="1525364421884" ID="ID_644469936" MODIFIED="1525364424994" TEXT="manage settings">
<node CREATED="1525364425645" ID="ID_700208912" MODIFIED="1525364579023" TEXT="default rate for jira projects"/>
<node CREATED="1525364441588" ID="ID_1320738387" MODIFIED="1525364470562" TEXT="manual sync instead of auto, button at settings page"/>
</node>
<node CREATED="1509032224174" ID="ID_238998016" MODIFIED="1509032251496" TEXT="properties for environments">
<node CREATED="1509032333093" ID="ID_1076325134" MODIFIED="1509032338422" TEXT="customize colors"/>
</node>
<node CREATED="1509032293453" ID="ID_876487309" MODIFIED="1509032299421" TEXT="auto deploy on mistral"/>
<node CREATED="1509032255390" ID="ID_1298072548" MODIFIED="1509032264960" TEXT="authentication"/>
<node CREATED="1509032265893" ID="ID_1867559860" MODIFIED="1509032269128" TEXT="authorization"/>
<node CREATED="1509032421780" ID="ID_162312050" MODIFIED="1509032436835" TEXT="session on server"/>
<node CREATED="1508857004526" ID="ID_1695181612" MODIFIED="1527530761307" TEXT="done">
<node CREATED="1507639991201" ID="ID_1274160339" MODIFIED="1508856998034" TEXT="add custom items to invoice">
<icon BUILTIN="button_ok"/>
</node>
<node CREATED="1507639974529" ID="ID_304024417" MODIFIED="1509032140183" TEXT="use decimal and bigdecimal for money">
<icon BUILTIN="button_ok"/>
<node CREATED="1507640020937" ID="ID_1335339360" MODIFIED="1507640021766" TEXT="https://rietta.com/blog/2012/03/03/best-data-types-for-currencymoney-in/"/>
</node>
<node CREATED="1509032273454" ID="ID_1793418401" MODIFIED="1527530712096" TEXT="jasper report">
<icon BUILTIN="button_ok"/>
</node>
<node CREATED="1509032353597" ID="ID_581192566" MODIFIED="1525364638406" TEXT="manage customers">
<icon BUILTIN="button_ok"/>
</node>
<node CREATED="1509032358421" ID="ID_156704729" MODIFIED="1527530715477" TEXT="manage projects">
<icon BUILTIN="button_ok"/>
</node>
<node CREATED="1509032211159" ID="ID_1808877419" MODIFIED="1525364682613" TEXT="JIRA tasks provider">
<icon BUILTIN="button_ok"/>
</node>
</node>
<node CREATED="1527530762706" ID="ID_1196275957" MODIFIED="1527530765873" TEXT="rejected">
<node CREATED="1509032491396" ID="ID_910306489" MODIFIED="1527530721522" TEXT="manage invoices">
<icon BUILTIN="button_cancel"/>
</node>
<node CREATED="1509032389205" ID="ID_1627381017" MODIFIED="1527530726128" TEXT="preview pdf and finalize invoice">
<icon BUILTIN="button_cancel"/>
</node>
</node>
</node>
<node CREATED="1491569029336" FOLDED="true" ID="ID_1332314841" MODIFIED="1509032463901" POSITION="left" TEXT="Links">
<node CREATED="1494066568446" ID="ID_720387667" LINK="https://vaadin.com/api/" MODIFIED="1494066586487" TEXT="https://vaadin.com/api/"/>
<node CREATED="1494066599397" ID="ID_1162832728" LINK="https://vaadin.com/icons" MODIFIED="1494066608957" TEXT="https://vaadin.com/icons"/>
<node CREATED="1494069422367" ID="ID_1267267371" LINK="https://demo.vaadin.com/sampler" MODIFIED="1494069433619" TEXT="https://demo.vaadin.com/sampler"/>
<node CREATED="1494069440630" ID="ID_390021433" MODIFIED="1494069442425" TEXT="----"/>
<node CREATED="1494066622717" ID="ID_950223777" LINK="https://demo.vaadin.com/dashboard" MODIFIED="1494066630845" TEXT="https://demo.vaadin.com/dashboard"/>
<node CREATED="1491839402955" ID="ID_746514123" LINK="https://github.com/vaadin/tutorial.git" MODIFIED="1491839423106" TEXT="https://github.com/vaadin/tutorial.git"/>
<node CREATED="1491569052098" ID="ID_1204325679" LINK="https://examples.javacodegeeks.com/enterprise-java/vaadin/vaadin-rest-example/" MODIFIED="1491569069946" TEXT="https://examples.javacodegeeks.com/enterprise-java/vaadin/vaadin-rest-example/"/>
<node CREATED="1491569291041" ID="ID_947717899" LINK="https://github.com/samie/vaadin-rest-sample" MODIFIED="1491569302203" TEXT="https://github.com/samie/vaadin-rest-sample"/>
</node>
</node>
</map>
