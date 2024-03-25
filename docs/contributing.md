# Contributing

> A developer onboarding notes

## Prerequisites

- JDK 17
- IntelliJIDEA
- Docker
- Bash console
- Git client

## Terminal Emulator

The bash-like syntax is used all over the commands and scripts. Use `bash`/`zsh` as a console to avoid cross-platform issues. 
On a Windows machine, use Git bash installed with the Git for Windows client.

## Development Commands
### Clone project

In a project folder:
```bash
git clone git@github.com:vecondev/buildoptima-api.git
```
or
```bash
git clone https://github.com/vecondev/buildoptima-api.git
```

### Git Config

> Important: commit using the corporate identity

In the project folder:

```bash
git config user.name "FIRST_NAME LAST_NAME"
git config user.email "USER_NAME@vecondev.com"
```

Or use `--global` flag to apply Git settings globally.


### Docker PostgreSQL Local Database Setup

> Important: If you already have running postgresql on your local machine you can skip this step.

For setup and running postgresql on your local machine, please follow steps.

Go to project root command line:

```bash
docker run postgresql:14.2
```
For stopping postgresql on your local machine, please follow steps.

Go to project [folder](../):

```bash
docker run postgres:14
```

### Maven Test, Build and Validate

```bash
mvn clean install
mvn test
mvn verify
```

## Committing Changes

Before a commit, run `mvn clean test validate`.
Place meaningful comments to the commits.
Use work items referencing via `[ID]` when possible. Where `[ID]` is JIRA issue identifier.

## Git Flow

- `develop` branch is protected
- Pull Requests used for merging changes to the protected branches
- Pull Requests used for code review
- A developer uses a personal dev branch (`[name] {issue description}`) for ongoing changes for unstructured work blocks
- After merged, feature and bug fix branches should be deleted


## Pipelines
GitHub Actions used for automation.

A CI pipeline with build and tests starts on each commit to the remote repo.
A CI/CD pipeline used to create image and push it to AWS ECR then it should be used to deploy application in EC2
