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
                	sh 'pwd'
                    sh 'cp ./target/reportexporter.jar /home/okontek/report_exporter/reportexporter.jar'
                    sh 'docker-compose build && docker-compose down && docker-compose up -d'
                }
            }
        }
    }
}