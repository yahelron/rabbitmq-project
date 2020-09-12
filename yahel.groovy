def label = "jenkins-pod-RunTest-${UUID.randomUUID().toString()}"
podTemplate(label: label, containers: [
		containerTemplate(name: 'centos', image: 'centos:7', ttyEnabled: true),
		containerTemplate(name: 'ubuntu', image: 'ubuntu:latest', ttyEnabled: true)]) {
	node(label) {
		stage('Run CentOS') {
			container('centos'){
				sh "cat /etc/*-release"

			}
		}
		stage('Run Ubuntu') {
			container('ubuntu'){
			  sh ''' 
			  apt update -y
			  apt install -y curl
              curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
              chmod +x /usr/local/bin/docker-compose
              apt update -y
              apt install -y apt-transport-https ca-certificates curl software-properties-common
              curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
              add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
              apt update -y
              apt-cache policy docker-ce
              apt install -y docker-ce
              apt install -y iputils-ping
              apt install -y git
              git clone https://github.com/yahelron/rabbitmq-project.git
              cd rabbitmq-project/consumer/
              docker login -u yahel777 -p akuoknkfv12
              docker build -t consumer .
              docker tag consumer yahel777/consumer:latest
              docker push yahel777/consumer
        '''
			}
		}
	}
}