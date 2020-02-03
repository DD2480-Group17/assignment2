# Continuous Integration

#### A continuous integration server that runs test on code automatically when pushed to git.

##### A project by group 17 for the course DD2480 Software Engineering Fundamentals.

A continuous integrations server is a server that runs JUnit test automatically on the code pushed to github. The results from the tests is sent to github which will display it for the
user. If the latest push resulted in failed tests. It is possible to connect to the server directly to get the commit history displayed or detailed information about a certain commit.    

## Files structure
```bash
├── README.md
├── cleanup.sh
├── cloneBuildTest.sh
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       ├── Builder.java
│   │       ├── ContinousIntegrationServer.java
│   │       ├── History.java
│   │       ├── JSONParser.java
│   │       └── LogToString.java
│   └── test
│       └── java
│           ├── HistoryTest.java
│           ├── JSONParserTest.java
│           └── LogToStringTest.java
├── target
│   ├── classes
│   │   ├── Builder.class
│   │   ├── ContinousIntegrationServer.class
│   │   ├── History$1.class
│   │   ├── History$CommitIDAndTimeStampHolder.class
│   │   ├── History.class
│   │   ├── JSONParser.class
│   │   └── LogToString.class
│   ├── generated-sources
│   │   └── annotations
│   ├── generated-test-sources
│   │   └── test-annotations
│   └── test-classes
│       ├── HistoryTest.class
│       ├── JSONParserTest.class
│       └── LogToStringTest.class
└── test_build_history
```
## Tutorial
#### Required software
* Java 11
* Apache Maven 3.6.0
* JUnit (Jupiter 5.0 or later)

#### Run and build the program

#### Run Test Cases

## Open Source Material

## Contributions
We are proud over our implementation of the program and that we used Travis for continuous integration (CI). We have learnt a lot during the lab on how a git project should be structured, by using issues, specific prefix for our commits, pull requests and testing.

In this section it is specified what each person contributed to the project with.

Edvin Ardö:


Marcus Jonsson Ewerbring:

Johanna Iivanainen:


George Rezkalla:

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
