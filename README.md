# HOW START amir-api

### Install JDK

Download and install JDK 8 from this page:
http://www.oracle.com/technetwork/java/javase/downloads/index.html

### Install homebrew (for MacOS users only)

```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

### Install utils

```bash
brew install git wget gedit
```

### Install Redis

```bash
brew install redis
brew services start redis
```


### Install PostgreSQL

```bash
brew install postgresql
```

Add PostgreSQL to auto-load:

```bash
brew services start postgresql
```

after that enable it from psql and create DBs
```
CREATE DATABASE amor;
```
Configure postgres service allow localhost connections without password 
### Configuration

1. Edit postgre configuration file:
    
        sudo gedit /etc/postgresql/POSTGRE_VERSION/main/pg_hba.conf
       
   for MacOS
        
        sudo gedit /usr/local/var/postgres/pg_hba.conf
    
2. Change all configuration access to:
    
        # Database administrative login by Unix domain socket
        local   all             all                                     trust

        # TYPE  DATABASE        USER            ADDRESS                 METHOD

        # "local" is for Unix domain socket connections only
        local   all             all                                     trust
        # IPv4 local connections:
        host    all             all             127.0.0.1/32            trust
        # IPv6 local connections:
        host    all             all             ::1/128                 trust
    
3. Restart postgre server
    
        brew services restart postgresql


### Install SDKMAN!

```bash
curl -s get.sdkman.io | bash
```

Initialization:

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

### Install Grails

```bash
sdk install grails
```

### Install Gradle

```bash
sdk install gradle
```

### Install Groovy

```bash
sdk install groovy
```

#### Dependency report (from back dir)

```bash
./gradlew dependencies
```

### Run project

```
./gradlew bootRun
```

After that api available on <br> 
prod: <b>http://amir.melodrom.ru:8080/api</b>

