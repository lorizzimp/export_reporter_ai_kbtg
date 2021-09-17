pipeline {
    agent any
    stages{
        stage('Preparation') { // for display purposes
            steps{
                script{
                    git(
                        url: 'https://github.com/lorizzimp/export_reporter_ai_kbtg.git',
                        credentialsId: 'github',
                        branch: "main"
                    )
                }
            }
        }
        stage('Build') {
            steps{
                script{
                    def mvnHome = tool 'm3'
                    withEnv(["MVN_HOME=$mvnHome"]) {
                        if (isUnix()) {
                            sh '"$MVN_HOME/bin/mvn" -Dmaven.test.failure.ignore clean package'
                        } else {
                            bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean package/)
                        }
                    }
                }
            }
        }
        stage('deploy') {
            steps{
                script{
                	sh 'sudo su'
                    sh 'cp ./target/reportexporter.jar /home/okontek/report_exporter/reportexporter.jar'
                    sh 'cd /home/okontek/report_exporter/'
                    sh 'pwd'
                    sh 'whoami'
                    sh 'docker-compose version'
                    sh '. /home/okontek/report_exporter/restart_service.sh'
                }
            }
        }
    }
}