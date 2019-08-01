// declarative pipeline
// https://jenkins.io/doc/book/pipeline/syntax/

pipeline{
    agent{
        label '!vm'
    }
    parameters {
        string(name: 'BRANCH', defaultValue: 'develop', description: 'Git branch to build, e.g. develop, master')
        string(name: 'TARGETS', defaultValue: 'mistral-jobcard mistral-jira', description: 'Space separated: mistral-jobcard mistral-jira')
    }
    options{
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    tools{
        gradle 'default'
    }
    stages{
        stage('checkout'){
            steps{
                checkout(
                    [$class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH}"]],
                    userRemoteConfigs: [[url: 'ssh://git@gitserver/var/git/billing.git']]])
            }
        }
        //stage('build'){
        //    failFast true
        //    parallel{
                stage('mistral-jobcard'){
                    // agent { ... // checkout should be on the same node?
                    when{ expression { "${params.TARGETS}".contains('mistral-jobcard') } }
                    steps{
                        script {
                            if(isUnix()) {
                                sh 'gradle -PenvironmentName=mistral-jobcard clean war'
                            }
                            else {
                                bat 'gradle -PenvironmentName=mistral-jobcard clean war'
                            }
                        }
                    }
                    post{
                        success{
                            archiveArtifacts '**/build/libs/*.war'
                        }
                    }
                }
                stage('mistral-jira'){
                    when{ expression { "${params.TARGETS}".contains('mistral-jira') } }
                    steps{
                        script {
                            if(isUnix()) {
                                sh 'gradle -PenvironmentName=mistral-jira billing-server:clean billing-web:clean war'
                            }
                            else {
                                bat 'gradle -PenvironmentName=mistral-jira billing-server:clean billing-web:clean war'
                            }
                        }
                    }
                    post{
                        success{
                            archiveArtifacts '**/build/libs/*.war'
                        }
                    }
                }
                stage('check'){
                    steps{
                        script{
                            if(isUnix()) {
                                sh 'gradle -PenvironmentName=mistral-jira check'
                            }
                            else {
                                bat 'gradle -PenvironmentName=mistral-jira check'
                            }
                        }
                    }
                }

        //    }
        //}
        stage('analysis'){    // TODO analyze only one environment
            steps{
                warnings canComputeNew: true,
                    canResolveRelativePaths: false,
                    categoriesPattern: '',
                    consoleParsers: [[parserName: 'Java Compiler (javac)']],
                    defaultEncoding: '', excludePattern: '', healthy: '', includePattern: '', messagesPattern: '', unHealthy: ''
                openTasks canComputeNew: true, defaultEncoding: '', excludePattern: '', healthy: '', high: '', low: '', normal: 'TODO', pattern: '', unHealthy: ''
                findbugs canComputeNew: true, defaultEncoding: '', excludePattern: '', healthy: '', includePattern: '', pattern: '**/build/reports/findbugs/main.xml', unHealthy: ''
                checkstyle canComputeNew: true, defaultEncoding: '', healthy: '', pattern: '**/build/reports/checkstyle/main.xml', unHealthy: ''
            }
        }
    }
}
