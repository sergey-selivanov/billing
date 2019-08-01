copy /y gradle-local-jira.properties gradle-local.properties
pushd server\src\main\resources
copy /y options-jira.properties options.properties
popd
pushd webui\src\main\resources
copy /y options-jira.properties options.properties
popd
