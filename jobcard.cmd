copy /y gradle-local-jobcard.properties gradle-local.properties
pushd server\src\main\resources
copy /y options-jobcard.properties options.properties
popd
pushd webui\src\main\resources
copy /y options-jobcard.properties options.properties
popd
