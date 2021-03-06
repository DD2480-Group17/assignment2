# Continuous Integration

#### A continuous integration server that runs test on code automatically when pushed to git.

##### A project by group 17 for the course DD2480 Software Engineering Fundamentals.

A continuous integrations server is a server that runs JUnit test automatically on the code pushed to github. The results from the tests is sent to github which will display it for the
user. If the latest push resulted in failed tests. It is possible to connect to the server directly to get the commit history displayed or detailed information about a certain commit.    

## Files structure
The file structure of the program is illustrated bellow:
```bash
├── README.md
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       ├── Builder.java
│   │       ├── ContinousIntegrationServer.java
│   │       ├── History.java
│   │       ├── JSONParser.java
│   │       ├── LogToString.java
│   │       ├── Notifier.java
│   │       └── Utility.java
│   └── test
│       └── java
│           ├── HistoryTest.java
│           ├── JSONParserTest.java
│           ├── LogToStringTest.java
│           └── UtilityTest.java
└── test_build_history
```
The main method is located in `ContinousIntegrationServer` which is a parser for the incoming http request. `Builder` is a class that
builds the project and run tests that are pushed to github. All unit-tests for the program is located in the test folder. The `test_build_history` folder
saves files related to the unit tests.

## Tutorial

#### Required software
* Java 11
* Apache Maven 3.6.0
* JUnit (Jupiter 5.0 or later)

#### Setup Github
In order to use this CI with your github project you need to link github to the computer you run the software on.
* Go to the github repository you want to link to the CI and click on ` Settings `.
* In the settings tab press ` Webhooks ` and press `Add webhook`.
* In `Payload URL` enter `https://COMPUTER_IP:8017/webhook/`, where COMPUTER_IP is the running computers IP. Press `Add webhook`.
* Generate a token at github and put it in a file called `.token` which should added to your repository.

#### Run and build the program
To run and build the program, enter ` mvn -q exec:java -Dexec.mainClass="ContinousIntegrationServer"` in the terminal.

When the server is running on your computer following commands can be used. The server uses the port 8017, however, to access the program you need to use the running computers IP-address or use localhost.

* ` list all recorded commits ` to view all recorded commits, enter the following in your web-browser `http://COMPUTER_IP:8017`, where COMPUTER_IP is the running computers ip. You could also use the URL `http://localhost:8017`
* ` View detailed information about one commit ` to view detailed information about a certain commit enter `http://COMPUTER_IP:8017/commit/?id=COMMIT_ID` or `http://localhost:8017/commit/?id=COMMIT_ID`, where COMMIT_ID are the unique hash for the commit to display. The available hashes can be found on the ` list all recorded commits ` page.


#### Run Test Cases
The program also has test cases for each class.
* On the terminal, first, go to the top directory of the project where the pom.xml file exists. Then, the tests can be executed by the following command ` mvn test`.

Example to show the history of commits:
[History of commits](http://130.229.177.226:8017/)

Example to show a single commit's build process:
[History of the commit cf17df7a8c6f861a6c1fb018490529d8d10eafb9](http://130.229.177.226:8017/commit/?id=cf17df7a8c6f861a6c1fb018490529d8d10eafb9_2020T02T06T10T18T21T429392)

#### How the functionality where tested
The classes ` History `, ` Utility `, ` JSONParser ` and ` LogToString ` are all tested by unit-tests. How to run the unit-tests are explained in [Run Test Cases](#Run Test Cases).
* The ` compilation ` has been tested by pushing code with syntax-errors, the CI reported the program failed to build. Code with correct syntax where pushed to the
CI successful build the program.
* The ` execution ` of test cases are automatically preformed on pushed code. The functionality for execution where test by pushing failing tests to the CI server, which reported failed
test. Then code with successful tests where pushed to the CI server which reported successful test.
* The ` notification ` function where tested by sending several pushes to the repository and visually inspect if we get notification from the CI server. The notification should
inform if the test where successful or not, which it does.


## Contributions
We are proud over our implementation of the program and that we used Travis for continuous integration (CI). We have learnt a lot during the lab on how a git project should be structured, by using issues, specific prefix for our commits, pull requests and testing.
We are also proud that we did tests between out program and github to verify functionality.


In this section it is specified what each person contributed to the project with.

Edvin Ardö:
* Code, implemented JSONParser and CI functionality to clone, build and notify.
* Pull request, reviewed several pull requests.
* Documentation, code documentation.


Marcus Jonsson Ewerbring:
* Code, implemented History, HistoryTest, Utility, UtilityTest and refactored code.
* Pull request, reviewed several pull requests.
* Documentation, README and code documentation.


Johanna Iivanainen:
* Code, implemented History, HistoryTest, HTML, HTMLTest and HTML templet
* Pull request, reviewed several pull requests.
* Documentation, code documentation.


George Rezkalla:
* Code, implemented/refactored handlers to handle requests for listing commit history, a particular commit and webhooks.
* Pull request, reviewed several pull requests.
* Documentation, code documentation.

## Contribution policy: TO DO BEFORE WE COMMIT TO REPO
* Create a test case for the function you have created.
* Test the old test cases and check them locally.
* Create a branch that includes the number of the issue using issue naming convention, like this: `git checkout -b issue/#issueNr`, where `#issueNr` is replaced by the actual issue number. For instance, `git checkout -b issue/2`, where `#issueNr` = 2.
* Describe in the commit message what you have created, modified or fixed in the commit using the commit message convention (see [Commit message convention](#commit-message-convention) below).
* Code is well-documented, according to JavaDocs standard.
* No unnecessary code is added (code that is not used).
* Make sure that the code is correctly implemented.

### Commit message convention
`prefix/#issueNR description-of-commit`

#### Commit Prefixes
`feat`
`fix`
`test`
`refactor`
`doc`

##### Example
`feat/#21 added IOHandler` and, then in the following line, write more detailed description of the modifications done by the commit.
